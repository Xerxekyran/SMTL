package de.digitalforger.smtl.trace

import de.digitalforger.smtl.logging.LogHelper
import ETraceConnectionType._
import scala.collection.mutable.HashMap
import java.util.LinkedList
import de.digitalforger.smtl.m2m.IRule
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.Platform
import org.eclipse.core.runtime.ISafeRunnable
import org.eclipse.core.runtime.SafeRunner

/**
 * The Tracer is used to save and read the connections between source and target model elements
 * in a transformation process.
 *
 * @author Lars George
 */
class Tracer extends LogHelper {

  var showDirectTrace = false

  private val ADDTRACER_ID = "de.digitalforger.smtl.tracer"

  /**
   * logs a backtrack -> which source element was used to create the target element?
   */
  private var reverseTraceLog = new HashMap[AnyRef, AnyRef]

  /**
   * Every element has a list of elements it is connected to with a certain type
   */
  private var traceLogBySourceElement = new HashMap[AnyRef, LinkedList[(AnyRef, ETraceConnectionType)]]

  /**
   * This log keeps additionally track of which rule created which target element from a source element
   */
  private var traceLogByRuleAndSourceElement = new HashMap[(IRule, AnyRef), LinkedList[(AnyRef, ETraceConnectionType)]]

  /**
   * add a new entry to the trace
   *
   * @param from
   * 	The source model element for the trace
   * @param to
   * 	The target model element for the trace
   * @param byRule
   * 	The rule that was used for the transformations
   * @param connectionType
   * 	The connection type bewteen the source and target element
   */
  def trace(from: AnyRef, to: AnyRef, byRule: IRule, connectionType: ETraceConnectionType) = {
    // first the traceLogBySourceElement

    // is there an entry for this from element?
    traceLogBySourceElement.get(from) match {
      // yes, just push the new connection in the list  
      case Some(x) => x.push((to, connectionType))

      // no, create a new list and then add the first element
      case None =>
        var newList = new LinkedList[(AnyRef, ETraceConnectionType)]()
        newList.push((to, connectionType))
        traceLogBySourceElement += from -> newList
    }

    // now the traceLogByRuleAndSourceElement
    traceLogByRuleAndSourceElement.get((byRule, from)) match {
      case Some(x) => x.push((to, connectionType))

      case None =>
        var newList = new LinkedList[(AnyRef, ETraceConnectionType)]()
        newList.push((to, connectionType))
        traceLogByRuleAndSourceElement += (byRule, from) -> newList
    }

    // finally the reverse trace log
    reverseTraceLog.get(to) match {
      case Some(x) => logger.warn("Reverse log overwrites an element: Two traces for the same target element?")
      case None =>
    }
    reverseTraceLog += to -> from

    if (showDirectTrace)
      logger.info("Rule [" + byRule + "] made new connection from [" + from + "] to [" + to + "] of type [" + connectionType + "]")

    // check for additional tracers from the extension point
    val p = Platform.getExtensionRegistry()
    if (p != null) {
      try {
        val config = p.getConfigurationElementsFor(ADDTRACER_ID)

        // evaluate the extension
        for (e <- config) {
          // if its an additional tracer, execute it in a safe environment
          e.createExecutableExtension("class") match {
            case addTracer: IAdditionalTracer =>
              val runnable = new SafeRunner(addTracer, from, to, byRule)
              SafeRunner.run(runnable)
          }
        }
      } catch {
        case ex: CoreException => logger.error(ex.getMessage());
      }
    }

  }

  /**
   * Getter for the source element that was used to create the given object. If no object could be found, null is returned
   *
   * @param targetEl
   * 	The target element for which a source element should be found
   *
   * @return The source element for the given target element. If no one can be found, null will be returned
   */
  def getSourceElementForCreatedElement(targetEl: AnyRef): AnyRef = {

    reverseTraceLog.get(targetEl) match {
      case Some(x) => x
      case None => null
    }
  }

  /**
   * Getter for trace log entries for a certain object
   *
   * @param from
   * 	The source element for which created elements shall be retrieved
   *
   * @return a list of tuples with the created elements of the given source element
   */
  def getElementsCreatedBy(from: AnyRef): LinkedList[(AnyRef, ETraceConnectionType)] = {
    traceLogBySourceElement.get(from) match {
      case Some(x) => x;
      case None => new LinkedList[(AnyRef, ETraceConnectionType)]()
    }
  }

  /**
   * Getter for the trace log entries for a certain object created by a certain rule
   *
   * @param from
   * 	the source model element
   * @param byRule
   * 	the rule that was used
   *
   * @return  a list of target model elements that where created from the given source model element and rule
   */
  def getElementsCreatedBy(from: AnyRef, byRule: IRule): LinkedList[((AnyRef, ETraceConnectionType))] = {
    traceLogByRuleAndSourceElement.get((byRule, from)) match {
      case Some(x) => x
      case None => new LinkedList[(AnyRef, ETraceConnectionType)]()
    }
  }

  /**
   * helper object to execute a safe runner for the additional tracers
   */
  class SafeRunner(addTracer: IAdditionalTracer, from: AnyRef, to: AnyRef, byRule: IRule) extends ISafeRunnable with LogHelper {
    def handleException(exception: Throwable) = {
      logger.error("Error executing additional tracer: " + exception)
    }

    /**
     * executes the additional tracer
     */
    def run() = {
      addTracer.trace(from, to, byRule)
    }
  }

}
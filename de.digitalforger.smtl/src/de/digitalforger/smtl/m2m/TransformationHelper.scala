package de.digitalforger.smtl.m2m

import scala.collection.mutable.HashSet
import scala.collection.JavaConversions._
import org.eclipse.emf.ecore.EObject
import de.digitalforger.smtl.logging.LogHelper
import de.digitalforger.smtl.trace.ETraceConnectionType
import scala.collection.mutable.LinkedList

/**
 * An object of this class is given to the execution methods of rules of a transformation.
 * Service methods to perform actions in the transformations are given
 *
 * @author Lars George
 */
class TransformationHelper(t: TransformationM2M) extends LogHelper {

  // set this helper as the current helper object for the syntax enhancer
  TransformationHelperSyntax.setHelper(this)

  /**
   * this attribute stores the current executed rule, so tracing can be done in method without this context
   */
  protected var currentExecutedRule: IRule = null
  def setCurrentExecutedRule(r: IRule) = { currentExecutedRule = r }
  def getCurrentExecutedRule(): IRule = { this.currentExecutedRule }

  protected var currentSourceObject: EObject = null
  def setCurrentSourceObject(obj: EObject) = { currentSourceObject = obj }
  def getCurrentSourceObject(): EObject = { this.currentSourceObject }

  /**
   * Getter for the transformation object
   * 
   * @return the transformation object this helper belongs to
   */
  def getTransformation() = t

  /**
   * Creates a new object in the output model and returns its instance
   *
   * @param typeName 
   * 	the string representation of the metamodel type name that should be created
   *
   * @return the newly created element
   */
  def createNewTargetElement(typeName: String): EObject = {
    val newObject = t.getToModel().newElement(t.getToModel().getReferenceModel().getMetaElementByName(typeName)).asInstanceOf[EObject]

    // trace if we can
    if (currentExecutedRule != null && currentSourceObject != null) {
      t.getTracer().trace(currentSourceObject, newObject, currentExecutedRule, ETraceConnectionType.Created)

      this.t.getCreatedElements().add(newObject)
    } else
      logger.warn("Created a new object but wasnt able to trace it, because the current rule or source object was not set")

    newObject
  }

  /**
   * operator style call of "createNewTargetElement(typeName: String)"
   *
   * @param typeName 
   * 	the string representation of the metamodel type name that should be created
   *
   * @return the newly created element
   */
  def +(typeName: String): EObject = createNewTargetElement(typeName)

  /**
   * Creates a new object in the output model and returns its instance
   *
   * @param cls 
   * 	the class representation of the metamodel type that should be created
   *
   * @return the newly created element
   */
  def createNewTargetElement[T <: EObject](cls: Class[T]): T = {
    val ret: EObject = createNewTargetElement(cls.getSimpleName())
    ret.asInstanceOf[T]
  }

  /**
   * operator style call of "createNewTargetElement[T <: EObject](cls : Class[T])"
   *
   * @param cls 
   * 	the class representation of the metamodel type that should be created
   *
   * @return the newly created element
   */
  def +[T <: EObject](cls: Class[T]): T = createNewTargetElement(cls)

  /**
   * getter of the created elements
   *
   * @return all created elements
   */
  def getCreatedElements() = t.getCreatedElements()

  /**
   * access method to certain created objects
   *
   * @param cls
   * 	the class object of the the meta model type that should be retrieved
   *
   * @return the set of created objects with the given type
   */
  def getCreatedElementsOfType(cls: Class[_ <: EObject]): HashSet[EObject] = {
    getCreatedElements().filter((s) => {
      if (cls.isAssignableFrom(s.getClass()))
        true
      else false
    })
  }

  /**
   * Getter for objects of the input model
   *
   * @param cls
   * 	the class object of the meta model type in the source model that should be retrieved
   *
   * @return the set of objects in the source model that have the given type
   */
  def getAllElementsOfInputType(cls: Class[_ <: EObject]): Set[EObject] = {
    t.getFromModel().getElementsByType(t.getFromModel().getReferenceModel().getMetaElementByName(cls.getSimpleName()))
  }

  /**
   * This method searches in all rules of the transformation to find one that matches the given signature (converting from sourceMM1 to targetMM2).
   * If its a unique rule that it is additionally checked, if this transformation already was performed, than the already created target object is returned
   *
   * @param sourceMM1
   * 	The source object that should be transformed
   *
   * @param targetMM2
   * 	The class to whitch the source should be transformed to
   *
   * @return The transformed source to an instance of targetMM2, otherwise an exception is thrown
   */
  def transform[source <: EObject, targetClass <: EObject](sourceMM1: source, targetMM2: Class[targetClass]): targetClass = {
    var found = false

    // search in all rules
    for (rule <- t.getRules()) {
      if (rule.getFromManifest().erasure.isAssignableFrom(sourceMM1.getClass()) &&
        rule.getToManifest().erasure.isAssignableFrom(targetMM2) &&
        rule.isApplicable(sourceMM1.asInstanceOf[rule.TA]) &&
        !rule.isToMany()) {

        // so the rule has the right signature, now check if its a unique rule
        if (rule.getIsUniqueRule) {
          // unique rules return already created objects instead of creating new one for already transformed objects
          val lst = t.getTracer().getElementsCreatedBy(sourceMM1, rule)

          // check the entries for a target object that was already created for this source element
          for (tpl <- lst) {
            if (tpl._1.isInstanceOf[targetClass]) {
              // match -> return this one instead of creating a new element
              return tpl._1.asInstanceOf[targetClass]
            }
          }
        }

        // if we get here, we need to create a new element using the found rule
        found = true
        val newElement = rule.exec(sourceMM1.asInstanceOf[rule.TA]).asInstanceOf[targetClass]

        // trace the newly created element
        // this.t.getTracer().trace(sourceMM1, newElement, rule, ETraceConnectionType.Created)

        return newElement
      }
    }

    if (!found) {
      logger.error("TransformationHelper could not find a rule that transforms [" + sourceMM1 + "] to an [" + targetMM2 + "]")
      throw new Exception("TransformationHelper could not find a rule that performs the transformation...")
    }
    null.asInstanceOf[targetClass]
  }

  /**
   * Transforming one source element to many target elements
   * 
   * @param source
   * 	The source object that should be transformed
   * 
   * @param targetMM2
   * 	A class representing the wanted target type
   * 
   * @return a list of created target elements from the given source element
   */
  def transformToMany[source <: EObject, targetClass <: EObject](sourceMM1: source, targetMM2: Class[targetClass]): LinkedList[targetClass] = {
    var ret = new LinkedList[targetClass]()
    var found = false

    for (rule <- t.getRules()) {
      if (rule.getFromManifest().erasure.isAssignableFrom(sourceMM1.getClass()) &&
        rule.getToManifest().erasure.isAssignableFrom(targetMM2) &&
        rule.isApplicable(sourceMM1.asInstanceOf[rule.TA]) &&
        rule.isToMany()) {
        found = true

        // execute the rule on it with the one to many execution
        val newElements = rule.execToMany(sourceMM1.asInstanceOf[rule.TA])

        for (newEl <- newElements) {
          // trace the newly created element
          this.t.getTracer().trace(sourceMM1, newEl, rule, ETraceConnectionType.Created)
        }

      }
    }

    if (!found) {
      logger.error("TransformationHelper could not find a rule that transforms [" + sourceMM1 + "] to an [" + targetMM2 + "]")
      throw new Exception("TransformationHelper could not find a rule that performs the transformation...")
    }
    ret
  }

}

/**
 * This object supports better sntax using a transformation helper
 * the drawback is, that it can only be used in a single threaded transformation environment.
 *
 * @author Lars George
 */
object TransformationHelperSyntax extends LogHelper {

  private var tHelper: TransformationHelper = null

  /**
   * setter for the transformation helper
   * 
   * @param transf 
   * 	the helper object that should be used
   */
  def setHelper(transf: TransformationHelper): Unit = {
    this.tHelper = transf
  }

  /**
   * creates a new element of the given type
   *
   * @return the newly created target model element of the given type
   */
  def create[T <: EObject: Manifest](): T = {

    var ret: T = null.asInstanceOf[T]

    try {
      val man = manifest[T]
      ret = tHelper.createNewTargetElement(man.erasure.asInstanceOf[Class[T]])
    } catch {
      case e: Exception => logger.error("Could not create the target model element: " + e)
    }

    ret
  }

  /**
   * Syntax definition for implicit transformation calling. An error will be thrown if the transformation has no rule to do the conversion
   *
   * @param from 
   * 	the source model element that should be transformed
   *
   * @return returns the transformed model element
   */
  def as[T <: EObject: Manifest](from: EObject): T = {
    var ret: T = null.asInstanceOf[T]

    try {
      val man = manifest[T]
      ret = tHelper.transform(from, man.erasure.asInstanceOf[Class[T]])
    } catch {
      case e: Exception => logger.error("Could not transform incoming object: " + e)
    }

    ret
  }

  /**
   * Syntax definiton to register a model element
   * 
   * @param obj 
   * 	the target-model-element that should be registered in the model
   *
   */
  def register[T <: EObject: Manifest](obj: T): Unit = {
    tHelper.getTransformation().getToModel().registerNewElement(obj)
  }

  /**
   * Syntax definition to get input elements of a given type
   *
   * @return A list of source model elements of the given type
   */
  def getInputElements[T <: EObject: Manifest](): Set[EObject] = {

    try {
      val man = manifest[T]
      return tHelper.getAllElementsOfInputType(man.erasure.asInstanceOf[Class[T]])
    } catch {
      case e: Exception => logger.error("Could not get the input model elements of the given type: ")
    }
    return null
  }

  /**
   * Syntax definition to get created elements of a given type
   *
   * @return A Set of created target model elements of the given type
   */
  def getCreatedElements[T <: EObject: Manifest](): HashSet[EObject] = {

    try {
      val man = manifest[T]
      return tHelper.getCreatedElementsOfType(man.erasure.asInstanceOf[Class[T]])
    } catch {
      case e: Exception => logger.error("Could not get the created model elements of the given type: ")
    }
    return null
  }
}
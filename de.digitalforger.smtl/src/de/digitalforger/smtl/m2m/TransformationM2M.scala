package de.digitalforger.smtl.m2m

import java.util.Collections
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.HashSet
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.emf.ecore.EObject
import de.digitalforger.smtl.core.model.emf.EMFModel
import de.digitalforger.smtl.core.model.emf.EMFModelFactory
import de.digitalforger.smtl.core.model.emf.EMFModelInjector
import de.digitalforger.smtl.core.model.emf.EMFReferenceModel
import de.digitalforger.smtl.logging.LogHelper
import de.digitalforger.smtl.core.ITransformation
import de.digitalforger.smtl.trace.Tracer
import de.digitalforger.smtl.trace.ETraceConnectionType
import java.util.LinkedList

/**
 * The Transformation class collects all needed parts for a model to model transformation using SMTL.
 *
 * @author Lars George
 */
class TransformationM2M(name: String) extends ITransformation {

  private var toModel: EMFModel = null
  private var refining = false
  private var createdElements = new HashSet[EObject]
  private var transformationHelper: TransformationHelper = new TransformationHelper(this)
  private var tracer: Tracer = new Tracer()
  private var phases = new LinkedList[HashSet[IRule]];
  private var currentPhaseToAddID = 0;
  private var activePhase = 0;
  /**
   * @inheritDoc
   */
  override def from(fromMetaModelURI: String) = {
    super.from(fromMetaModelURI)
    this
  }

  /**
   * @inheritDoc
   */
  override def from(sourceTransformation: TransformationM2M) = {
    super.from(sourceTransformation)
    this
  }

  /**
   * @inheritDoc
   */
  override def in(loadFromFile: String) = {
    super.in(loadFromFile)
    this
  }

  /**
   * @inheritDoc
   */
  override def in(loadFromIterable: Iterable[_ <: EObject]) = {
    super.in(loadFromIterable)
    this
  }

  /**
   * Getter for the Model object this transformation transforms to
   * 
   * @return the target model
   */
  def getToModel() = {
    if (toModel == null)
      logger.warn("No target model to get yet");
    toModel
  }

  /**
   * Setter if the tracer should print the registered trace logs directly to the console
   *
   * @param mode the new mode for trace logging outputs
   */
  def setShowDirectTrace(mode: Boolean) = {
    tracer.showDirectTrace = mode
  }

  /**
   * Getter for the tracer object
   *
   * @return the tracer object of this transformation
   */
  def getTracer(): Tracer = { tracer }

  /**
   * getter of the rules in a certain phase
   * 
   * @return all rules of the given phase
   */
  def getRules(phase: Int) = this.phases.get(phase)

  /**
   * getter of the rules in the current active phase
   * 
   * @return all rules of the current phase 
   */
  def getRules() = this.phases.get(this.activePhase)

  /**
   * Getter for the set of newly created elements
   * 
   * @return A list of all already created elements
   */
  def getCreatedElements() = { createdElements }

  /**
   * Getter of the transformation helper object
   * 
   * @return The transformation helper object for this transformation
   */
  def getTransformationHelper() = {
    if (transformationHelper == null)
      transformationHelper = new TransformationHelper(this);
    transformationHelper
  }

  /**
   * Sets this transformation in refining mode (a transformation that only changes the source model)
   *
   * @param fromMetaModelURI the source and target meta model uri
   * 
   * @return The transformation object (fluent interface)
   */
  def refine(fromMetaModelURI: String) = {
    if (this.toModel != null)
      logger.warn("A target meta model is set and the refining mode is selected. Overriding the target meta model now... ")

    this.from(fromMetaModelURI)
    this.refining = true

    this
  }

  /**
   * Sets this transformation in refining mode (a transformation that only changes the source model)
   *
   * @param fromMetaModelURI  the source and target meta model from another transformation
   * 
   * @return The transformation object (fluent interface)
   */
  def refine(sourceTransformation: TransformationM2M) = {
    if (this.toModel != null)
      logger.warn("A target meta model is set and the refining mode is selected. Overriding the target meta model now... ")

    this.from(sourceTransformation)
    this.refining = true

    this
  }

  /**
   * Sets the target meta model
   *
   * @param toMetaModelURI The target meta model uri
   * 
   * @return The transformation object (fluent interface)
   */
  def to(toMetaModelURI: String) = {
    // get target metamodel and create an empty model for it
    val toMetaModel: EMFReferenceModel = mf.newReferenceModel().asInstanceOf[EMFReferenceModel]
    injector.inject(toMetaModel, toMetaModelURI)
    toModel = mf.newModel(toMetaModel).asInstanceOf[EMFModel]
    this
  }

  /**
   * Adds an array of rules to the transformation
   *
   * @param newRules One or more IRule objects for this transformation
   * 
   * @return The transformation object (fluent interface)
   */
  def addRule(newRules: IRule*) = {
    for (rule <- newRules) {
      rule.setToModel(toModel)
      rule.setTransformation(this)

      if (this.phases.size() <= currentPhaseToAddID)
        this.phases.add(currentPhaseToAddID, new HashSet[IRule])

      this.phases.get(currentPhaseToAddID) += rule
    }

    this
  }

  /**
   * Sets the next phaseID. Adding rules after calling this method will result in a call of the rules in a seperate phase
   * 
   * @return The transformation object (fluent interface)
   */
  def nextPhase() = {
    this.currentPhaseToAddID += 1
    this
  }

  /**
   * saves the result to a file with the given path
   *
   * @param filePath a path to the file the result of the transformation should be written
   * 
   * @return The transformation object (fluent interface)
   */
  def export(filePath: String) = {
    val saveRes = this.toModel.getResource().getResourceSet().createResource(URI.createURI(filePath))
    saveRes.getContents().addAll(0, toModel.getResource().getContents())
    saveRes.save(Collections.EMPTY_MAP)
    
    this
  }

  /**
   * Executes this transformation using the given rules
   * 
   * @return The transformation object (fluent interface)
   */
  def transform(): TransformationM2M = {

    // if the input for this transformation is another transformation, read the output now
    if (fromModelFromTransformation != null) {
      fromModel = fromModelFromTransformation.getToModel()
    }

    // do all phases
    for (i <- 0 to this.phases.size() - 1) {
      this.activePhase = i

      logger.debug("Now starting phase " + this.activePhase)

      if (!refining) {
        // for all contents in the resource
        for (resContent <- fromModel.getResource().getContents()) {
          executeOnEveryElement(resContent, activePhase)
        }
      } else {
        // copy all content from the incoming model to the outgoing
        val toMetaModel: EMFReferenceModel = mf.newReferenceModel().asInstanceOf[EMFReferenceModel]
        injector.inject(toMetaModel, fromModel.getReferenceModel().getResource().getURI().toString())
        toModel = mf.newModel(toMetaModel).asInstanceOf[EMFModel]
        injector.inject(toModel, mf.getResourceSet().createResource(URI.createURI("new-model")))

        toModel.getResource().getContents().addAll(0, fromModel.getResource().getContents())

        val elements: Array[EObject] = toModel.getResource().getContents().toArray().asInstanceOf[Array[EObject]]

        for (resContent <- elements) {
          executeOnEveryElement(resContent, this.activePhase)
        }
      }
    }
    this
  }

  /**
   * Convinience method to pass the incoming model at transformation execution time
   * 
   * @param str
   * 	The path to the inupt model
   * 
   * @return The transformation object (fluent interface)
   */
  def transform(str: String): TransformationM2M = {
    in(str)
    transform()
  }

  /**
   * Convinience method to pass the incoming model at transformation execution time
   * 
   * @param loadFromIterable
   * 	A list of instantiated source model elements
   * 
   * @return The transformation object (fluent interface)
   */
  def transform(loadFromIterable: Iterable[_ <: EObject]): TransformationM2M = {
    in(loadFromIterable)
    transform()
  }

  /**
   * recursively traverses the EObject and tries to execute the rules on each element
   *
   * @param source 
   * 	an element this transformation should be performed on. additionally this method searches all child-elements and calls the belonging methods on them
   *
   * @param phase 
   * 	the current phase that should be executed for this element
   */
  private def executeOnEveryElement(source: EObject, phase: Int): Unit = {
    // execute all rules for every content element that is contained
    executeRule(source, phase)

    for (element <- source.eContents()) {
      // digg deeper in the hirarchy to really find all elements
      executeOnEveryElement(element, phase)
    }
  }

  /**
   * Execute all rules on the given object in the given phase
   */
  private def executeRule(element: EObject, phase: Int): Unit = {
    try {

      if (this.phases.get(phase).size == 0)
        logger.warn("This transformation phase contains no rules: " + phase)

      // for every rule          
      for (r <- this.phases.get(phase)) {
        // only execute none lazy rules                
        // is the element of the rules input type?               
        if (!r.getIsLazyRule() &&
          r.getFromManifest().erasure.isAssignableFrom(element.getClass())) {

          try {
            val castedSourceElement = element.asInstanceOf[r.TA]
            // does the element match the user method check for this rule?
            if (r.isApplicable(castedSourceElement)) {
              if (!refining) {
                if (r.isToMany()) {
                  // execute the rule on it with the one to many execution
                  val newElements = r.execToMany(castedSourceElement)

                  for (newEl <- newElements) {
                    // trace the newly created element -> changed that it is done by the helper
                    // tracer.trace(castedSourceElement, newEl, r, ETraceConnectionType.Created)

                    // add the new element to the list
                    // this.createdElements += newEl
                  }
                } else {
                  // execute the rule on it and let the rule create a new element
                  val newElement = r.exec(castedSourceElement)

                  // trace the newly created element -> changed that it is done by the helper
                  // tracer.trace(castedSourceElement, newElement, r, ETraceConnectionType.Created)

                  // add the new element to the list
                  //this.createdElements += newElement
                }

              } else {
                // call the execute method of the rule which does not create a new element
                r.exec(castedSourceElement, castedSourceElement.asInstanceOf[r.TB])

                // trace the changed created element
                tracer.trace(castedSourceElement, castedSourceElement, r, ETraceConnectionType.Changed)
              }
            }
          } catch {
            case s: Exception => s.printStackTrace()
          }
        }
      }
    }
  }
}
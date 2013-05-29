package de.digitalforger.smtl.core
import de.digitalforger.smtl.core.model.emf.EMFModel
import de.digitalforger.smtl.core.model.emf.EMFModelFactory
import de.digitalforger.smtl.core.model.emf.EMFModelInjector
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import de.digitalforger.smtl.logging.LogHelper
import org.eclipse.emf.ecore.EObject
import de.digitalforger.smtl.core.model.emf.EMFReferenceModel
import de.digitalforger.smtl.m2m.TransformationM2M

/**
 * The Transformation trait is the base trait for transformations using SMTL.
 *
 * @author Lars George
 */
trait ITransformation extends LogHelper {
  
  protected var fromModelFromTransformation : TransformationM2M = null
  protected var fromModel: EMFModel = null

  // need util object
  protected val mf = new EMFModelFactory()
  protected val injector = new EMFModelInjector()
  protected val metaMetaModel = mf.getMetametamodel()
  
  // make xmi files readable
  protected val reg = Resource.Factory.Registry.INSTANCE
  reg.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl())  
  
  /**
   * Getter for the Model object this transformation transforms from
   */
  def getFromModel() = {
    if (fromModel == null)
      logger.warn("No source model to get yet");
    fromModel
  }
  
   /**
   * Sets the source meta model
   *
   * @param fromMetaModelURI
   * 	the uri of the source meta model
   * 
   * @return The transformation object (fluent interface)
   */
  def from(fromMetaModelURI: String) = {
    // get the source metamodel and load the model from an xmi file            
    val fromMetaModel: EMFReferenceModel = mf.newReferenceModel().asInstanceOf[EMFReferenceModel]
    injector.inject(fromMetaModel, fromMetaModelURI)
    fromModel = mf.newModel(fromMetaModel).asInstanceOf[EMFModel]

    this
  }
  
  /**
   * Sets the source meta model using a resource
   * 
   * @param resource
   * 	a resource to the metamodel
   * 
   * @return The transformation object (fluent interface)
   */
  def from(resource : Resource) = {
    val fromMetaModel: EMFReferenceModel = mf.newReferenceModel().asInstanceOf[EMFReferenceModel]
    injector.inject(fromMetaModel, resource)
    fromModel = mf.newModel(fromMetaModel).asInstanceOf[EMFModel]
    this
  }
  
  /**
   * Sets the source metamodel and model for this transformation
   * 
   * @param sourceTransformation	
   * 	A transformation that should serve as the source for this transformation
   * 
   * @return The transformation object (fluent interface)
   */
  def from(sourceTransformation : TransformationM2M) = {
    fromModelFromTransformation = sourceTransformation    
    this
  }
  
    /**
   * loading data from a file
   *
   * @param loadFromFile
   * 	the path to a file that contains data conforming to the source model of this transformation
   * 
   * @return The transformation object (fluent interface)
   */
  def in(loadFromFile: String) = {

    if (fromModel == null)
      logger.error("method 'in' called with no source model (transform(String) with a valid model uri needs to be called)")

    injector.inject(fromModel, loadFromFile)

    this
  }

  /**
   * loading data from an array
   *
   * @param loadFromIterable
   * 	 an iterable of data that should be transformed by this transformation. it must conform to the meta model of the source model
   * 
   * @return The transformation object (fluent interface)
   */
  def in(loadFromIterable: Iterable[_ <: EObject]) = {
    if (fromModel == null)
      logger.error("method 'in' called with no source model (transform(String) with a valid model uri needs to be called)")
    
      injector.inject(fromModel, loadFromIterable)
    this
  }
}
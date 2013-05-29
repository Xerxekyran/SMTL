package de.digitalforger.smtl.core.model.emf

import scala.collection.JavaConversions.asJavaCollection
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import de.digitalforger.smtl.core.model.IModel
import de.digitalforger.smtl.core.model.IModelInjector
import de.digitalforger.smtl.core.model.IReferenceModel
import org.eclipse.emf.ecore.EObject
import scala.collection.mutable.Buffer

/**
 * The EMF implementation of a model injector
 * Based on the ATL implementation
 * 
 * @author Lars George
 */
class EMFModelInjector extends IModelInjector {

  /**
   * @inheritDoc
   */
  override def inject(targetModel: IModel, source: String): Unit = {

    val resSet = targetModel.getModelFactory().asInstanceOf[EMFModelFactory].getResourceSet()

    if (source != null) {
      try {
        var mainResource = resSet.getResource(URI.createURI(source), true)

        inject(targetModel, mainResource)

      } catch {
        case e: Throwable => throw new Exception("EMFModelInjector: Loading error: " + source + " [" + e.getMessage + "]")
      }
    } else {
      throw new Exception("EMFModelInjector: No resource: " + source)
    }
  }
  
  /**
   * an inject method for emf models given in an array instead of a path to a file
   * 
   * @param targetModel
   * 	the model the data should be injected to
   * 
   * @param source
   * 	the data that should be injected
   */
  def inject(targetModel : IModel, source : Iterable[_ <: EObject]) : Unit = {
    
    val resSet = targetModel.getModelFactory().asInstanceOf[EMFModelFactory].getResourceSet()
    
    try {
        var mainResource = resSet.createResource(URI.createURI("new-model"))
        mainResource.getContents().addAll(source)

        inject(targetModel, mainResource)

      } catch {
        case e: Throwable => throw new Exception("EMFModelInjector: Loading error: " + source + " [" + e.getMessage + "]")
      }
  }

  /**
   * Injects the given resource into the given model
   *
   * @param targetModel
   * 	the model where to inject
   *
   * @param resource
   * 	the resource that should be injected
   */
  def inject(targetModel: IModel, resource: Resource): Unit = {
    targetModel.asInstanceOf[EMFModel].setResource(resource)

    if (targetModel.isInstanceOf[IReferenceModel]) {
      targetModel.asInstanceOf[EMFReferenceModel].register()
    }
  }

}
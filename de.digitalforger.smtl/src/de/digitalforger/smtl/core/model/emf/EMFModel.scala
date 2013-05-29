package de.digitalforger.smtl.core.model.emf

import scala.collection.JavaConversions.asScalaIterator
import scala.collection.mutable.HashSet
import scala.collection.mutable.HashMap

import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject

import de.digitalforger.smtl.core.model.IModel
import de.digitalforger.smtl.core.model.IReferenceModel

/**
 * The EMF implementation of an IModel
 * Based on the ATL implementation
 * 
 * @author Lars George
 */
class EMFModel(refModel: EMFReferenceModel, modelFactory: EMFModelFactory) extends IModel {

  private var resource: Resource = null
  private val elementsByType = new HashMap[EClass, Set[EObject]]()

  private var referenceModel = refModel

  /**
   * Getter for the resource
   */
  def getResource(): Resource = resource

  /**
   * Setter for the resource
   *
   * @param res
   * 	 	the resource to set
   *
   */
  def setResource(res: Resource): Unit = { resource = res }

  /**
   * @inheritDoc
   */
  override def getReferenceModel(): EMFReferenceModel = referenceModel

  /**
   * @inheritDoc
   */
  override def setReferenceModel(refModel: IReferenceModel): Unit = { referenceModel = referenceModel }

  /**
   * @inheritDoc
   */
  override def getModelFactory(): EMFModelFactory = modelFactory

  /**
   * registers a new element to the model (without creating a new one)
   *
   * @param newObject 
   * 	the object that should be registered to this model
   * 
   * @return true if registering the new object was successfull, else false
   */
  def registerNewElement(newObject: EObject) = {
    var mainResource = getResource()
    if (mainResource == null) {
      mainResource = modelFactory.getResourceSet().createResource(URI.createURI("new-model"))
      setResource(mainResource)
    }
    
    mainResource.getContents().add(newObject)
  }

  /**
   * @inheritDoc
   */
  override def newElement(metaElement: AnyRef): AnyRef = {
    var mainResource = getResource()
    if (mainResource == null) {
      mainResource = modelFactory.getResourceSet().createResource(URI.createURI("new-model"))
      setResource(mainResource)
    }

    var ec = metaElement.asInstanceOf[EClass]
    var ret: EObject = null

    ret = ec.getEPackage().getEFactoryInstance().create(ec)
    mainResource.getContents().add(ret)

    ret
  }

  /**
   * @inheritDoc
   */
  override def getElementsByType(metaElement: AnyRef): Set[EObject] = {
    val eClass = metaElement.asInstanceOf[EClass]

    elementsByType.get(eClass) match {
      case Some(s) => s
      case None => {
        var retVal = new HashSet[EObject]()
        val res = getResource()
        if (res != null) {
          for (it <- res.getAllContents()) {
            if (eClass.isInstance(it)) {
              retVal.add(it)
            }
          }
        }
        val ret = retVal.toSet
        elementsByType.put(eClass, ret)

        ret
      }
    }
  }
}
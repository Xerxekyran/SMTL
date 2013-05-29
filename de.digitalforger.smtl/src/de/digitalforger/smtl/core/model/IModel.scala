package de.digitalforger.smtl.core.model


/**
 * An IModel defines any type of model that can be handled
 * Based on the ATL implementation
 * 
 *  @author Lars George
 */
trait IModel {

  /**
   * Creates a new element in the model
   *
   * @param metaElement
   * 	The metatype of the new element that should be created
   * 
   * @return  the created element
   */
  def newElement(metaElement: AnyRef): AnyRef

  /**
   * Getter for the reference model of this model
   *
   * @return the reference model
   */
  def getReferenceModel(): IReferenceModel

  /**
   * setter of the reference model
   *
   * @param refModel the reference model of this model (eg metamodel)
   */
  def setReferenceModel(refModel: IReferenceModel): Unit

  /**
   * Getter for the model factory that is used in this model
   *
   * @return the model factory of this model
   */
  def getModelFactory(): IModelFactory

  /**
   * Getter for all elements of a given meta type
   *
   * @param metaElement
   * 	the metatype
   *
   * @return a set of elements that match the given meta type
   */
  def getElementsByType(metaElement: AnyRef): Set[_]
}
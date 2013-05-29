package de.digitalforger.smtl.core.model

/**
 * An IReferenceModel is a reference model, e.g. a metamodel
 * Based on the ATL implementation
 * 
 * @author Lars George
 */
trait IReferenceModel extends IModel {
  /**
   * Returns the metaelement with the given name
   *
   * @param name
   * 	the name of the element that should be returned
   * 
   * @return the meta element with the given name
   *
   */
  def getMetaElementByName(name: String): AnyRef
}
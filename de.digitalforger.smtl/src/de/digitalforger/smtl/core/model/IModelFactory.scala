package de.digitalforger.smtl.core.model

/**
 * The interface for a model factory
 * Based on the ATL implementation
 * 
 * @author Lars George
 */
trait IModelFactory {
	
  /**
   * Create a new Model
   * 
   * @param referenceModel
   * 	the reference model for the new model
   * 
   * @return The newly created model
   */
  def newModel(referenceModel: IReferenceModel): IModel
  
  /**
   * Getter for the default metametamodel
   * 
   * @return The metametamodel
   */
  def getMetametamodel() : IReferenceModel
  
  /**
   * Create a new reference model
   * 
   * @return The newly created reference model
   */
  def newReferenceModel() : IReferenceModel
}
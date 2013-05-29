package de.digitalforger.smtl.core.model

/**
 * A Modelinjector injects a model into the several types of models.
 * Based on the ATL implementation
 * 
 * @author Lars George
 *  
 */
trait IModelInjector {
  /**
	 * Injects data into an IModel using default options.
	 * 
	 * @param targetModel
	 *            the IModel where to inject
	 * @param source
	 *           the source indication to load the sourceModel
	 */
	def inject(targetModel : IModel , source : String ) : Unit
}
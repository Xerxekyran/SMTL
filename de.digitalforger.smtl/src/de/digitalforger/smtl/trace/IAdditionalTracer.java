package de.digitalforger.smtl.trace;

import de.digitalforger.smtl.m2m.IRule;

/**
 * Interface for additional trace classes
 * 
 * @author Lars George
 * 
 */
public interface IAdditionalTracer {

	/**
	 * This method is called by smtl, if something can be traced
	 * 
	 * @param from
	 *            the source model element of the current transformation
	 * @param to
	 *            the target model element of the current transformation
	 * @param byRule
	 *            the rule that waws used to transform the model element
	 */
	public void trace(Object from, Object to, IRule byRule);
}

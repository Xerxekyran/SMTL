package de.digitalforger.smtl.m2m

import org.eclipse.emf.ecore.EObject
import de.digitalforger.smtl.core.IGeneratedCC
import scala.collection.mutable.LinkedList

/**
 * A rule defines if and how to transform objects of certain types.
 *
 * @author Lars George
 */
class Rule[A <: EObject: Manifest, B <: EObject: Manifest](name: String = "") extends IRule with Function1[A, B]{
  type TA = A
  type TB = B

  manifest_TA = manifest[TA]
  manifest_TB = manifest[TB]

  /**
   * The apply method is called if this rule is used as a function
   * 
   * @param a
   * 	the source model element
   * 
   * @return the transformed version of the source element (of the target model element type)
   */
  def apply(a : A) : B = {
    this.exec(a, true)
  }
  
  /**
   * Sets the user method of the transformation process.
   *
   * @param fnc
   * 	the method that is called if this rule should transform an input object
   *
   * @return The rule object (fluent interface)
   */
  def perform(fnc: (A, B) => Unit) = {
    this.execFunc = fnc
    this
  }

  /**
   * Sets the user method of the transformation process forcing the user to create the target model element himself
   *
   * @param fnc
   * 	the method that is called if this rule should transform an input object
   *
   * @return The rule object (fluent interface)
   */
  def perform(fnc: (A) => B) = {
    this.execFunc2 = fnc
    this
  }

  /**
   * Sets the user method of the transformation process when transforming from one element to a list of elements
   * 
   * @param fnc
   * 	the method that is called if this rule should transform an input object
   * 
   * @return The rule object (fluent interface)
   */
  def performToMany(fnc: (TransformationHelper, A, java.util.LinkedList[B]) => Unit) = {
    this.execFuncToMany = fnc
    this
  }

  /**
   * method to do an implicit from an model object to the case class for that
   */
  def use[ACC <: IGeneratedCC](implicit conv: A => ACC) /*: (ACC => B) => Rule[A,B]*/ = new MyMatchFunc[ACC](usingCC(conv) _)

  /**
   * internal class to disguise the appl method call
   */
  class MyMatchFunc[ACC](fnc: (ACC => B) => Rule[A, B]) {
    def matching(fn: ACC => B) = fnc.apply(fn)
  }

  /**
   * internal method to use for the case class execution function
   * 
   * @return The rule object (fluent interface)
   */
  def usingCC[ACC <: IGeneratedCC](conv: A => ACC)(fnc: ACC => B) = {
    def fn(a: A): B = fnc(conv(a))
    this.execFuncCC = fn
    this
  }

  /**
   * Sets the user evaluation method.
   *
   * @param eval
   * 	the method that is called to check if this rule is applicable to a certain input object
   *
   * @return The rule object (fluent interface)
   */
  def when(eval: (A) => Boolean) = {
    this.evalFunc = eval
    this
  }

  /**
   * Setter that this rule should be lazily executed (only if called by a helper)
   * 
   * @return The rule object (fluent interface)
   */
  def isLazy() = {
    this.isLazyRule = true
    this
  }

  /**
   * Setter that this rule is not unique, meaning that it will return new elements for the same incoming source objects
   * 
   * @return The rule object (fluent interface)
   */
  def isNotUnique() = {
    this.isUniqueRule = false
    this
  }

  /**
   * The toString method of a rule
   * 
   * @return A String represenation of this rule
   */
  override def toString() = {
    "Rule: " + this.name
  }
}
package de.digitalforger.smtl.m2m

import scala.collection.JavaConversions._
import org.eclipse.emf.ecore.EObject
import de.digitalforger.smtl.core.model.emf.EMFModel
import de.digitalforger.smtl.logging.LogHelper
import scala.collection.mutable.LinkedList

/**
 * A rule stands for a specific transformation from type TA to TB.
 * The user can specify when this rule is applicable and what should be done with the target elements.
 *
 * @author Lars George
 */
trait IRule extends LogHelper {
  type TA <: EObject
  type TB <: EObject

  protected var evalFunc: (TA) => Boolean = ((n) => { true })
  protected var execFunc: (TA, TB) => Unit = null
  protected var execFunc2: (TA) => TB = null
  protected var execFuncCC: (TA) => TB = null
  
  protected var execFuncToMany: (TransformationHelper, TA, java.util.LinkedList[TB]) => Unit = null

  protected var toModel: EMFModel = null

  protected var manifest_TA: Manifest[TA] = null
  protected var manifest_TB: Manifest[TB] = null
  protected var transformation: TransformationM2M = null

  protected var isLazyRule: Boolean = false
  protected var isUniqueRule: Boolean = true

  /**
   * Getter if this rule is a one to many
   */
  def isToMany(): Boolean = execFuncToMany != null

  /**
   * Execution method for one to many transformations
   * 
   * @param a
   * 	the source model element
   * 
   * @return a list of the target model elements
   */
  def execToMany(a: TA): java.util.LinkedList[TB] = {
    val ret = new java.util.LinkedList[TB]()
    this.execFuncToMany(this.transformation.getTransformationHelper(), a, ret)
    ret
  }

  /**
   * Getter of the isLazyRule attribute
   * 
   * @return true if this rule is set to "Lazy"
   */
  def getIsLazyRule(): Boolean = { isLazyRule }

  /**
   * Getter of the isUniqueRule attribute
   * 
   * @return true if this rule is a "Unique" rule
   */
  def getIsUniqueRule(): Boolean = { isUniqueRule }

  /**
   * setter of the transformation this rule belongs to
   *
   * @param t
   * 	the transformation this rule should belong to
   */
  def setTransformation(t: TransformationM2M): Unit = {
    if (this.transformation != null && this.transformation != t) {
      logger.warn("This rule is already part of another transformation")
      // throw new Exception("This rule is already part of another transformation")
    } else
      this.transformation = t
  }

  /**
   * setter for the toModel
   * 
   * @param model
   * 	set the target model
   */
  def setToModel(model: EMFModel) = {
    if (toModel != null) {
      logger.warn("This rule already got a model set to")
    }
    this.toModel = model
  }

  /**
   * getter for the manifest of the from-model
   *
   * @return the manifest object of the TA type of this rule (source type)
   */
  def getFromManifest() = manifest_TA

  /**
   * getter for the manifest of the to-model
   *
   * @return the manifest object of the TB type of this rule (source type)
   */
  def getToManifest() = manifest_TB

  /**
   * the users evaluation method is called and the return value is passed as return value
   *
   * @param in
   * 	the element this rule should be performed on
   *
   * @return true if this rule is applicable for the input element, otherwise false
   */
  def isApplicable(in: TA): Boolean = {
    evalFunc(in)
  }

  /**
   * Executes this rule. A new Element of type TB is created, given to the users execution method or created by the user and finally returned
   *
   * @param a
   * 	the source object that should be transformed by this rule
   *
   * @param skipUserActionsOnExistingObject
   * 	If set to true, no user actions will be executed on existing model elements. Default value is false
   * 
   * @return a newly created element of type TB
   */
  def exec(a: TA, skipUserActionsOnExistingObject : Boolean = false): TB = {

    this.transformation.getTransformationHelper().setCurrentExecutedRule(this)
    this.transformation.getTransformationHelper().setCurrentSourceObject(a)
    
    // check that this rule was assigned to a transformation
    if(this.transformation == null) {
      logger.error("Rule execution of rule [" + this.toString() + "] fails. It was not assigned to a transformation.")
      return null.asInstanceOf[TB]
    }
    
    val clsName = manifest_TB.erasure.getName()
    val shortName = (clsName.substring(clsName.lastIndexOf(".") + 1))

    // check which version of the exec func was given (user creates element or the transformation itself)    
    if (this.execFunc != null) {

      // if we are a unique rule
      if (isUniqueRule) {

        // before creating a new one, check if we already did this transformation
        val lst = this.transformation.getTracer().getElementsCreatedBy(a, this);

        for (tpl <- lst) {
          if (getToManifest().erasure.isAssignableFrom(tpl._1.getClass())) {

            // perform the user actions if the skip flag is not set
            if(!skipUserActionsOnExistingObject)
            	execFunc(a, tpl._1.asInstanceOf[TB])

            return tpl._1.asInstanceOf[TB]
          }
        }
      }

      // create a new object of the target model      
      var newObject: TB = this.transformation.getTransformationHelper().createNewTargetElement(shortName).asInstanceOf[TB]

      // perform the user actions
      execFunc(a, newObject)

      return newObject
    } else if (this.execFunc2 != null) {
      // let the user create the new element
      var newObject: TB = execFunc2(a)

      // register the new element in the target model
      // toModel.registerNewElement(newObject)

      return newObject
    } else if (this.execFuncCC != null) {
      // let the user create the new element
      var newObject: TB = execFuncCC(a)

      // register the new element in the target model
      // toModel.registerNewElement(newObject)

      return newObject
    } else {
      logger.error("This rule has no or multiple execution function! [" + this + "]")
      null.asInstanceOf[TB]
    }
  }

  /**
   * Executes this rule without creating a new element. Instead the target element is passed as the 2nd argument
   *
   * @param a
   * 	the source object of the transformation
   * @param b
   * 	the target object of the transformation
   *
   * @return the target object that may be changed by the users execution method
   */
  def exec(a: TA, b: TB): TB = {
    if (execFunc == null)
      logger.error("The rule has no execution part")
    else
      execFunc( a, b)
    b
  }

  /**
   * getter of the created elements
   *
   * @return a list of objects that are already created by the transformation this rule belongs to
   */
  def getCreatedElements() = this.transformation.getCreatedElements()

}
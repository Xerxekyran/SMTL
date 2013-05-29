package de.digitalforger.smtl.core
import org.eclipse.emf.ecore.EObject

/**
 * Base-Trait for all generated case classes.
 * This Trait ist used to trigger implicit conversions for model elements to CaseClasses.
 *
 * @author Lars George
 */
trait IGeneratedCC {
  override def toString(): String = "Generated Case Class"
  override def hashCode(): Int = { this.getClass().hashCode() }
}

object IGeneratedFunctions {

  /**
   * implicit for lists of domain objects to a list of CaseClasses
   */
  implicit def domainListToCCList[D <: EObject, CC <: IGeneratedCC](lst: org.eclipse.emf.common.util.EList[D])(implicit d2cc: D => CC): List[CC] = {
    var ret = List[CC]()
    for (i <- 0 to lst.size - 1) {
      val elTyped: CC = lst.get(i)
      ret = elTyped :: ret
    }
    ret
  }

  /**
   * Implicit for lists of CaseClasses to a list of domain objects
   */
  implicit def ccListToDomainList[D <: EObject, CC <: IGeneratedCC](lst: List[CC])(implicit cc2d: CC => D): java.util.LinkedList[D] = {
    var ret = new java.util.LinkedList[D]()
    for (i <- 0 to lst.size - 1) {
      val elTyped: D = lst(i)
      ret.add(elTyped)
    }
    ret
  }
}
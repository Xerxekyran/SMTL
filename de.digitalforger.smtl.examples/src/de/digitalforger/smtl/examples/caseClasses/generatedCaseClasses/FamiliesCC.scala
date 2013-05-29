package de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses

import java.lang.String
import scala.collection.mutable.HashMap
import org.eclipse.emf.ecore.EObject
import de.digitalforger.examples.model.families.Families.impl.FamiliesFactoryImpl
import de.digitalforger.examples.model.families.Families.Family
import de.digitalforger.examples.model.families.Families.Member
import de.digitalforger.smtl.core._
import de.digitalforger.smtl.core.IGeneratedFunctions._
import de.digitalforger.smtl.m2m.TransformationHelperSyntax._
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

case class FamilyCC(lastName: String, var father: MemberCC, var mother: MemberCC, var sons: List[MemberCC], var daughters: List[MemberCC]) extends IGeneratedCC
case class MemberCC(firstName: String, var familyFather: FamilyCC, var familyMother: FamilyCC, var familySon: FamilyCC, var familyDaughter: FamilyCC) extends IGeneratedCC

object FamiliesCC {

  private type DomainRootType = Family
  private var domainTraces = HashMap[EObject, IGeneratedCC]()
  private var domainRootElements = ListBuffer[EObject]()
  private var domainReferencesToCC = ListBuffer[LazyRefToCC[_]]()

  private type CCRootType = FamilyCC
  private var ccTraces = HashMap[IGeneratedCC, EObject]()
  private var ccRootElements = ListBuffer[IGeneratedCC]()
  private var ccReferencesToDomain = ListBuffer[LazyRefToDomain[_]]()

  def let(obj: IGeneratedCC) = obj

  /**
   *
   */
  def getRoot[T <: EObject](el: T): DomainRootType = {
    if (el.eContainer == null) {
      el.asInstanceOf[DomainRootType]
    } else {
      getRoot(el.eContainer)
    }
  }

  def getCCRoot[T <: IGeneratedCC](el: T): CCRootType = {
    var ret: CCRootType = null
    for (dt <- domainTraces) {
      if (dt._2 == el) {
        var domainRoot = getRoot(dt._1)
        ret = domainTraces.get(domainRoot).get.asInstanceOf[CCRootType]
      }
    }
    ret
  }

  def lazyrefToCC[T <: IGeneratedCC](f: T => Unit, t: EObject): T = {
    domainReferencesToCC += LazyRefToCC(f, t)
    null.asInstanceOf[T]
  }

  case class LazyRefToCC[T <: IGeneratedCC](f: T => Unit, t: EObject) {
    def resolve() {
      if (t != null) {
        val o = domainTraces.get(t).get.asInstanceOf[T]
        f(o)
      }
    }
  }

  def lazyrefToDomain[T <: EObject](f: T => Unit, t: IGeneratedCC): T = {
    ccReferencesToDomain += LazyRefToDomain(f, t)
    null.asInstanceOf[T]
  }

  case class LazyRefToDomain[T <: EObject](f: T => Unit, t: IGeneratedCC) {
    def resolve() {
      if (t != null) {
        val o = ccTraces.get(t).get.asInstanceOf[T]
        f(o)
      }
    }
  }

  implicit def Family2FamilyCC(obj: Family): FamilyCC = {

    // traces already build?
    if (domainTraces.get(obj) != None) {
      domainTraces.get(obj).get.asInstanceOf[FamilyCC]
    } else {
      // traces not yet build and not triggered
      var myRoot = getRoot(obj)
      if (!domainRootElements.contains(myRoot)) {
        // set the root, so we know that the build process in triggered
        domainRootElements.prepend(myRoot)

        // do implicit magic
        let(myRoot)

        // resolve references
        for (r <- domainReferencesToCC) r.resolve()

        // now we should be in the traces, so return the CC for us
        domainTraces.get(obj).get.asInstanceOf[FamilyCC]
      } else {
        var me: FamilyCC = null

        // traces not yet build but build process already triggered
        me = FamilyCC(if (obj != null) obj.getLastName() else null,
          if (obj != null) obj.getFather() else null,
          if (obj != null) obj.getMother() else null,
          if (obj != null) domainListToCCList(obj.getSons()) else null,
          if (obj != null) domainListToCCList(obj.getDaughters()) else null)

        domainTraces += obj -> me
        me
      }
    }
  }

  implicit def FamilyCC2Family(obj: FamilyCC): Family = {
    // traces already build?
    if (ccTraces.get(obj) != None) {
      ccTraces.get(obj).get.asInstanceOf[Family]
    } else {
      // traces not yet build and not triggered
      var myRoot = getCCRoot(obj)
      if (ccRootElements.contains(myRoot)) {
        ccRootElements.prepend(myRoot)

        val implicitMagic: Family = myRoot

        // resolve references
        for (r <- ccReferencesToDomain) r.resolve()

        ccTraces.get(obj).get.asInstanceOf[Family]
      } else {
        if (obj != null) {
          val ret: Family = FamiliesFactoryImpl.init().createFamily()
          ret.getDaughters().addAll(0, ccListToDomainList(obj.daughters))
          ret.getSons().addAll(0, ccListToDomainList(obj.sons))
          ret.setMother(obj.mother)
          ret.setFather(obj.father)
          ret.setLastName(obj.lastName)
          register(ret)
          ret
        } else {
          null.asInstanceOf[Family]
        }
      }
    }
  }

  implicit def Member2MemberCC(obj: Member): MemberCC = {
    // traces already build?
    if (domainTraces.get(obj) != None) {
      domainTraces.get(obj).get.asInstanceOf[MemberCC]
    } else {
      // traces not yet build and not triggered
      var myRoot = getRoot(obj)
      if (!domainRootElements.contains(myRoot)) {
        // set the root, so we know that the build process in triggered
        domainRootElements.prepend(myRoot)

        // do implicit magic
        let(myRoot)

        // resolve references
        for (r <- domainReferencesToCC) r.resolve()

        // now we should be in the traces, so return the CC for us
        domainTraces.get(obj).get.asInstanceOf[MemberCC]
      } else {
        // traces not yet build but build process already triggered

        var me: MemberCC = null
        me = MemberCC(if (obj != null) obj.getFirstName() else null,
          lazyrefToCC[FamilyCC](me.familyFather = _, obj.getFamilyFather),
          lazyrefToCC[FamilyCC](me.familyMother = _, obj.getFamilyMother),
          lazyrefToCC[FamilyCC](me.familySon = _, obj.getFamilySon),
          lazyrefToCC[FamilyCC](me.familyDaughter = _, obj.getFamilyDaughter))

        domainTraces += obj -> me
        me
      }
    }
  }

  implicit def MemberCC2Member(obj: MemberCC): Member = {

    // traces already build?
    if (ccTraces.get(obj) != None) {
      ccTraces.get(obj).get.asInstanceOf[Member]
    } else {
      // traces not yet build and not triggered
      var myRoot = getCCRoot(obj)
      if (ccRootElements.contains(myRoot)) {
        ccRootElements.prepend(myRoot)

        val implicitMagic: Family = myRoot

        // resolve references
        for (r <- ccReferencesToDomain) r.resolve()

        ccTraces.get(obj).get.asInstanceOf[Member]
      } else {
        if (obj != null) {
          val ret: Member = FamiliesFactoryImpl.init().createMember()
          ret.setFirstName(obj.firstName)
          
          lazyrefToDomain[Family](ret.setFamilyDaughter(_), obj.familyDaughter)
          lazyrefToDomain[Family](ret.setFamilySon(_), obj.familySon)
          lazyrefToDomain[Family](ret.setFamilyMother(_), obj.familyMother)
          lazyrefToDomain[Family](ret.setFamilyFather(_), obj.familyFather)

          register(ret)
          ret
        } else {
          null.asInstanceOf[Member]
        }
      }
    }

  }
}

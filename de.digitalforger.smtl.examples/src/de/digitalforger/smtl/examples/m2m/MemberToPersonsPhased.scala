package de.digitalforger.smtl.examples.m2m

import java.lang.System

import de.digitalforger.examples.model.families.Families.FamiliesPackage
import de.digitalforger.examples.model.families.Families.Member
import de.digitalforger.examples.model.persons.Persons.Female
import de.digitalforger.examples.model.persons.Persons.Male
import de.digitalforger.examples.model.persons.Persons.PersonsPackage
import de.digitalforger.smtl.m2m.Rule
import de.digitalforger.smtl.m2m.TransformationM2M

/**
 * Example transformation for SMTL. 
 *
 * An SMTL version of the ATL example Families2Persons seperated into two phases
 *
 * @author Lars George
 */
object MemberToPersonsPhased {

  def getFamilyName(m: Member): String = {
    if (m.getFamilyFather() != null) m.getFamilyFather().getLastName()
    else if (m.getFamilyDaughter() != null) m.getFamilyDaughter().getLastName()
    else if (m.getFamilyMother() != null) m.getFamilyMother().getLastName()
    else if (m.getFamilySon() != null) m.getFamilySon().getLastName()
    else ""
  }

  def isMale(s: Member): Boolean = {
    !isFemale(s)
  }

  def isFemale(s: Member): Boolean = {
    if (s.getFamilyMother() != null || s.getFamilyDaughter() != null)
      true
    else
      false
  }

  def main(args: Array[String]): Unit = {

    // init the models
    FamiliesPackage.eINSTANCE.eClass()
    PersonsPackage.eINSTANCE.eClass()

    val inputFile = "file:../de.digitalforger.smtl.examples.model/resources/FamiliesToPersons/sample-Families.xmi"
    val familiesMetaModel = "http://www.digital-forger.de/examples/model/families"
    val personsMetaModel = "http://www.digital-forger.de/examples/model/persons"

    // create the transformation
    val transformation = new TransformationM2M("Families to Persons") from familiesMetaModel to personsMetaModel

    // first rule transforms member objects to male persons
    val rule1 = new Rule[Member, Male]("MemberToMale") when
      isMale perform
      ((mem, male) =>
        male.setFullName(mem.getFirstName() + " " + getFamilyName(mem)))

    // first rule transforms member objects to female persons
    val rule2 = new Rule[Member, Female]("MemberToFemale") when
      isFemale perform
      ((mem, fem) => 
        fem.setFullName(mem.getFirstName() + " " + getFamilyName(mem)))

    // add rules to phase 1    
    transformation.addRule(rule1)
    
    // next phase
    transformation.nextPhase()
    
    // add tules to phase 2
    transformation.addRule(rule2)

    // do the transformation work
    transformation.setShowDirectTrace(true)
    var startTime = System.nanoTime()
    transformation transform inputFile
    println("transformation took: " + (System.nanoTime() - startTime) * Math.pow(10, -9) + " seconds")

    transformation.export("../de.digitalforger.smtl.examples.model/resources/FamiliesToPersons/sampleOutputWithSMTL.xmi")
  }

}


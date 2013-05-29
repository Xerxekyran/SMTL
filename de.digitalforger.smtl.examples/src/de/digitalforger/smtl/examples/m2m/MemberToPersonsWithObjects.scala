package de.digitalforger.smtl.examples.m2m

import de.digitalforger.examples.model.families.Families.Member
import de.digitalforger.examples.model.families.Families.Family
import de.digitalforger.examples.model.persons.Persons.impl.PersonsFactoryImpl
import de.digitalforger.examples.model.families.Families.impl.FamiliesFactoryImpl
import de.digitalforger.examples.model.families.Families.FamiliesPackage
import de.digitalforger.smtl.m2m.Rule
import de.digitalforger.smtl.m2m.TransformationM2M
import de.digitalforger.examples.model.persons.Persons.PersonsPackage
import de.digitalforger.examples.model.persons.Persons.Male
import de.digitalforger.examples.model.persons.Persons.Female
import org.eclipse.emf.ecore.EObject

/**
 * An example transformation for SMTL
 *  
 * This example is the same as MemberToPersons but it does not use any persisted data like a xml file
 */
object MemberToPersonsWithObjects {

  def getFamilyName(m: Member): String = {
    var famName: String = ""

    if (m.getFamilyFather() != null) famName = m.getFamilyFather().getLastName()
    else if (m.getFamilyDaughter() != null) famName = m.getFamilyDaughter().getLastName()
    else if (m.getFamilyMother() != null) famName = m.getFamilyMother().getLastName()
    else if (m.getFamilySon() != null) famName = m.getFamilySon().getLastName()

    famName
  }

  /**
   * entry point of this example
   */
  def main(args: Array[String]): Unit = {

    // init the models
    FamiliesPackage.eINSTANCE.eClass()
    PersonsPackage.eINSTANCE.eClass()

    val familiesMetaModel = "http://www.digital-forger.de/examples/model/families"
    val personsMetaModel = "http://www.digital-forger.de/examples/model/persons"

    // create the transformation
    val transform1 = new TransformationM2M("Families to Persons") from familiesMetaModel to personsMetaModel

    // first rule transforms member objects to male persons
    val rule1 = new Rule[Member, Male]("MemberToMale") when
      ((n) => { n.getFamilyMother() == null && n.getFamilyDaughter() == null }) perform
      ((n, t) => {
        t.setFullName(n.getFirstName() + " " + getFamilyName(n))
      })

    // first rule transforms member objects to female persons
    val rule2 = new Rule[Member, Female]("MemberToFemale") when
      ((n) => { n.getFamilyFather() == null && n.getFamilySon() == null }) perform
      ((n, t) => {
        t.setFullName(n.getFirstName() + " " + getFamilyName(n))
      })

    // add the rules    
    transform1.addRule(rule1, rule2)

    // do the transformation work
    var startTime = System.nanoTime()
    transform1 transform createTestCase1()
    println("transformation took: " + (System.nanoTime() - startTime) * Math.pow(10, -9) + " seconds")

    transform1.export("../de.digitalforger.smtl.examples.model/resources/FamiliesToPersons/sampleOutput.xmi")
  }

  /**
   * creates the objects like in the following xml file:
   *
   * <Family lastName="March">
   * 	<father firstName="Jim"/>
   * 	<mother firstName="Cindy"/>
   * 	<sons firstName="Brandon"/>
   * 	<daughters firstName="Brenda"/>
   * </Family>
   * <Family lastName="Sailor">
   * 	<father firstName="Peter"/>
   * 	<mother firstName="Jackie"/>
   * 	<sons firstName="David"/>
   * 	<sons firstName="Dylan"/>
   * 	<daughters firstName="Kelly"/>
   * </Family>
   */
  def createTestCase1(): Iterable[Family] = {
    val famFactory = FamiliesFactoryImpl.init();
    val persFactory = PersonsFactoryImpl.init();

    // -----------------
    // fam 1
    var fam1 = famFactory.createFamily()
    fam1.setLastName("March")

    var father1 = famFactory.createMember()
    father1.setFirstName("Jim")
    fam1.setFather(father1)

    var mother1 = famFactory.createMember()
    mother1.setFirstName("Cindy")
    fam1.setMother(mother1)

    var son1 = famFactory.createMember()
    son1.setFirstName("Brandon")
    fam1.getSons().add(son1)

    var daughter1 = famFactory.createMember()
    daughter1.setFirstName("Brenda")
    fam1.getDaughters().add(daughter1)

    // -----------------
    // fam 2
    var fam2 = famFactory.createFamily()
    fam2.setLastName("Sailor")

    var father2 = famFactory.createMember()
    father2.setFirstName("Peter")
    fam2.setFather(father2)

    var mother2 = famFactory.createMember()
    mother2.setFirstName("Jacky")
    fam2.setMother(mother2)

    var son2 = famFactory.createMember()
    son2.setFirstName("David")
    fam2.getSons().add(son2)

    var son3 = famFactory.createMember()
    son3.setFirstName("Dilan")
    fam2.getSons().add(son3)

    var daughter2 = famFactory.createMember()
    daughter2.setFirstName("Kelly")
    fam2.getDaughters().add(daughter2)

    return Array(fam1, fam2)
  }
}
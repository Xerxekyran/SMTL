package de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses

import de.digitalforger.smtl.core._
import java.lang.String
import de.digitalforger.examples.model.persons.Persons.Person
import de.digitalforger.examples.model.persons.Persons.Male
import de.digitalforger.examples.model.persons.Persons.Female
import de.digitalforger.examples.model.persons.Persons.impl.PersonsFactoryImpl

case class ExtendedMaleCC (fullName : String, buddy : ExtendedMaleCC, girlFriend : ExtendedFemaleCC) extends IGeneratedCC
case class ExtendedFemaleCC (fullName : String, bestFriend : Person) extends IGeneratedCC

object ExtendedPersonsCC{
	def let(obj: IGeneratedCC) = obj

	implicit def ExtendedMale2ExtendedMaleCC(obj : Male) : ExtendedMaleCC = ExtendedMaleCC(if(obj != null) obj.getFullName() else null , null, null)
	implicit def ExtendedMaleCC2ExtendedMale(obj : ExtendedMaleCC): Male = { 
		val ret : Male = PersonsFactoryImpl.init().createMale()
		ret.setFullName(obj.fullName)
		ret
	}
	implicit def ExtendedFemale2ExtendedFemaleCC(obj : Female) : ExtendedFemaleCC = ExtendedFemaleCC(if(obj != null) obj.getFullName() else null , null)
	implicit def ExtendedFemaleCC2ExtendedFemale(obj : ExtendedFemaleCC): Female = { 
		val ret : Female = PersonsFactoryImpl.init().createFemale()
		ret.setFullName(obj.fullName)
		ret
	}
}

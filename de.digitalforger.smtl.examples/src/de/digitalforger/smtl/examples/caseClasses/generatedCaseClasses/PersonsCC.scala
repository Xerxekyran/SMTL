package de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses

import de.digitalforger.smtl.core._
import de.digitalforger.smtl.m2m.TransformationHelperSyntax._
import java.lang.String
import de.digitalforger.examples.model.persons.Persons.Person
import de.digitalforger.examples.model.persons.Persons.Male
import de.digitalforger.examples.model.persons.Persons.Female
import de.digitalforger.examples.model.persons.Persons.impl.PersonsFactoryImpl

case class MaleCC (fullName : String) extends IGeneratedCC
case class FemaleCC (fullName : String) extends IGeneratedCC

object PersonsCC{
	def let(obj: IGeneratedCC) = obj

	implicit def Male2MaleCC(obj : Male) : MaleCC = MaleCC(if(obj != null) obj.getFullName() else null )
	implicit def MaleCC2Male(obj : MaleCC): Male = { 
		val ret : Male = PersonsFactoryImpl.init().createMale()
		ret.setFullName(obj.fullName)
		register(ret)
		ret
	}
	implicit def Female2FemaleCC(obj : Female) : FemaleCC = FemaleCC(if(obj != null) obj.getFullName() else null )
	implicit def FemaleCC2Female(obj : FemaleCC): Female = { 
		val ret : Female = PersonsFactoryImpl.init().createFemale()
		ret.setFullName(obj.fullName)
		register(ret)
		ret
	}
}

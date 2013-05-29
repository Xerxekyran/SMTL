package de.digitalforger.smtl.examples.caseClasses

import de.digitalforger.smtl.util.ImplicitGenerator
import de.digitalforger.examples.model.persons.Persons.PersonsPackage

/**
 * An example of the automatic case class generation
 */
object GeneratePersonsCaseClasses {
	def main(args : Array[String]) {

		PersonsPackage.eINSTANCE

		// do the transformation work
		var startTime = System.nanoTime()
				
		ImplicitGenerator.analyzeEcore("file:../de.digitalforger.smtl.examples.model/resources/FamiliesToPersons/Persons.ecore",
			"src/de/digitalforger/smtl/examples/caseClasses/generatedCaseClasses/PersonsCC.scala",
			"PersonsCC",
			"de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses",
			"de.digitalforger.examples.model.persons")

		println("transformation took: " + (System.nanoTime() - startTime) * Math.pow(10, -9) + " seconds")				
	}
}
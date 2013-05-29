package de.digitalforger.smtl.examples.caseClasses

import de.digitalforger.examples.model.families.Families.FamiliesPackage
import de.digitalforger.smtl.util.ImplicitGenerator

/**
 * An example of the automatic case class generation
 */
object GenerateFamiliesCaseClasses {
	def main(args : Array[String]) {

		FamiliesPackage.eINSTANCE

		// do the transformation work
		var startTime = System.nanoTime()
				
		ImplicitGenerator.analyzeEcore("file:../de.digitalforger.smtl.examples.model/resources/FamiliesToPersons/Families.ecore",
			"src/de/digitalforger/smtl/examples/caseClasses/generatedCaseClasses/FamiliesCC.scala",
			"FamiliesCC",
			"de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses",
			"de.digitalforger.examples.model.families")

		println("transformation took: " + (System.nanoTime() - startTime) * Math.pow(10, -9) + " seconds")				
	}
}
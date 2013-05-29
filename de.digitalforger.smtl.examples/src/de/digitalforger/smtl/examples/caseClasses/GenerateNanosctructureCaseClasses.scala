package de.digitalforger.smtl.examples.caseClasses
import de.digitalforger.examples.model.families.Families.FamiliesPackage
import de.digitalforger.smtl.util.ImplicitGenerator
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.NanoStructurePackage

/**
 * An example of the automatic case class generation
 */
object GenerateNanosctructureCaseClasses {
def main(args : Array[String]) {

		NanoStructurePackage.eINSTANCE

		// do the transformation work
		var startTime = System.nanoTime()
				
		ImplicitGenerator.analyzeEcore("file:../de.huberlin.nanoworkbench2/resource/NanoStructure.ecore",
			"src/de/digitalforger/smtl/examples/caseClasses/generatedCaseClasses/nanostructure/NanoStructureCC.scala",
			"NanoStructureCC",
			"de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.nanostructure",
			"de.huberlin.nanoworkbench2.nanostructure")

		println("transformation took: " + (System.nanoTime() - startTime) * Math.pow(10, -9) + " seconds")				
	}
}
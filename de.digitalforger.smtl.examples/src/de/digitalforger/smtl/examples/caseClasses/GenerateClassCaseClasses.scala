package de.digitalforger.smtl.examples.caseClasses

import de.digitalforger.examples.model.Class.Class.ClassPackage
import de.digitalforger.smtl.util.ImplicitGenerator

/**
 * An example of the automatic case class generation
 */
object GenerateClassCaseClasses {
	def main(args : Array[String]) {

		ClassPackage.eINSTANCE

		// do the transformation work
		var startTime = System.nanoTime()
				
		ImplicitGenerator.analyzeEcore("file:../de.digitalforger.smtl.examples.model/resources/Class2Relational/Class.ecore",
			"src/de/digitalforger/smtl/examples/caseClasses/generatedCaseClasses/classExample/ClassCC.scala",
			"ClassCC",
			"de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.classExample",
			"de.digitalforger.examples.model.Class")

		println("transformation took: " + (System.nanoTime() - startTime) * Math.pow(10, -9) + " seconds")				
	}
}
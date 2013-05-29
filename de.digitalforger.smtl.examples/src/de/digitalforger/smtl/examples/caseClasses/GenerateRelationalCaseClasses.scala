package de.digitalforger.smtl.examples.caseClasses

import de.digitalforger.examples.model.Relation.Relation.RelationPackage
import de.digitalforger.smtl.util.ImplicitGenerator

/**
 * An example of the automatic case class generation
 */
object GenerateRelationalCaseClasses {
	def main(args : Array[String]) {

		RelationPackage.eINSTANCE

		// do the transformation work
		var startTime = System.nanoTime()
				
		ImplicitGenerator.analyzeEcore("file:../de.digitalforger.smtl.examples.model/resources/Class2Relational/Relation.ecore",
			"src/de/digitalforger/smtl/examples/caseClasses/generatedCaseClasses/classExample/RelationCC.scala",
			"RelationCC",
			"de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.classExample",
			"de.digitalforger.examples.model.Relation")

		println("transformation took: " + (System.nanoTime() - startTime) * Math.pow(10, -9) + " seconds")				
	}
}
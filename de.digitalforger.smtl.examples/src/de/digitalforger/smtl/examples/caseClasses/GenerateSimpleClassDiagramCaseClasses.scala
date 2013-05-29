package de.digitalforger.smtl.examples.caseClasses

import de.digitalforger.smtl.util.ImplicitGenerator
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.SimpleClassDiagramPackage

/**
 * An example of the automatic case class generation
 */
object GenerateSimpleClassDiagramCaseClasses {

	def main(args : Array[String]) : Unit = {		
		SimpleClassDiagramPackage.eINSTANCE
		
		// do the transformation work
		var startTime = System.nanoTime()
		
		ImplicitGenerator.analyzeEcore("file:../de.digitalforger.smtl.examples.model/resources/PublicToPrivate/SimpleClassDiagram.ecore",
			"src/de/digitalforger/smtl/examples/caseClasses/generatedCaseClasses/ClassDiagramCC.scala",
			"SimpleClassCC",
			"de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses",
			"de.digitalforger.examples.model.SimpleClassDiagram")

		println("transformation took: " + (System.nanoTime() - startTime) * Math.pow(10, -9) + " seconds")		
	}

}
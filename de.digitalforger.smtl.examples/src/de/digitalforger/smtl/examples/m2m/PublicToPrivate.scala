package de.digitalforger.smtl.examples.m2m

import de.digitalforger.smtl.m2m.Rule
import de.digitalforger.smtl.m2m.TransformationM2M
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.SimpleClassDiagramPackage
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.Attribute
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.Method
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.Parameter
import de.digitalforger.smtl.m2m.TransformationHelperSyntax._

/**
 * Example transformation for SMTL.
 * 
 * This is a refinement transformation. It uses only one model and changes it (internally a new model is created)
 */
object PublicToPrivate {

	def main(args : Array[String]) : Unit = {
		// init the models
		SimpleClassDiagramPackage.eINSTANCE.eClass()

		val inputFile = "file:../de.digitalforger.smtl.examples.model/resources/PublicToPrivate/privateClassDiagram.xmi"
		val classDiagramMetaModel = "http://www.digitalforger.de/examples/model/SimpleClassDiagram"

		// create the transformation
		val transform1 = new TransformationM2M("Public to private") refine classDiagramMetaModel in inputFile

		// change attributes that are visible and changeable
		val rule1 = new Rule[Attribute, Attribute]("Public attr to private") when ((s) => { s.isVisible() && s.isChangeable() }) perform ((fromAttr, toAttr) => {
			toAttr.setVisible(false)

			val getter = create[Method]
			getter.setName("get" + firstToUpper(fromAttr.getName()))
			getter.setReturn(fromAttr.getType())
			getter.setOwner(fromAttr.getOwner())

			val setterParameter = create[Parameter]
			setterParameter.setName("new" + firstToUpper(fromAttr.getName()) + "Value")
			setterParameter.setType(fromAttr.getType())

			val setter = create[Method]
			setter.setName("set" + firstToUpper(fromAttr.getName()))
			setter.getParameters().add(0, setterParameter)
			setter.setOwner(fromAttr.getOwner())

		})

		transform1.addRule(rule1)

		// do the transformation work
		var startTime = System.nanoTime()
		transform1 transform()
		println("transformation took: " + (System.nanoTime() - startTime) * Math.pow(10, -9) + " seconds")

		transform1.export("../de.digitalforger.smtl.examples.model/resources/PublicToPrivate/privatizedOutputWithSMTL.xmi")
	}

	def firstToUpper(str : String) : String = {
		str.substring(0, 1).toUpperCase() + str.substring(1)
	}

}
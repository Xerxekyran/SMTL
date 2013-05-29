package de.digitalforger.smtl.examples.m2m

import de.digitalforger.smtl.m2m.Rule
import de.digitalforger.smtl.m2m.TransformationM2M
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.SimpleClassDiagramPackage
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.Attribute
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.Method
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.Parameter
import de.digitalforger.smtl.m2m.IRule
import de.digitalforger.smtl.m2m.TransformationHelperSyntax._

/**
 * Example transformation for SMTL.
 * 
 * This is a refinement transformation. It uses only one model and changes it (internally a new model is created)
 * This version demonstrates a transformation chain with two transformations
 */
object PublicToPrivateChain {

  def main(args: Array[String]): Unit = {
    // init the models
    SimpleClassDiagramPackage.eINSTANCE.eClass()

    val inputFile = "file:../de.digitalforger.smtl.examples.model/resources/PublicToPrivate/privateClassDiagram.xmi"
    val classDiagramMetaModel = "http://www.digitalforger.de/examples/model/SimpleClassDiagram"

    // create the first transformation
    val transform1 = new TransformationM2M("Public to private") refine classDiagramMetaModel in inputFile
    transform1.addRule(getPublicToPrivateRule)
    
    // do the transformation work
    var startTime = System.nanoTime()
    transform1.transform()
    println("transformation 1 took: " + (System.nanoTime() - startTime) * Math.pow(10, -9) + " seconds")
    
    // create a 2nd transformation, that uses the first one as input
    val transform2 = new TransformationM2M("Addition information").refine(transform1)
    transform2.addRule(getAdditionalRule)
    
    startTime = System.nanoTime()
    transform2.transform()
    println("transformation 2 took: " + (System.nanoTime() - startTime) * Math.pow(10, -9) + " seconds")

    transform2.export("../de.digitalforger.smtl.examples.model/resources/PublicToPrivate/privatizedOutputWithSMTLChain.xmi")
  }

  /**
   * 
   */
  def getAdditionalRule() : IRule = {
    return new Rule[Attribute, Attribute]("Additional information") perform ((fromAttr, toAttr) => {
      toAttr.setName(toAttr.getName() + "Add")
    })
  }
  
  /**
   *
   */
  def getPublicToPrivateRule(): IRule = {
    // change attributes that are visible and changeable
    return new Rule[Attribute, Attribute]("Public attr to private") when ((s) => { s.isVisible() && s.isChangeable() }) perform ((fromAttr, toAttr) => {
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
  }

  def firstToUpper(str: String): String = {
    str.substring(0, 1).toUpperCase() + str.substring(1)
  }

}
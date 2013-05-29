package de.digitalforger.smtl.examples.caseClasses
import de.digitalforger.smtl.util.ImplicitGenerator

import de.huberlin.nanoworkbench2.nano.NanoPackage

/**
 * An example of the automatic case class generation
 */
object GenerateNanoCaseClasses {
  def main(args: Array[String]) {

    NanoPackage.eINSTANCE    
    
    // do the transformation work
    var startTime = System.nanoTime()

    ImplicitGenerator.analyzeEcore("file:../de.huberlin.nanoworkbench2/src-gen/de/huberlin/nanoworkbench2/Nano2.ecore",
      "src/de/digitalforger/smtl/examples/caseClasses/generatedCaseClasses/nano/NanoCC.scala",
      "NanoCC",
      "de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.nano",
      "de.huberlin.nanoworkbench2")

    println("transformation took: " + (System.nanoTime() - startTime) * Math.pow(10, -9) + " seconds")
  }
}
package de.digitalforger.smtl.examples.m2m

import java.lang.System

import scala.collection.JavaConversions.mutableSetAsJavaSet
import scala.collection.mutable.HashSet

import de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.nano.NanoCC.LineDelete2LineDeleteCC
import de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.nano.NanoCC.LineDeleteCC2LineDelete
import de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.nano.NanoCC.UnitOperation2UnitOperationCC
import de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.nanostructure.NanoStructureCC.let
import de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.nano.Coordinate2DCC
import de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.nano.DeleteCC
import de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.nano.LineDeleteCC
import de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.nano.StepCC
import de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.nano.UnitOperationCC
import de.digitalforger.smtl.m2m.Rule
import de.digitalforger.smtl.m2m.TransformationHelper
import de.digitalforger.smtl.m2m.TransformationHelperSyntax._
import de.digitalforger.smtl.m2m.TransformationM2M
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.Cuboid
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.Cylinder
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.Group
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.Material
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.NanoStructurePackage
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.StructuralElement
import de.huberlin.nanoworkbench2.nano.Binary
import de.huberlin.nanoworkbench2.nano.Calculation
import de.huberlin.nanoworkbench2.nano.Lattice
import de.huberlin.nanoworkbench2.nano.LatticeType
import de.huberlin.nanoworkbench2.nano.LineDelete
import de.huberlin.nanoworkbench2.nano.LineDirection
import de.huberlin.nanoworkbench2.nano.LineSelectionType
import de.huberlin.nanoworkbench2.nano.NanoPackage
import de.huberlin.nanoworkbench2.nano.NumberLiteral
import de.huberlin.nanoworkbench2.nano.Slab
import de.huberlin.nanoworkbench2.nano.UnitOperation

/**
 * Example transformation for SMTL. 
 * Nano models are transformed into NanoStructure models. These models then can be viewed with the NanoStructure View (Eclipse-Plug-In)
 *  
 */
object NanoToNanostructure {

  def main(args: Array[String]) {
    NanoStructurePackage.eINSTANCE.eClass()
    NanoPackage.eINSTANCE.eClass()

    val inputFile = "file:../de.huberlin.nanoworkbench2/nanomodel.xmi"
    val nanoMetaModel = "http://www.huberlin.de/Nano"
    val nanoStructureMetaModel = "http://nanostructure/1.0"

    val transformation = new TransformationM2M("Nano to nanostructure") from nanoMetaModel to nanoStructureMetaModel

    // *******************************************
    // material conversion rule
    // *******************************************
    implicit val materialRule = new Rule[de.huberlin.nanoworkbench2.nano.Material, Material]("Material to Material") perform ((m1, m2) => {
      m2.setName(m1.getName())
      m2.setRefractionIndex(m1.getIndex().doubleValue())
    })

    // *******************************************
    // rule to transform a slab to an cuboid
    // *******************************************
    val slab = new Rule[Slab, Cuboid]("SlabToCuboid") perform ((slab, cub) => {
      cub.setName("Slab")
      cub.setXPos(slab.getCoordinate().getX().getValue().doubleValue())
      cub.setYPos(slab.getCoordinate().getY().getValue().doubleValue())
      cub.setZPos(slab.getCoordinate().getZ().getValue().doubleValue())

      // implicit conversion from MM1 to MM2
      cub.setMaterial(slab.getMaterial())

      cub.setDepth(executeCalculation(slab.getThickness()))
    })

    // *******************************************
    // the rule that handles the lattice
    // *******************************************
    val lattice = new Rule[Lattice, Group]("LatticeToGroup") perform ((lat, grp) => {

      val radius = executeCalculation(lat.getHoleRadius());
      val distance = executeCalculation(lat.getDistance());
      val height_hexagonal_distance = Math.sqrt((distance * distance) - ((distance / 2) * (distance / 2)));
      var maxX = lat.getXNumber().intValue()
      var maxY = lat.getYNumber().intValue()
      var xShiftVal = ((maxX - 1) / 2.0).intValue()
      var yShiftVal = ((maxY - 1) / 2.0).intValue()
      var lst: HashSet[StructuralElement] = null;

      grp.setName("Lattice")

      val unitOperations: Set[UnitOperation] = getInputElements[UnitOperation].asInstanceOf[Set[UnitOperation]];
      val doNotCreate = readDeleteUnitOperations(unitOperations)

      if (lat.getLatticeType() == LatticeType.HEXAGONAL) {
        val slab: Cuboid = getCreatedElements[Cuboid].filter(s => s.asInstanceOf[Cuboid].getName().equals("Slab")).head.asInstanceOf[Cuboid]

        slab.setWidth(distance * maxX + height_hexagonal_distance)
        slab.setHeight(height_hexagonal_distance * maxY)

        xShiftVal = ((maxX - 1)).intValue()
        maxX = maxX * 2
        yShiftVal = ((maxY) / 2.0).intValue()

        lst = generateHoles(lat.getLatticeType(), 0 - xShiftVal, 0 - yShiftVal, maxX - xShiftVal, maxY - yShiftVal - 1, radius, distance, height_hexagonal_distance, doNotCreate)
      }

      grp.getElements().clear()
      grp.getElements().addAll(0, lst)
    })

    // add the rules
    transformation.addRule(slab, lattice, materialRule)

    // do the transformation work
    var startTime = System.nanoTime()
    transformation transform inputFile
    println("transformation took: " + (System.nanoTime() - startTime) * Math.pow(10, -9) + " seconds")

    // write the result to a file
    transformation.export("../Masterarbeit_ATL/nanoStructure/nanoStructureWithSMTL.xmi")

  }

  /**
   *
   */
  def readDeleteUnitOperations(unitOperation: Set[UnitOperation]): HashSet[String] = {
    var ret: HashSet[String] = new HashSet()

    for (op <- unitOperation) {
      ret = let(op) match {
        case UnitOperationCC(DeleteCC(_, ld @ LineDeleteCC(LineDirection.HORIZONTAL, _, _, _, _, _), _), _, _) => ret.union(readHorizontalLineDeleteUnitOperation(ld))
        case UnitOperationCC(DeleteCC(_, ld @ LineDeleteCC(LineDirection.DIAGONALPLUS60, _, _, _, _, _), _), _, _) => ret.union(readDiagonalPlusLineDeleteUnitOperation(ld))
        case UnitOperationCC(DeleteCC(_, ld @ LineDeleteCC(LineDirection.DIAGONALMINUS60, _, _, _, _, _), _), _, _) => ret.union(readDiagonalMinusLineDeleteUnitOperation(ld))
        case _ => ret
      }
    }
    ret
  }

  /**
   *
   */
  def readDiagonalMinusLineDeleteUnitOperation(lineDel: LineDelete): HashSet[String] = {
    var ret: HashSet[String] = new HashSet()

    ret = let(lineDel) match {
      case LineDeleteCC(_, _, LineSelectionType.SPAN, Coordinate2DCC(coord1X, coord1Y), _, StepCC(stepValue)) =>
        val moveVal = ((stepValue.intValue() - 0.5) / 2).toInt
        ret.union(getIndicesFromToDiagonalMinus(coord1X.intValue() - moveVal, coord1Y.intValue() + moveVal, coord1X.intValue() + moveVal))
      case _ => ret
    }

    ret
  }

  /**
   *
   */
  def getIndicesFromToDiagonalMinus(fromX: Int, fromY: Int, toX: Int): HashSet[String] = {
    var ret: HashSet[String] = new HashSet()
    var y = fromY
    for (x <- fromX to toX) {
      ret.add(x + "_" + y)
      y -= 1
    }
    ret
  }

  /**
   *
   */
  def readDiagonalPlusLineDeleteUnitOperation(lineDel: LineDelete): HashSet[String] = {
    var ret: HashSet[String] = new HashSet()

    ret = let(lineDel) match {
      case LineDeleteCC(_, _, LineSelectionType.SPAN, Coordinate2DCC(coord1X, coord1Y), _, StepCC(stepValue)) =>
        val moveVal = ((stepValue.intValue() - 0.5) / 2).toInt
        ret.union(getIndicesFromToDiagonalPlus(coord1X.intValue() - moveVal, coord1Y.intValue() - moveVal, coord1X.intValue() + moveVal))
      case _ => ret
    }
    ret
  }

  /**
   *
   */
  def getIndicesFromToDiagonalPlus(fromX: Int, fromY: Int, toX: Int): HashSet[String] = {
    var ret: HashSet[String] = new HashSet()
    var y = fromY
    for (x <- fromX to toX) {
      ret.add(x + "_" + y)
      y += 1
    }
    ret
  }

  /**
   *
   */
  def readHorizontalLineDeleteUnitOperation(lineDel: LineDelete): HashSet[String] = {
    var ret: HashSet[String] = new HashSet()

    ret = let(lineDel) match {
      case LineDeleteCC(_, _, LineSelectionType.SPAN, Coordinate2DCC(coord1X, coord1Y), _, StepCC(stepValue)) =>
        val moveVal = ((stepValue.intValue() + 2) / 2).toInt
        ret.union(getIndicesFromToHorizontal(coord1X.intValue() - moveVal, coord1Y.intValue(), coord1X.intValue() + moveVal))
      case _ => ret
    }
    ret
  }

  /**
   *
   */
  def getIndicesFromToHorizontal(fromX: Int, fromY: Int, toX: Int): HashSet[String] = {
    var ret: HashSet[String] = new HashSet()
    for (x <- fromX to toX) {
      ret.add(x + "_" + fromY)
    }
    ret
  }

  /**
   *
   */
  def generateHoles(latType: LatticeType, startX: Int, startY: Int, maxX: Int, maxY: Int, radius: Double, x_distance: Double, y_distance: Double, doNotCreate: HashSet[String]): HashSet[StructuralElement] = {

    var lst = new HashSet[StructuralElement]()

    // for all new holes to create
    var endX = maxX;
    for (y <- startY to maxY) {
      var x = startX
      if (latType.equals(LatticeType.HEXAGONAL) && y % 2 != 0)
        x += 1

      while (x < endX) {
        if (!doNotCreate.contains(x + "_" + y)) {
          val cyl = create[Cylinder]
          cyl.setName("Hole_" + x + "_" + y)
          cyl.setRadius(radius)
          cyl.setZPos(0.0)
          cyl.setYPos(y * y_distance)

          // the y position is different in the lattices
          if (latType.equals(LatticeType.HEXAGONAL))
            cyl.setXPos((x / 2.0) * x_distance)
          else
            cyl.setXPos(x * x_distance)

          // add the new element to the list
          lst += cyl
        }

        // next x position
        if (latType.equals(LatticeType.HEXAGONAL))
          x += 2
        else
          x += 1
      }

      // one line is complete
      if (latType.equals(LatticeType.HEXAGONAL) && y % 2 != 0)
        endX = endX + 1;
      else
        endX = endX - 1;
    }

    lst
  }

  /**
   * reading the calculation and returning its corresponding value
   *
   * @param calc the calculation that should be calculated
   * @return the double value of the calculation term
   */
  def executeCalculation(calc: Calculation): Double = {

    // is this anumber literal?
    if (calc.isInstanceOf[NumberLiteral]) {
      // a true value or a reference to a variable?
      if (calc.asInstanceOf[NumberLiteral].getValue() != null) {
        return calc.asInstanceOf[NumberLiteral].getValue().doubleValue();
      } else {
        // target is a variable
        return calc.asInstanceOf[NumberLiteral].getValue2().getNumber().asInstanceOf[NumberLiteral].getValue().doubleValue();
      }
    } else {
      // its a term
      if (calc.isInstanceOf[Binary] && calc.getOp() == "*") {
        // multiplication
        return executeCalculation(calc.getLeft()) * executeCalculation(calc.asInstanceOf[Binary].getRight());
      }
    }

    // default return zero
    0.0;
  }

}
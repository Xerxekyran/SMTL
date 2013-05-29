package de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.nano

import de.digitalforger.smtl.core._
import de.digitalforger.smtl.m2m.TransformationHelperSyntax._
import java.lang.String
import java.math.BigDecimal
import de.huberlin.nanoworkbench2.nano.SetupSec
import de.huberlin.nanoworkbench2.nano.UnitSec
import de.huberlin.nanoworkbench2.nano.CodeSec
import de.huberlin.nanoworkbench2.nano.StructureSec
import de.huberlin.nanoworkbench2.nano.SimulationSec
import de.huberlin.nanoworkbench2.nano.SourcesSec
import de.huberlin.nanoworkbench2.nano.MonitorsSec
import de.huberlin.nanoworkbench2.nano.Instance
import de.huberlin.nanoworkbench2.nano.Dimension
import de.huberlin.nanoworkbench2.nano.Material
import de.huberlin.nanoworkbench2.nano.Objects
import de.huberlin.nanoworkbench2.nano.Calculation
import de.huberlin.nanoworkbench2.nano.Slab
import de.huberlin.nanoworkbench2.nano.Lattice
import de.huberlin.nanoworkbench2.nano.Element
import de.huberlin.nanoworkbench2.nano.Coordinate3DSlab
import de.huberlin.nanoworkbench2.nano.UnitOperation
import de.huberlin.nanoworkbench2.nano.Delete
import de.huberlin.nanoworkbench2.nano.Move
import de.huberlin.nanoworkbench2.nano.Overwrite
import de.huberlin.nanoworkbench2.nano.SingleDelete
import de.huberlin.nanoworkbench2.nano.LineDelete
import de.huberlin.nanoworkbench2.nano.RangeDelete
import de.huberlin.nanoworkbench2.nano.Coordinate2D
import de.huberlin.nanoworkbench2.nano.Step
import de.huberlin.nanoworkbench2.nano.SingleMove
import de.huberlin.nanoworkbench2.nano.LineMove
import de.huberlin.nanoworkbench2.nano.RangeMove
import de.huberlin.nanoworkbench2.nano.SingleOverwrite
import de.huberlin.nanoworkbench2.nano.LineOverwrite
import de.huberlin.nanoworkbench2.nano.RangeOverwrite
import de.huberlin.nanoworkbench2.nano.Coordinate3D
import de.huberlin.nanoworkbench2.nano.Dipole
import de.huberlin.nanoworkbench2.nano.BroadbandSource
import de.huberlin.nanoworkbench2.nano.NarrowbandSource
import de.huberlin.nanoworkbench2.nano.Monitor
import de.huberlin.nanoworkbench2.nano.Model
import de.huberlin.nanoworkbench2.nano.LengthUnit
import de.huberlin.nanoworkbench2.nano.TimeUnit
import de.huberlin.nanoworkbench2.nano.FrequencyUnit
import de.huberlin.nanoworkbench2.nano.LossUnit
import de.huberlin.nanoworkbench2.nano.PredefinedMaterial
import de.huberlin.nanoworkbench2.nano.LogicOperation
import de.huberlin.nanoworkbench2.nano.LatticeType
import de.huberlin.nanoworkbench2.nano.LineDirection
import de.huberlin.nanoworkbench2.nano.LineSelectionType
import de.huberlin.nanoworkbench2.nano.NewElement
import de.huberlin.nanoworkbench2.nano.Cuboid
import de.huberlin.nanoworkbench2.nano.Cone
import de.huberlin.nanoworkbench2.nano.Sphere
import de.huberlin.nanoworkbench2.nano.Ring
import de.huberlin.nanoworkbench2.nano.Pyramid
import de.huberlin.nanoworkbench2.nano.Boundaries
import de.huberlin.nanoworkbench2.nano.NumberLiteral
import de.huberlin.nanoworkbench2.nano.Binary
import de.huberlin.nanoworkbench2.nano.Parenthesis
import de.huberlin.nanoworkbench2.nano.impl.NanoFactoryImpl

case class ModelCC (setupSec : SetupSecCC, unitSec : UnitSecCC, codeSec : CodeSecCC, structureSec : StructureSecCC, simulationSec : SimulationSecCC, sourcesSec : SourcesSecCC, monitorsSec : MonitorsSecCC, codeSec2 : CodeSecCC ) extends IGeneratedCC
case class SetupSecCC (projectName : String, values : org.eclipse.emf.common.util.EList[Instance] ) extends IGeneratedCC
case class UnitSecCC (lengthUnit : LengthUnit, timeUnit : TimeUnit, frequencyUnit : FrequencyUnit, lossUnit : LossUnit) extends IGeneratedCC
case class CodeSecCC (codeBlock : String) extends IGeneratedCC
case class Coordinate2DCC (x : BigDecimal, y : BigDecimal) extends IGeneratedCC
case class Coordinate3DCC (x : BigDecimal, y : BigDecimal, z : BigDecimal) extends IGeneratedCC
case class Coordinate3DSlabCC (x : DimensionCC, y : DimensionCC, z : DimensionCC ) extends IGeneratedCC
case class StructureSecCC (material : org.eclipse.emf.common.util.EList[Material], objects : org.eclipse.emf.common.util.EList[Objects] ) extends IGeneratedCC
case class InstanceCC (name : String, filepath : String, number : CalculationCC ) extends IGeneratedCC
case class MaterialCC (name : String, index : BigDecimal) extends IGeneratedCC
case class ObjectsCC (slab : SlabCC, lattice : LatticeCC, element : ElementCC ) extends IGeneratedCC
case class SlabCC (coordinate : Coordinate3DSlabCC, material : MaterialCC, thickness : CalculationCC ) extends IGeneratedCC
case class DimensionCC (value : BigDecimal) extends IGeneratedCC
case class LatticeCC (latticeType : LatticeType, xNumber : BigDecimal, yNumber : BigDecimal, holeRadiusToDistance : BigDecimal, distance : CalculationCC, holeRadius : CalculationCC, unitOperations : org.eclipse.emf.common.util.EList[UnitOperation] ) extends IGeneratedCC
case class UnitOperationCC (delete : DeleteCC, move : MoveCC, overwrite : OverwriteCC ) extends IGeneratedCC
case class StepCC (steps : BigDecimal) extends IGeneratedCC
case class DeleteCC (single : SingleDeleteCC, line : LineDeleteCC, range : RangeDeleteCC ) extends IGeneratedCC
case class SingleDeleteCC (coordinate : Coordinate2DCC ) extends IGeneratedCC
case class LineDeleteCC (direction : LineDirection, center : String, lineSelectType : LineSelectionType, coordinate1 : Coordinate2DCC, coordinate2 : Coordinate2DCC, step : StepCC ) extends IGeneratedCC
case class RangeDeleteCC (coordinate1 : Coordinate2DCC, coordinate2 : Coordinate2DCC ) extends IGeneratedCC
case class MoveCC (single : SingleMoveCC, line : LineMoveCC, range : RangeMoveCC ) extends IGeneratedCC
case class SingleMoveCC (coordinate : Coordinate2DCC, xOffset : CalculationCC, yOffset : CalculationCC ) extends IGeneratedCC
case class LineMoveCC (direction : LineDirection, center : String, lineSelectType : LineSelectionType, coordinate1 : Coordinate2DCC, coordinate2 : Coordinate2DCC, step : StepCC, xOffset : CalculationCC, yOffset : CalculationCC ) extends IGeneratedCC
case class RangeMoveCC (coordinate1 : Coordinate2DCC, coordinate2 : Coordinate2DCC, xOffset : CalculationCC, yOffset : CalculationCC ) extends IGeneratedCC
case class OverwriteCC (single : SingleOverwriteCC, line : LineOverwriteCC, range : RangeOverwriteCC ) extends IGeneratedCC
case class SingleOverwriteCC (coordinate : Coordinate2DCC, element : org.eclipse.emf.common.util.EList[Element] ) extends IGeneratedCC
case class LineOverwriteCC (direction : LineDirection, center : String, lineSelectType : LineSelectionType, coordinate1 : Coordinate2DCC, coordinate2 : Coordinate2DCC, step : StepCC, element : org.eclipse.emf.common.util.EList[Element] ) extends IGeneratedCC
case class RangeOverwriteCC (coordinate1 : Coordinate2DCC, coordinate2 : Coordinate2DCC, element : org.eclipse.emf.common.util.EList[Element] ) extends IGeneratedCC
case class ElementCC (material2 : PredefinedMaterial, coordinate : Coordinate3DCC, material : MaterialCC ) extends IGeneratedCC
case class NewElementCC (material2 : PredefinedMaterial, name : String, logicoperation : LogicOperation, element1 : ElementCC, element2 : ElementCC ) extends IGeneratedCC
case class CuboidCC (material2 : PredefinedMaterial, height : CalculationCC, width : CalculationCC, depth : CalculationCC ) extends IGeneratedCC
case class ConeCC (material2 : PredefinedMaterial, radiusGround : CalculationCC, radiusTop : CalculationCC, height : CalculationCC ) extends IGeneratedCC
case class SphereCC (material2 : PredefinedMaterial, radius : CalculationCC ) extends IGeneratedCC
case class RingCC (material2 : PredefinedMaterial, innerRadius : CalculationCC, outerRadius : CalculationCC, height : CalculationCC, thetaStart : CalculationCC, thetaStop : CalculationCC ) extends IGeneratedCC
case class PyramidCC (material2 : PredefinedMaterial, height : CalculationCC, xSpanBottom : CalculationCC, ySpanBottom : CalculationCC, xSpanTop : CalculationCC, ySpanTop : CalculationCC ) extends IGeneratedCC
case class SimulationSecCC (simulationTime : BigDecimal, bgIndex : BigDecimal, xBoundary : Boundaries, yBoundary : Boundaries, zBoundary : Boundaries, dx : CalculationCC, dy : CalculationCC, dz : CalculationCC ) extends IGeneratedCC
case class SourcesSecCC (dipole : DipoleCC ) extends IGeneratedCC
case class BroadbandSourceCC (wavelengthCenter : CalculationCC, wavelengthSpan : CalculationCC ) extends IGeneratedCC
case class NarrowbandSourceCC (frequency : CalculationCC, wavelength : CalculationCC, pulseLength : CalculationCC, pulseOffset : CalculationCC ) extends IGeneratedCC
case class DipoleCC (typeValue : String, coordinate : Coordinate3DCC, dipoleTheta : CalculationCC, dipolePhi : CalculationCC, broadbandSource : BroadbandSourceCC, narrowbandSource : NarrowbandSourceCC ) extends IGeneratedCC
case class MonitorsSecCC (monitors : org.eclipse.emf.common.util.EList[Monitor] ) extends IGeneratedCC
case class MonitorCC (typeValue : String, name : String, coordinate : Coordinate3DCC ) extends IGeneratedCC
case class CalculationCC (op : String, left : CalculationCC ) extends IGeneratedCC
case class NumberLiteralCC (op : String, value : BigDecimal, value2 : InstanceCC ) extends IGeneratedCC
case class BinaryCC (op : String, right : CalculationCC ) extends IGeneratedCC
case class ParenthesisCC (op : String, expression : CalculationCC ) extends IGeneratedCC

object NanoCC{
	def let(obj: IGeneratedCC) = obj

	implicit def Model2ModelCC(obj : Model) : ModelCC = ModelCC(if(obj != null) obj.getSetupSec() else null, if(obj != null) obj.getUnitSec() else null, if(obj != null) obj.getCodeSec() else null, if(obj != null) obj.getStructureSec() else null, if(obj != null) obj.getSimulationSec() else null, if(obj != null) obj.getSourcesSec() else null, if(obj != null) obj.getMonitorsSec() else null, if(obj != null) obj.getCodeSec2() else null )
	implicit def ModelCC2Model(obj : ModelCC): Model = { 
		val ret : Model = NanoFactoryImpl.init().createModel()
		ret.setCodeSec2(obj.codeSec2)
		ret.setMonitorsSec(obj.monitorsSec)
		ret.setSourcesSec(obj.sourcesSec)
		ret.setSimulationSec(obj.simulationSec)
		ret.setStructureSec(obj.structureSec)
		ret.setCodeSec(obj.codeSec)
		ret.setUnitSec(obj.unitSec)
		ret.setSetupSec(obj.setupSec)
		register(ret)
		ret
	}
	implicit def SetupSec2SetupSecCC(obj : SetupSec) : SetupSecCC = SetupSecCC(if(obj != null) obj.getProjectName() else null, if(obj != null) obj.getValues() else null )
	implicit def SetupSecCC2SetupSec(obj : SetupSecCC): SetupSec = { 
		val ret : SetupSec = NanoFactoryImpl.init().createSetupSec()
		ret.getValues().addAll(0, obj.values)
		ret.setProjectName(obj.projectName)
		register(ret)
		ret
	}
	implicit def UnitSec2UnitSecCC(obj : UnitSec) : UnitSecCC = UnitSecCC(if(obj != null) obj.getLengthUnit() else null, if(obj != null) obj.getTimeUnit() else null, if(obj != null) obj.getFrequencyUnit() else null, if(obj != null) obj.getLossUnit() else null )
	implicit def UnitSecCC2UnitSec(obj : UnitSecCC): UnitSec = { 
		val ret : UnitSec = NanoFactoryImpl.init().createUnitSec()
		ret.setLossUnit(obj.lossUnit)
		ret.setFrequencyUnit(obj.frequencyUnit)
		ret.setTimeUnit(obj.timeUnit)
		ret.setLengthUnit(obj.lengthUnit)
		register(ret)
		ret
	}
	implicit def CodeSec2CodeSecCC(obj : CodeSec) : CodeSecCC = CodeSecCC(if(obj != null) obj.getCodeBlock() else null )
	implicit def CodeSecCC2CodeSec(obj : CodeSecCC): CodeSec = { 
		val ret : CodeSec = NanoFactoryImpl.init().createCodeSec()
		ret.setCodeBlock(obj.codeBlock)
		register(ret)
		ret
	}
	implicit def Coordinate2D2Coordinate2DCC(obj : Coordinate2D) : Coordinate2DCC = Coordinate2DCC(if(obj != null) obj.getX() else null, if(obj != null) obj.getY() else null )
	implicit def Coordinate2DCC2Coordinate2D(obj : Coordinate2DCC): Coordinate2D = { 
		val ret : Coordinate2D = NanoFactoryImpl.init().createCoordinate2D()
		ret.setY(obj.y)
		ret.setX(obj.x)
		register(ret)
		ret
	}
	implicit def Coordinate3D2Coordinate3DCC(obj : Coordinate3D) : Coordinate3DCC = Coordinate3DCC(if(obj != null) obj.getX() else null, if(obj != null) obj.getY() else null, if(obj != null) obj.getZ() else null )
	implicit def Coordinate3DCC2Coordinate3D(obj : Coordinate3DCC): Coordinate3D = { 
		val ret : Coordinate3D = NanoFactoryImpl.init().createCoordinate3D()
		ret.setZ(obj.z)
		ret.setY(obj.y)
		ret.setX(obj.x)
		register(ret)
		ret
	}
	implicit def Coordinate3DSlab2Coordinate3DSlabCC(obj : Coordinate3DSlab) : Coordinate3DSlabCC = Coordinate3DSlabCC(if(obj != null) obj.getX() else null, if(obj != null) obj.getY() else null, if(obj != null) obj.getZ() else null )
	implicit def Coordinate3DSlabCC2Coordinate3DSlab(obj : Coordinate3DSlabCC): Coordinate3DSlab = { 
		val ret : Coordinate3DSlab = NanoFactoryImpl.init().createCoordinate3DSlab()
		ret.setZ(obj.z)
		ret.setY(obj.y)
		ret.setX(obj.x)
		register(ret)
		ret
	}
	implicit def StructureSec2StructureSecCC(obj : StructureSec) : StructureSecCC = StructureSecCC(if(obj != null) obj.getMaterial() else null, if(obj != null) obj.getObjects() else null )
	implicit def StructureSecCC2StructureSec(obj : StructureSecCC): StructureSec = { 
		val ret : StructureSec = NanoFactoryImpl.init().createStructureSec()
		ret.getObjects().addAll(0, obj.objects)
		ret.getMaterial().addAll(0, obj.material)
		register(ret)
		ret
	}
	implicit def Instance2InstanceCC(obj : Instance) : InstanceCC = InstanceCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getFilepath() else null, if(obj != null) obj.getNumber() else null )
	implicit def InstanceCC2Instance(obj : InstanceCC): Instance = { 
		val ret : Instance = NanoFactoryImpl.init().createInstance()
		ret.setNumber(obj.number)
		ret.setFilepath(obj.filepath)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Material2MaterialCC(obj : Material) : MaterialCC = MaterialCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getIndex() else null )
	implicit def MaterialCC2Material(obj : MaterialCC): Material = { 
		val ret : Material = NanoFactoryImpl.init().createMaterial()
		ret.setIndex(obj.index)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Objects2ObjectsCC(obj : Objects) : ObjectsCC = ObjectsCC(if(obj != null) obj.getSlab() else null, if(obj != null) obj.getLattice() else null, if(obj != null) obj.getElement() else null )
	implicit def ObjectsCC2Objects(obj : ObjectsCC): Objects = { 
		val ret : Objects = NanoFactoryImpl.init().createObjects()
		ret.setElement(obj.element)
		ret.setLattice(obj.lattice)
		ret.setSlab(obj.slab)
		register(ret)
		ret
	}
	implicit def Slab2SlabCC(obj : Slab) : SlabCC = SlabCC(if(obj != null) obj.getCoordinate() else null, if(obj != null) obj.getMaterial() else null, if(obj != null) obj.getThickness() else null )
	implicit def SlabCC2Slab(obj : SlabCC): Slab = { 
		val ret : Slab = NanoFactoryImpl.init().createSlab()
		ret.setThickness(obj.thickness)
		ret.setMaterial(obj.material)
		ret.setCoordinate(obj.coordinate)
		register(ret)
		ret
	}
	implicit def Dimension2DimensionCC(obj : Dimension) : DimensionCC = DimensionCC(if(obj != null) obj.getValue() else null )
	implicit def DimensionCC2Dimension(obj : DimensionCC): Dimension = { 
		val ret : Dimension = NanoFactoryImpl.init().createDimension()
		ret.setValue(obj.value)
		register(ret)
		ret
	}
	implicit def Lattice2LatticeCC(obj : Lattice) : LatticeCC = LatticeCC(if(obj != null) obj.getLatticeType() else null, if(obj != null) obj.getXNumber() else null, if(obj != null) obj.getYNumber() else null, if(obj != null) obj.getHoleRadiusToDistance() else null, if(obj != null) obj.getDistance() else null, if(obj != null) obj.getHoleRadius() else null, if(obj != null) obj.getUnitOperations() else null )
	implicit def LatticeCC2Lattice(obj : LatticeCC): Lattice = { 
		val ret : Lattice = NanoFactoryImpl.init().createLattice()
		ret.getUnitOperations().addAll(0, obj.unitOperations)
		ret.setHoleRadius(obj.holeRadius)
		ret.setDistance(obj.distance)
		ret.setHoleRadiusToDistance(obj.holeRadiusToDistance)
		ret.setYNumber(obj.yNumber)
		ret.setXNumber(obj.xNumber)
		ret.setLatticeType(obj.latticeType)
		register(ret)
		ret
	}
	implicit def UnitOperation2UnitOperationCC(obj : UnitOperation) : UnitOperationCC = UnitOperationCC(if(obj != null) obj.getDelete() else null, if(obj != null) obj.getMove() else null, if(obj != null) obj.getOverwrite() else null )
	implicit def UnitOperationCC2UnitOperation(obj : UnitOperationCC): UnitOperation = { 
		val ret : UnitOperation = NanoFactoryImpl.init().createUnitOperation()
		ret.setOverwrite(obj.overwrite)
		ret.setMove(obj.move)
		ret.setDelete(obj.delete)
		register(ret)
		ret
	}
	implicit def Step2StepCC(obj : Step) : StepCC = StepCC(if(obj != null) obj.getSteps() else null )
	implicit def StepCC2Step(obj : StepCC): Step = { 
		val ret : Step = NanoFactoryImpl.init().createStep()
		ret.setSteps(obj.steps)
		register(ret)
		ret
	}
	implicit def Delete2DeleteCC(obj : Delete) : DeleteCC = DeleteCC(if(obj != null) obj.getSingle() else null, if(obj != null) obj.getLine() else null, if(obj != null) obj.getRange() else null )
	implicit def DeleteCC2Delete(obj : DeleteCC): Delete = { 
		val ret : Delete = NanoFactoryImpl.init().createDelete()
		ret.setRange(obj.range)
		ret.setLine(obj.line)
		ret.setSingle(obj.single)
		register(ret)
		ret
	}
	implicit def SingleDelete2SingleDeleteCC(obj : SingleDelete) : SingleDeleteCC = SingleDeleteCC(if(obj != null) obj.getCoordinate() else null )
	implicit def SingleDeleteCC2SingleDelete(obj : SingleDeleteCC): SingleDelete = { 
		val ret : SingleDelete = NanoFactoryImpl.init().createSingleDelete()
		ret.setCoordinate(obj.coordinate)
		register(ret)
		ret
	}
	implicit def LineDelete2LineDeleteCC(obj : LineDelete) : LineDeleteCC = LineDeleteCC(if(obj != null) obj.getDirection() else null, if(obj != null) obj.getCenter() else null, if(obj != null) obj.getLineSelectType() else null, if(obj != null) obj.getCoordinate1() else null, if(obj != null) obj.getCoordinate2() else null, if(obj != null) obj.getStep() else null )
	implicit def LineDeleteCC2LineDelete(obj : LineDeleteCC): LineDelete = { 
		val ret : LineDelete = NanoFactoryImpl.init().createLineDelete()
		ret.setStep(obj.step)
		ret.setCoordinate2(obj.coordinate2)
		ret.setCoordinate1(obj.coordinate1)
		ret.setLineSelectType(obj.lineSelectType)
		ret.setCenter(obj.center)
		ret.setDirection(obj.direction)
		register(ret)
		ret
	}
	implicit def RangeDelete2RangeDeleteCC(obj : RangeDelete) : RangeDeleteCC = RangeDeleteCC(if(obj != null) obj.getCoordinate1() else null, if(obj != null) obj.getCoordinate2() else null )
	implicit def RangeDeleteCC2RangeDelete(obj : RangeDeleteCC): RangeDelete = { 
		val ret : RangeDelete = NanoFactoryImpl.init().createRangeDelete()
		ret.setCoordinate2(obj.coordinate2)
		ret.setCoordinate1(obj.coordinate1)
		register(ret)
		ret
	}
	implicit def Move2MoveCC(obj : Move) : MoveCC = MoveCC(if(obj != null) obj.getSingle() else null, if(obj != null) obj.getLine() else null, if(obj != null) obj.getRange() else null )
	implicit def MoveCC2Move(obj : MoveCC): Move = { 
		val ret : Move = NanoFactoryImpl.init().createMove()
		ret.setRange(obj.range)
		ret.setLine(obj.line)
		ret.setSingle(obj.single)
		register(ret)
		ret
	}
	implicit def SingleMove2SingleMoveCC(obj : SingleMove) : SingleMoveCC = SingleMoveCC(if(obj != null) obj.getCoordinate() else null, if(obj != null) obj.getXOffset() else null, if(obj != null) obj.getYOffset() else null )
	implicit def SingleMoveCC2SingleMove(obj : SingleMoveCC): SingleMove = { 
		val ret : SingleMove = NanoFactoryImpl.init().createSingleMove()
		ret.setYOffset(obj.yOffset)
		ret.setXOffset(obj.xOffset)
		ret.setCoordinate(obj.coordinate)
		register(ret)
		ret
	}
	implicit def LineMove2LineMoveCC(obj : LineMove) : LineMoveCC = LineMoveCC(if(obj != null) obj.getDirection() else null, if(obj != null) obj.getCenter() else null, if(obj != null) obj.getLineSelectType() else null, if(obj != null) obj.getCoordinate1() else null, if(obj != null) obj.getCoordinate2() else null, if(obj != null) obj.getStep() else null, if(obj != null) obj.getXOffset() else null, if(obj != null) obj.getYOffset() else null )
	implicit def LineMoveCC2LineMove(obj : LineMoveCC): LineMove = { 
		val ret : LineMove = NanoFactoryImpl.init().createLineMove()
		ret.setYOffset(obj.yOffset)
		ret.setXOffset(obj.xOffset)
		ret.setStep(obj.step)
		ret.setCoordinate2(obj.coordinate2)
		ret.setCoordinate1(obj.coordinate1)
		ret.setLineSelectType(obj.lineSelectType)
		ret.setCenter(obj.center)
		ret.setDirection(obj.direction)
		register(ret)
		ret
	}
	implicit def RangeMove2RangeMoveCC(obj : RangeMove) : RangeMoveCC = RangeMoveCC(if(obj != null) obj.getCoordinate1() else null, if(obj != null) obj.getCoordinate2() else null, if(obj != null) obj.getXOffset() else null, if(obj != null) obj.getYOffset() else null )
	implicit def RangeMoveCC2RangeMove(obj : RangeMoveCC): RangeMove = { 
		val ret : RangeMove = NanoFactoryImpl.init().createRangeMove()
		ret.setYOffset(obj.yOffset)
		ret.setXOffset(obj.xOffset)
		ret.setCoordinate2(obj.coordinate2)
		ret.setCoordinate1(obj.coordinate1)
		register(ret)
		ret
	}
	implicit def Overwrite2OverwriteCC(obj : Overwrite) : OverwriteCC = OverwriteCC(if(obj != null) obj.getSingle() else null, if(obj != null) obj.getLine() else null, if(obj != null) obj.getRange() else null )
	implicit def OverwriteCC2Overwrite(obj : OverwriteCC): Overwrite = { 
		val ret : Overwrite = NanoFactoryImpl.init().createOverwrite()
		ret.setRange(obj.range)
		ret.setLine(obj.line)
		ret.setSingle(obj.single)
		register(ret)
		ret
	}
	implicit def SingleOverwrite2SingleOverwriteCC(obj : SingleOverwrite) : SingleOverwriteCC = SingleOverwriteCC(if(obj != null) obj.getCoordinate() else null, if(obj != null) obj.getElement() else null )
	implicit def SingleOverwriteCC2SingleOverwrite(obj : SingleOverwriteCC): SingleOverwrite = { 
		val ret : SingleOverwrite = NanoFactoryImpl.init().createSingleOverwrite()
		ret.getElement().addAll(0, obj.element)
		ret.setCoordinate(obj.coordinate)
		register(ret)
		ret
	}
	implicit def LineOverwrite2LineOverwriteCC(obj : LineOverwrite) : LineOverwriteCC = LineOverwriteCC(if(obj != null) obj.getDirection() else null, if(obj != null) obj.getCenter() else null, if(obj != null) obj.getLineSelectType() else null, if(obj != null) obj.getCoordinate1() else null, if(obj != null) obj.getCoordinate2() else null, if(obj != null) obj.getStep() else null, if(obj != null) obj.getElement() else null )
	implicit def LineOverwriteCC2LineOverwrite(obj : LineOverwriteCC): LineOverwrite = { 
		val ret : LineOverwrite = NanoFactoryImpl.init().createLineOverwrite()
		ret.getElement().addAll(0, obj.element)
		ret.setStep(obj.step)
		ret.setCoordinate2(obj.coordinate2)
		ret.setCoordinate1(obj.coordinate1)
		ret.setLineSelectType(obj.lineSelectType)
		ret.setCenter(obj.center)
		ret.setDirection(obj.direction)
		register(ret)
		ret
	}
	implicit def RangeOverwrite2RangeOverwriteCC(obj : RangeOverwrite) : RangeOverwriteCC = RangeOverwriteCC(if(obj != null) obj.getCoordinate1() else null, if(obj != null) obj.getCoordinate2() else null, if(obj != null) obj.getElement() else null )
	implicit def RangeOverwriteCC2RangeOverwrite(obj : RangeOverwriteCC): RangeOverwrite = { 
		val ret : RangeOverwrite = NanoFactoryImpl.init().createRangeOverwrite()
		ret.getElement().addAll(0, obj.element)
		ret.setCoordinate2(obj.coordinate2)
		ret.setCoordinate1(obj.coordinate1)
		register(ret)
		ret
	}
	implicit def Element2ElementCC(obj : Element) : ElementCC = ElementCC(if(obj != null) obj.getMaterial2() else null, if(obj != null) obj.getCoordinate() else null, if(obj != null) obj.getMaterial() else null )
	implicit def ElementCC2Element(obj : ElementCC): Element = { 
		val ret : Element = NanoFactoryImpl.init().createElement()
		ret.setMaterial(obj.material)
		ret.setCoordinate(obj.coordinate)
		ret.setMaterial2(obj.material2)
		register(ret)
		ret
	}
	implicit def NewElement2NewElementCC(obj : NewElement) : NewElementCC = NewElementCC(if(obj != null) obj.getMaterial2() else null, if(obj != null) obj.getName() else null, if(obj != null) obj.getLogicoperation() else null, if(obj != null) obj.getElement1() else null, if(obj != null) obj.getElement2() else null )
	implicit def NewElementCC2NewElement(obj : NewElementCC): NewElement = { 
		val ret : NewElement = NanoFactoryImpl.init().createNewElement()
		ret.setElement2(obj.element2)
		ret.setElement1(obj.element1)
		ret.setLogicoperation(obj.logicoperation)
		ret.setName(obj.name)
		ret.setMaterial2(obj.material2)
		register(ret)
		ret
	}
	implicit def Cuboid2CuboidCC(obj : Cuboid) : CuboidCC = CuboidCC(if(obj != null) obj.getMaterial2() else null, if(obj != null) obj.getHeight() else null, if(obj != null) obj.getWidth() else null, if(obj != null) obj.getDepth() else null )
	implicit def CuboidCC2Cuboid(obj : CuboidCC): Cuboid = { 
		val ret : Cuboid = NanoFactoryImpl.init().createCuboid()
		ret.setDepth(obj.depth)
		ret.setWidth(obj.width)
		ret.setHeight(obj.height)
		ret.setMaterial2(obj.material2)
		register(ret)
		ret
	}
	implicit def Cone2ConeCC(obj : Cone) : ConeCC = ConeCC(if(obj != null) obj.getMaterial2() else null, if(obj != null) obj.getRadiusGround() else null, if(obj != null) obj.getRadiusTop() else null, if(obj != null) obj.getHeight() else null )
	implicit def ConeCC2Cone(obj : ConeCC): Cone = { 
		val ret : Cone = NanoFactoryImpl.init().createCone()
		ret.setHeight(obj.height)
		ret.setRadiusTop(obj.radiusTop)
		ret.setRadiusGround(obj.radiusGround)
		ret.setMaterial2(obj.material2)
		register(ret)
		ret
	}
	implicit def Sphere2SphereCC(obj : Sphere) : SphereCC = SphereCC(if(obj != null) obj.getMaterial2() else null, if(obj != null) obj.getRadius() else null )
	implicit def SphereCC2Sphere(obj : SphereCC): Sphere = { 
		val ret : Sphere = NanoFactoryImpl.init().createSphere()
		ret.setRadius(obj.radius)
		ret.setMaterial2(obj.material2)
		register(ret)
		ret
	}
	implicit def Ring2RingCC(obj : Ring) : RingCC = RingCC(if(obj != null) obj.getMaterial2() else null, if(obj != null) obj.getInnerRadius() else null, if(obj != null) obj.getOuterRadius() else null, if(obj != null) obj.getHeight() else null, if(obj != null) obj.getThetaStart() else null, if(obj != null) obj.getThetaStop() else null )
	implicit def RingCC2Ring(obj : RingCC): Ring = { 
		val ret : Ring = NanoFactoryImpl.init().createRing()
		ret.setThetaStop(obj.thetaStop)
		ret.setThetaStart(obj.thetaStart)
		ret.setHeight(obj.height)
		ret.setOuterRadius(obj.outerRadius)
		ret.setInnerRadius(obj.innerRadius)
		ret.setMaterial2(obj.material2)
		register(ret)
		ret
	}
	implicit def Pyramid2PyramidCC(obj : Pyramid) : PyramidCC = PyramidCC(if(obj != null) obj.getMaterial2() else null, if(obj != null) obj.getHeight() else null, if(obj != null) obj.getXSpanBottom() else null, if(obj != null) obj.getYSpanBottom() else null, if(obj != null) obj.getXSpanTop() else null, if(obj != null) obj.getYSpanTop() else null )
	implicit def PyramidCC2Pyramid(obj : PyramidCC): Pyramid = { 
		val ret : Pyramid = NanoFactoryImpl.init().createPyramid()
		ret.setYSpanTop(obj.ySpanTop)
		ret.setXSpanTop(obj.xSpanTop)
		ret.setYSpanBottom(obj.ySpanBottom)
		ret.setXSpanBottom(obj.xSpanBottom)
		ret.setHeight(obj.height)
		ret.setMaterial2(obj.material2)
		register(ret)
		ret
	}
	implicit def SimulationSec2SimulationSecCC(obj : SimulationSec) : SimulationSecCC = SimulationSecCC(if(obj != null) obj.getSimulationTime() else null, if(obj != null) obj.getBgIndex() else null, if(obj != null) obj.getXBoundary() else null, if(obj != null) obj.getYBoundary() else null, if(obj != null) obj.getZBoundary() else null, if(obj != null) obj.getDx() else null, if(obj != null) obj.getDy() else null, if(obj != null) obj.getDz() else null )
	implicit def SimulationSecCC2SimulationSec(obj : SimulationSecCC): SimulationSec = { 
		val ret : SimulationSec = NanoFactoryImpl.init().createSimulationSec()
		ret.setDz(obj.dz)
		ret.setDy(obj.dy)
		ret.setDx(obj.dx)
		ret.setZBoundary(obj.zBoundary)
		ret.setYBoundary(obj.yBoundary)
		ret.setXBoundary(obj.xBoundary)
		ret.setBgIndex(obj.bgIndex)
		ret.setSimulationTime(obj.simulationTime)
		register(ret)
		ret
	}
	implicit def SourcesSec2SourcesSecCC(obj : SourcesSec) : SourcesSecCC = SourcesSecCC(if(obj != null) obj.getDipole() else null )
	implicit def SourcesSecCC2SourcesSec(obj : SourcesSecCC): SourcesSec = { 
		val ret : SourcesSec = NanoFactoryImpl.init().createSourcesSec()
		ret.setDipole(obj.dipole)
		register(ret)
		ret
	}
	implicit def BroadbandSource2BroadbandSourceCC(obj : BroadbandSource) : BroadbandSourceCC = BroadbandSourceCC(if(obj != null) obj.getWavelengthCenter() else null, if(obj != null) obj.getWavelengthSpan() else null )
	implicit def BroadbandSourceCC2BroadbandSource(obj : BroadbandSourceCC): BroadbandSource = { 
		val ret : BroadbandSource = NanoFactoryImpl.init().createBroadbandSource()
		ret.setWavelengthSpan(obj.wavelengthSpan)
		ret.setWavelengthCenter(obj.wavelengthCenter)
		register(ret)
		ret
	}
	implicit def NarrowbandSource2NarrowbandSourceCC(obj : NarrowbandSource) : NarrowbandSourceCC = NarrowbandSourceCC(if(obj != null) obj.getFrequency() else null, if(obj != null) obj.getWavelength() else null, if(obj != null) obj.getPulseLength() else null, if(obj != null) obj.getPulseOffset() else null )
	implicit def NarrowbandSourceCC2NarrowbandSource(obj : NarrowbandSourceCC): NarrowbandSource = { 
		val ret : NarrowbandSource = NanoFactoryImpl.init().createNarrowbandSource()
		ret.setPulseOffset(obj.pulseOffset)
		ret.setPulseLength(obj.pulseLength)
		ret.setWavelength(obj.wavelength)
		ret.setFrequency(obj.frequency)
		register(ret)
		ret
	}
	implicit def Dipole2DipoleCC(obj : Dipole) : DipoleCC = DipoleCC(if(obj != null) obj.getType() else null, if(obj != null) obj.getCoordinate() else null, if(obj != null) obj.getDipoleTheta() else null, if(obj != null) obj.getDipolePhi() else null, if(obj != null) obj.getBroadbandSource() else null, if(obj != null) obj.getNarrowbandSource() else null )
	implicit def DipoleCC2Dipole(obj : DipoleCC): Dipole = { 
		val ret : Dipole = NanoFactoryImpl.init().createDipole()
		ret.setNarrowbandSource(obj.narrowbandSource)
		ret.setBroadbandSource(obj.broadbandSource)
		ret.setDipolePhi(obj.dipolePhi)
		ret.setDipoleTheta(obj.dipoleTheta)
		ret.setCoordinate(obj.coordinate)
		ret.setType(obj.typeValue)
		register(ret)
		ret
	}
	implicit def MonitorsSec2MonitorsSecCC(obj : MonitorsSec) : MonitorsSecCC = MonitorsSecCC(if(obj != null) obj.getMonitors() else null )
	implicit def MonitorsSecCC2MonitorsSec(obj : MonitorsSecCC): MonitorsSec = { 
		val ret : MonitorsSec = NanoFactoryImpl.init().createMonitorsSec()
		ret.getMonitors().addAll(0, obj.monitors)
		register(ret)
		ret
	}
	implicit def Monitor2MonitorCC(obj : Monitor) : MonitorCC = MonitorCC(if(obj != null) obj.getType() else null, if(obj != null) obj.getName() else null, if(obj != null) obj.getCoordinate() else null )
	implicit def MonitorCC2Monitor(obj : MonitorCC): Monitor = { 
		val ret : Monitor = NanoFactoryImpl.init().createMonitor()
		ret.setCoordinate(obj.coordinate)
		ret.setName(obj.name)
		ret.setType(obj.typeValue)
		register(ret)
		ret
	}
	implicit def Calculation2CalculationCC(obj : Calculation) : CalculationCC = CalculationCC(if(obj != null) obj.getOp() else null, if(obj != null) obj.getLeft() else null )
	implicit def CalculationCC2Calculation(obj : CalculationCC): Calculation = { 
		val ret : Calculation = NanoFactoryImpl.init().createCalculation()
		ret.setLeft(obj.left)
		ret.setOp(obj.op)
		register(ret)
		ret
	}
	implicit def NumberLiteral2NumberLiteralCC(obj : NumberLiteral) : NumberLiteralCC = NumberLiteralCC(if(obj != null) obj.getOp() else null, if(obj != null) obj.getValue() else null, if(obj != null) obj.getValue2() else null )
	implicit def NumberLiteralCC2NumberLiteral(obj : NumberLiteralCC): NumberLiteral = { 
		val ret : NumberLiteral = NanoFactoryImpl.init().createNumberLiteral()
		ret.setValue2(obj.value2)
		ret.setValue(obj.value)
		ret.setOp(obj.op)
		register(ret)
		ret
	}
	implicit def Binary2BinaryCC(obj : Binary) : BinaryCC = BinaryCC(if(obj != null) obj.getOp() else null, if(obj != null) obj.getRight() else null )
	implicit def BinaryCC2Binary(obj : BinaryCC): Binary = { 
		val ret : Binary = NanoFactoryImpl.init().createBinary()
		ret.setRight(obj.right)
		ret.setOp(obj.op)
		register(ret)
		ret
	}
	implicit def Parenthesis2ParenthesisCC(obj : Parenthesis) : ParenthesisCC = ParenthesisCC(if(obj != null) obj.getOp() else null, if(obj != null) obj.getExpression() else null )
	implicit def ParenthesisCC2Parenthesis(obj : ParenthesisCC): Parenthesis = { 
		val ret : Parenthesis = NanoFactoryImpl.init().createParenthesis()
		ret.setExpression(obj.expression)
		ret.setOp(obj.op)
		register(ret)
		ret
	}
}

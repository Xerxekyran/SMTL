package de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.nanostructure

import de.digitalforger.smtl.core._
import de.digitalforger.smtl.m2m.TransformationHelperSyntax._
import java.lang.String
import java.math.BigDecimal
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.StructuralElement
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.Material
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.NamedElement
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.Group
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.DispersionMaterial
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.Sphere
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.Pyramid
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.Cuboid
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.Ring
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.Cylinder
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.Cone
import de.huberlin.nanoworkbench2.nanostructure.NanoStructure.impl.NanoStructureFactoryImpl

case class NamedElementCC (name : String) extends IGeneratedCC
case class GroupCC (name : String, elements : org.eclipse.emf.common.util.EList[StructuralElement] ) extends IGeneratedCC
case class MaterialCC (name : String, refractionIndex : Double) extends IGeneratedCC
case class DispersionMaterialCC (name : String, refractionIndex : Double) extends IGeneratedCC
case class StructuralElementCC (name : String, xPos : Double, yPos : Double, zPos : Double, material : MaterialCC ) extends IGeneratedCC
case class SphereCC (name : String, xPos : Double, yPos : Double, zPos : Double, radius : BigDecimal) extends IGeneratedCC
case class PyramidCC (name : String, xPos : Double, yPos : Double, zPos : Double, height : Double, xSpanBottom : Double, ySpanBottom : Double, xSpanTop : Double, ySpanTop : Double) extends IGeneratedCC
case class CuboidCC (name : String, xPos : Double, yPos : Double, zPos : Double, width : Double, height : Double, depth : Double) extends IGeneratedCC
case class RingCC (name : String, xPos : Double, yPos : Double, zPos : Double, innerRadius : Double, outerRadius : Double, thetaStart : Double, thetaStop : Double) extends IGeneratedCC
case class CylinderCC (name : String, xPos : Double, yPos : Double, zPos : Double, radius : Double, height : Double) extends IGeneratedCC
case class ConeCC (name : String, xPos : Double, yPos : Double, zPos : Double, topRadius : Double, bottomRadius : Double, height : Double) extends IGeneratedCC

object NanoStructureCC{
	def let(obj: IGeneratedCC) = obj

	implicit def NamedElement2NamedElementCC(obj : NamedElement) : NamedElementCC = NamedElementCC(if(obj != null) obj.getName() else null )
	implicit def NamedElementCC2NamedElement(obj : NamedElementCC): NamedElement = { 
		val ret : NamedElement = NanoStructureFactoryImpl.init().createNamedElement()
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Group2GroupCC(obj : Group) : GroupCC = GroupCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getElements() else null )
	implicit def GroupCC2Group(obj : GroupCC): Group = { 
		val ret : Group = NanoStructureFactoryImpl.init().createGroup()
		ret.getElements().addAll(0, obj.elements)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Material2MaterialCC(obj : Material) : MaterialCC = MaterialCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getRefractionIndex() else 0.0 )
	implicit def MaterialCC2Material(obj : MaterialCC): Material = { 
		val ret : Material = NanoStructureFactoryImpl.init().createMaterial()
		ret.setRefractionIndex(obj.refractionIndex)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def DispersionMaterial2DispersionMaterialCC(obj : DispersionMaterial) : DispersionMaterialCC = DispersionMaterialCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getRefractionIndex() else 0.0 )
	implicit def DispersionMaterialCC2DispersionMaterial(obj : DispersionMaterialCC): DispersionMaterial = { 
		val ret : DispersionMaterial = NanoStructureFactoryImpl.init().createDispersionMaterial()
		ret.setRefractionIndex(obj.refractionIndex)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def StructuralElement2StructuralElementCC(obj : StructuralElement) : StructuralElementCC = StructuralElementCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getXPos() else 0.0, if(obj != null) obj.getYPos() else 0.0, if(obj != null) obj.getZPos() else 0.0, if(obj != null) obj.getMaterial() else null )
	implicit def StructuralElementCC2StructuralElement(obj : StructuralElementCC): StructuralElement = { 
		val ret : StructuralElement = NanoStructureFactoryImpl.init().createStructuralElement()
		ret.setMaterial(obj.material)
		ret.setZPos(obj.zPos)
		ret.setYPos(obj.yPos)
		ret.setXPos(obj.xPos)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Sphere2SphereCC(obj : Sphere) : SphereCC = SphereCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getXPos() else 0.0, if(obj != null) obj.getYPos() else 0.0, if(obj != null) obj.getZPos() else 0.0, if(obj != null) obj.getRadius() else null )
	implicit def SphereCC2Sphere(obj : SphereCC): Sphere = { 
		val ret : Sphere = NanoStructureFactoryImpl.init().createSphere()
		ret.setRadius(obj.radius)
		ret.setZPos(obj.zPos)
		ret.setYPos(obj.yPos)
		ret.setXPos(obj.xPos)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Pyramid2PyramidCC(obj : Pyramid) : PyramidCC = PyramidCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getXPos() else 0.0, if(obj != null) obj.getYPos() else 0.0, if(obj != null) obj.getZPos() else 0.0, if(obj != null) obj.getHeight() else 0.0, if(obj != null) obj.getXSpanBottom() else 0.0, if(obj != null) obj.getYSpanBottom() else 0.0, if(obj != null) obj.getXSpanTop() else 0.0, if(obj != null) obj.getYSpanTop() else 0.0 )
	implicit def PyramidCC2Pyramid(obj : PyramidCC): Pyramid = { 
		val ret : Pyramid = NanoStructureFactoryImpl.init().createPyramid()
		ret.setYSpanTop(obj.ySpanTop)
		ret.setXSpanTop(obj.xSpanTop)
		ret.setYSpanBottom(obj.ySpanBottom)
		ret.setXSpanBottom(obj.xSpanBottom)
		ret.setHeight(obj.height)
		ret.setZPos(obj.zPos)
		ret.setYPos(obj.yPos)
		ret.setXPos(obj.xPos)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Cuboid2CuboidCC(obj : Cuboid) : CuboidCC = CuboidCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getXPos() else 0.0, if(obj != null) obj.getYPos() else 0.0, if(obj != null) obj.getZPos() else 0.0, if(obj != null) obj.getWidth() else 0.0, if(obj != null) obj.getHeight() else 0.0, if(obj != null) obj.getDepth() else 0.0 )
	implicit def CuboidCC2Cuboid(obj : CuboidCC): Cuboid = { 
		val ret : Cuboid = NanoStructureFactoryImpl.init().createCuboid()
		ret.setDepth(obj.depth)
		ret.setHeight(obj.height)
		ret.setWidth(obj.width)
		ret.setZPos(obj.zPos)
		ret.setYPos(obj.yPos)
		ret.setXPos(obj.xPos)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Ring2RingCC(obj : Ring) : RingCC = RingCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getXPos() else 0.0, if(obj != null) obj.getYPos() else 0.0, if(obj != null) obj.getZPos() else 0.0, if(obj != null) obj.getInnerRadius() else 0.0, if(obj != null) obj.getOuterRadius() else 0.0, if(obj != null) obj.getThetaStart() else 0.0, if(obj != null) obj.getThetaStop() else 0.0 )
	implicit def RingCC2Ring(obj : RingCC): Ring = { 
		val ret : Ring = NanoStructureFactoryImpl.init().createRing()
		ret.setThetaStop(obj.thetaStop)
		ret.setThetaStart(obj.thetaStart)
		ret.setOuterRadius(obj.outerRadius)
		ret.setInnerRadius(obj.innerRadius)
		ret.setZPos(obj.zPos)
		ret.setYPos(obj.yPos)
		ret.setXPos(obj.xPos)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Cylinder2CylinderCC(obj : Cylinder) : CylinderCC = CylinderCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getXPos() else 0.0, if(obj != null) obj.getYPos() else 0.0, if(obj != null) obj.getZPos() else 0.0, if(obj != null) obj.getRadius() else 0.0, if(obj != null) obj.getHeight() else 0.0 )
	implicit def CylinderCC2Cylinder(obj : CylinderCC): Cylinder = { 
		val ret : Cylinder = NanoStructureFactoryImpl.init().createCylinder()
		ret.setHeight(obj.height)
		ret.setRadius(obj.radius)
		ret.setZPos(obj.zPos)
		ret.setYPos(obj.yPos)
		ret.setXPos(obj.xPos)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Cone2ConeCC(obj : Cone) : ConeCC = ConeCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getXPos() else 0.0, if(obj != null) obj.getYPos() else 0.0, if(obj != null) obj.getZPos() else 0.0, if(obj != null) obj.getTopRadius() else 0.0, if(obj != null) obj.getBottomRadius() else 0.0, if(obj != null) obj.getHeight() else 0.0 )
	implicit def ConeCC2Cone(obj : ConeCC): Cone = { 
		val ret : Cone = NanoStructureFactoryImpl.init().createCone()
		ret.setHeight(obj.height)
		ret.setBottomRadius(obj.bottomRadius)
		ret.setTopRadius(obj.topRadius)
		ret.setZPos(obj.zPos)
		ret.setYPos(obj.yPos)
		ret.setXPos(obj.xPos)
		ret.setName(obj.name)
		register(ret)
		ret
	}
}

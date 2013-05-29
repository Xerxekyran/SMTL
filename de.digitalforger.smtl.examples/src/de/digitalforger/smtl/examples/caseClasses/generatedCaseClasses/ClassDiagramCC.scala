package de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses

import de.digitalforger.smtl.core._
import de.digitalforger.smtl.m2m.TransformationHelperSyntax._
import java.lang.String
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.Attribute
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.Method
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.Classifier
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.Class
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.Parameter
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.Package
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.NamedElement
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.Model
import de.digitalforger.examples.model.SimpleClassDiagram.SimpleClassDiagram.impl.SimpleClassDiagramFactoryImpl

case class ClassCC (name : String, attributes : org.eclipse.emf.common.util.EList[Attribute], methods : org.eclipse.emf.common.util.EList[Method] ) extends IGeneratedCC
case class AttributeCC (name : String, visible : Boolean, changeable : Boolean, value : String, typeValue : ClassifierCC, owner : ClassCC ) extends IGeneratedCC
case class MethodCC (name : String, returnValue : ClassifierCC, parameters : org.eclipse.emf.common.util.EList[Parameter], owner : ClassCC ) extends IGeneratedCC
case class ClassifierCC (name : String, instanceClassName : String) extends IGeneratedCC
case class NamedElementCC (name : String) extends IGeneratedCC
case class PackageCC (name : String, containingClasses : org.eclipse.emf.common.util.EList[Class] ) extends IGeneratedCC
case class ModelCC (packages : org.eclipse.emf.common.util.EList[Package], dataTypes : org.eclipse.emf.common.util.EList[Classifier] ) extends IGeneratedCC
case class ParameterCC (name : String, typeValue : ClassifierCC ) extends IGeneratedCC

object SimpleClassCC{
	def let(obj: IGeneratedCC) = obj

	implicit def Class2ClassCC(obj : Class) : ClassCC = ClassCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getAttributes() else null, if(obj != null) obj.getMethods() else null )
	implicit def ClassCC2Class(obj : ClassCC): Class = { 
		val ret : Class = SimpleClassDiagramFactoryImpl.init().createClass()
		ret.getMethods().addAll(0, obj.methods)
		ret.getAttributes().addAll(0, obj.attributes)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Attribute2AttributeCC(obj : Attribute) : AttributeCC = AttributeCC(if(obj != null) obj.getName() else null, obj.isVisible(), obj.isChangeable(), if(obj != null) obj.getValue() else null, if(obj != null) obj.getType() else null, if(obj != null) obj.getOwner() else null )
	implicit def AttributeCC2Attribute(obj : AttributeCC): Attribute = { 
		val ret : Attribute = SimpleClassDiagramFactoryImpl.init().createAttribute()
		ret.setOwner(obj.owner)
		ret.setType(obj.typeValue)
		ret.setValue(obj.value)
		ret.setChangeable(obj.changeable)
		ret.setVisible(obj.visible)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Method2MethodCC(obj : Method) : MethodCC = MethodCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getReturn() else null, if(obj != null) obj.getParameters() else null, if(obj != null) obj.getOwner() else null )
	implicit def MethodCC2Method(obj : MethodCC): Method = { 
		val ret : Method = SimpleClassDiagramFactoryImpl.init().createMethod()
		ret.setOwner(obj.owner)
		ret.getParameters().addAll(0, obj.parameters)
		ret.setReturn(obj.returnValue)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Classifier2ClassifierCC(obj : Classifier) : ClassifierCC = ClassifierCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getInstanceClassName() else null )
	implicit def ClassifierCC2Classifier(obj : ClassifierCC): Classifier = { 
		val ret : Classifier = SimpleClassDiagramFactoryImpl.init().createClassifier()
		ret.setInstanceClassName(obj.instanceClassName)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def NamedElement2NamedElementCC(obj : NamedElement) : NamedElementCC = NamedElementCC(if(obj != null) obj.getName() else null )
	implicit def NamedElementCC2NamedElement(obj : NamedElementCC): NamedElement = { 
		val ret : NamedElement = SimpleClassDiagramFactoryImpl.init().createNamedElement()
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Package2PackageCC(obj : Package) : PackageCC = PackageCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getContainingClasses() else null )
	implicit def PackageCC2Package(obj : PackageCC): Package = { 
		val ret : Package = SimpleClassDiagramFactoryImpl.init().createPackage()
		ret.getContainingClasses().addAll(0, obj.containingClasses)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Model2ModelCC(obj : Model) : ModelCC = ModelCC(if(obj != null) obj.getPackages() else null, if(obj != null) obj.getDataTypes() else null )
	implicit def ModelCC2Model(obj : ModelCC): Model = { 
		val ret : Model = SimpleClassDiagramFactoryImpl.init().createModel()
		ret.getDataTypes().addAll(0, obj.dataTypes)
		ret.getPackages().addAll(0, obj.packages)
		register(ret)
		ret
	}
	implicit def Parameter2ParameterCC(obj : Parameter) : ParameterCC = ParameterCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getType() else null )
	implicit def ParameterCC2Parameter(obj : ParameterCC): Parameter = { 
		val ret : Parameter = SimpleClassDiagramFactoryImpl.init().createParameter()
		ret.setType(obj.typeValue)
		ret.setName(obj.name)
		register(ret)
		ret
	}
}

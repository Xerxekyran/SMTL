package de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.classExample

import de.digitalforger.smtl.core._
import de.digitalforger.smtl.m2m.TransformationHelperSyntax._
import java.lang.String
import de.digitalforger.examples.model.Class.Class.Attribute
import de.digitalforger.examples.model.Class.Class.Classifier
import de.digitalforger.examples.model.Class.Class.Class
import de.digitalforger.examples.model.Class.Class.NameElt
import de.digitalforger.examples.model.Class.Class.DataType
import de.digitalforger.examples.model.Class.Class.Package
import de.digitalforger.examples.model.Class.Class.impl.ClassFactoryImpl

case class NameEltCC (name : String) extends IGeneratedCC
case class ClassifierCC (name : String) extends IGeneratedCC
case class DataTypeCC (name : String) extends IGeneratedCC
case class ClassCC (name : String, isAbstract : Boolean, atts : org.eclipse.emf.common.util.EList[Attribute] ) extends IGeneratedCC
case class AttributeCC (name : String, multivalued : Boolean, typeValue : ClassifierCC, owner : ClassCC ) extends IGeneratedCC
case class PackageCC (content : org.eclipse.emf.common.util.EList[NameElt] ) extends IGeneratedCC

object ClassCC{
	def let(obj: IGeneratedCC) = obj

	implicit def NameElt2NameEltCC(obj : NameElt) : NameEltCC = NameEltCC(if(obj != null) obj.getName() else null )
	implicit def NameEltCC2NameElt(obj : NameEltCC): NameElt = { 
		val ret : NameElt = ClassFactoryImpl.init().createNameElt()
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Classifier2ClassifierCC(obj : Classifier) : ClassifierCC = ClassifierCC(if(obj != null) obj.getName() else null )
	implicit def ClassifierCC2Classifier(obj : ClassifierCC): Classifier = { 
		val ret : Classifier = ClassFactoryImpl.init().createClassifier()
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def DataType2DataTypeCC(obj : DataType) : DataTypeCC = DataTypeCC(if(obj != null) obj.getName() else null )
	implicit def DataTypeCC2DataType(obj : DataTypeCC): DataType = { 
		val ret : DataType = ClassFactoryImpl.init().createDataType()
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Class2ClassCC(obj : Class) : ClassCC = ClassCC(if(obj != null) obj.getName() else null, obj.isIsAbstract(), if(obj != null) obj.getAtts() else null )
	implicit def ClassCC2Class(obj : ClassCC): Class = { 
		val ret : Class = ClassFactoryImpl.init().createClass()
		ret.getAtts().addAll(0, obj.atts)
		ret.setIsAbstract(obj.isAbstract)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Attribute2AttributeCC(obj : Attribute) : AttributeCC = AttributeCC(if(obj != null) obj.getName() else null, obj.isMultivalued(), if(obj != null) obj.getType() else null, if(obj != null) obj.getOwner() else null )
	implicit def AttributeCC2Attribute(obj : AttributeCC): Attribute = { 
		val ret : Attribute = ClassFactoryImpl.init().createAttribute()
		ret.setOwner(obj.owner)
		ret.setType(obj.typeValue)
		ret.setMultivalued(obj.multivalued)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Package2PackageCC(obj : Package) : PackageCC = PackageCC(if(obj != null) obj.getContent() else null )
	implicit def PackageCC2Package(obj : PackageCC): Package = { 
		val ret : Package = ClassFactoryImpl.init().createPackage()
		ret.getContent().addAll(0, obj.content)
		register(ret)
		ret
	}
}

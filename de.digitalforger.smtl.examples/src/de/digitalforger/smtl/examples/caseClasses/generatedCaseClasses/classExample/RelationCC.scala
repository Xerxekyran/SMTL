package de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.classExample

import de.digitalforger.smtl.core._
import de.digitalforger.smtl.m2m.TransformationHelperSyntax._
import java.lang.String
import de.digitalforger.examples.model.Relation.Relation.Column
import de.digitalforger.examples.model.Relation.Relation.Type
import de.digitalforger.examples.model.Relation.Relation.Table
import de.digitalforger.examples.model.Relation.Relation.Named
import de.digitalforger.examples.model.Relation.Relation.impl.RelationFactoryImpl

case class NamedCC (name : String) extends IGeneratedCC
case class TableCC (name : String, cols : org.eclipse.emf.common.util.EList[Column], keys : org.eclipse.emf.common.util.EList[Column] ) extends IGeneratedCC
case class ColumnCC (name : String, typeValue : TypeCC, keyOf : org.eclipse.emf.common.util.EList[Table], owner : TableCC ) extends IGeneratedCC
case class TypeCC (name : String) extends IGeneratedCC

object RelationCC{
	def let(obj: IGeneratedCC) = obj

	implicit def Named2NamedCC(obj : Named) : NamedCC = NamedCC(if(obj != null) obj.getName() else null )
	implicit def NamedCC2Named(obj : NamedCC): Named = { 
		val ret : Named = RelationFactoryImpl.init().createNamed()
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Table2TableCC(obj : Table) : TableCC = TableCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getCols() else null, if(obj != null) obj.getKeys() else null )
	implicit def TableCC2Table(obj : TableCC): Table = { 
		val ret : Table = RelationFactoryImpl.init().createTable()
		ret.getKeys().addAll(0, obj.keys)
		ret.getCols().addAll(0, obj.cols)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Column2ColumnCC(obj : Column) : ColumnCC = ColumnCC(if(obj != null) obj.getName() else null, if(obj != null) obj.getType() else null, if(obj != null) obj.getKeyOf() else null, if(obj != null) obj.getOwner() else null )
	implicit def ColumnCC2Column(obj : ColumnCC): Column = { 
		val ret : Column = RelationFactoryImpl.init().createColumn()
		ret.setOwner(obj.owner)
		ret.getKeyOf().addAll(0, obj.keyOf)
		ret.setType(obj.typeValue)
		ret.setName(obj.name)
		register(ret)
		ret
	}
	implicit def Type2TypeCC(obj : Type) : TypeCC = TypeCC(if(obj != null) obj.getName() else null )
	implicit def TypeCC2Type(obj : TypeCC): Type = { 
		val ret : Type = RelationFactoryImpl.init().createType()
		ret.setName(obj.name)
		register(ret)
		ret
	}
}

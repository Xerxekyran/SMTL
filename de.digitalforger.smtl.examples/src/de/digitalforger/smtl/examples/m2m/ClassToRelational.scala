package de.digitalforger.smtl.examples.m2m

import java.lang.System
import de.digitalforger.smtl.m2m.TransformationHelperSyntax._
import de.digitalforger.smtl.m2m.Rule
import de.digitalforger.smtl.m2m.TransformationM2M
import de.digitalforger.examples.model.Class.Class.ClassPackage
import de.digitalforger.examples.model.Class.Class.Class
import de.digitalforger.examples.model.Class.Class.DataType
import de.digitalforger.examples.model.Class.Class.Attribute
import de.digitalforger.examples.model.Relation.Relation.RelationPackage
import de.digitalforger.examples.model.Relation.Relation.Table
import de.digitalforger.examples.model.Relation.Relation.Column
import de.digitalforger.examples.model.Relation.Relation.Type
import de.digitalforger.smtl.examples.caseClasses.generatedCaseClasses.classExample.TypeCC

/**
 * Example transformation for SMTL.
 * Class models are transformed into Relational models.
 *
 * @author Lars George
 */
object ClassToRelational {

  def main(args: Array[String]): Unit = {

    // init the meta models
    ClassPackage.eINSTANCE
    RelationPackage.eINSTANCE

    val inputFile = "file:../de.digitalforger.smtl.examples.model/resources/Class2Relational/inClassSample.xmi"
    val classMetaModel = "http://www.digitalforger.de/examples/model/Class"
    val relationalMetaModel = "http://www.digitalforger.de/examples/model/Relation"

    // create the transformation
    val transform1 = new TransformationM2M("Class to Relational") from classMetaModel to relationalMetaModel

    // helper to get the type of the obejct ids
    def objectIdType(): Type = {
      val dt = getInputElements[DataType].filter((e) => {e.asInstanceOf[DataType].getName().equals("Integer")}).head.asInstanceOf[DataType]

      as[Type](dt)
    }

    // class to table
    implicit val classToTable = new Rule[Class, Table]("classToTable") perform (
      (cls, tbl) => {
        tbl.setName(cls.getName)

        var key = create[Column]
        key.setName("objectId")
        key.setOwner(tbl)
        key.setType(objectIdType)

        tbl.getKeys.add(key)
      })

    // datatype to type
    implicit val dataType2Type = new Rule[DataType, Type]("dataType2Type") perform (
      (dt, t) => {
        t.setName(dt.getName)
      })

    // singleValDTAttribute to Column
    val singleValDTAttribute2Column = new Rule[Attribute, Column]("singleValDTAttribute2Column") when ((attr) => {
      attr.getType.isInstanceOf[DataType] && !attr.isMultivalued
    }) perform (
      (attr, col) => {
        col.setName(attr.getName)
        col.setType(attr.getType.asInstanceOf[DataType])
        col.setOwner(attr.getOwner)
      })

    // multiValDTAttribute to Column
    val multiValDTAttribute2Column = new Rule[Attribute, Table]("multiValDTAttribute2Column") when ((attr) => {
      attr.getType.isInstanceOf[DataType] && attr.isMultivalued
    }) perform (
      (attr, tbl) => {
        tbl.setName(attr.getOwner.getName + "_" + attr.getName)

        var id = create[Column]
        id.setName(attr.getOwner.getName + "Id")
        id.setType(objectIdType)

        var value = create[Column]
        value.setName(attr.getName)
        value.setType(attr.getType.asInstanceOf[DataType])

        tbl.getCols.add(id)
        tbl.getCols.add(value)
      })

    //classAttribute2Column
    val classAttribute2Column = new Rule[Attribute, Column]("classAttribute2Column") when ((attr) => {
      attr.getType.isInstanceOf[Class] && !attr.isMultivalued
    }) perform (
      (attr, col) => {
        col.setName(attr.getName + "Id")
        col.setType(objectIdType)
        col.setOwner(attr.getOwner)
      })

    //classAttribute2Column
    val multiValClassAttribute2Column = new Rule[Attribute, Table]("multiValClassAttribute2Column") when ((attr) => {
      attr.getType.isInstanceOf[Class] && attr.isMultivalued
    }) perform (
      (attr, tbl) => {
        var id = create[Column]
        id.setName(attr.getOwner.getName + "Id")
        id.setType(objectIdType)

        var foreignKey = create[Column]
        foreignKey.setName(attr.getName + "Id")
        foreignKey.setType(objectIdType)

        tbl.setName(attr.getOwner.getName + "_" + attr.getName)
        tbl.getCols.add(id)
        tbl.getCols.add(foreignKey)
      })

    // add the rules    
    transform1.addRule(classToTable, dataType2Type, singleValDTAttribute2Column, multiValDTAttribute2Column, classAttribute2Column, multiValClassAttribute2Column)

    // do the transformation work
    transform1.setShowDirectTrace(true)
    var startTime = System.nanoTime()
    transform1 transform inputFile
    println("transformation took: " + (System.nanoTime() - startTime) * Math.pow(10, -9) + " seconds")

    transform1.export("../de.digitalforger.smtl.examples.model/resources/Class2Relational/outRelationSample.xmi")
  }

}


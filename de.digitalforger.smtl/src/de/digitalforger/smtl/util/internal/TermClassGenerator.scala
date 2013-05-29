package de.digitalforger.smtl.util.internal

import java.util.LinkedList
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.HashMap
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EDataType
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.EStructuralFeature
import de.digitalforger.smtl.logging.LogHelper
import de.digitalforger.smtl.m2t.IM2TGenerator
import org.eclipse.emf.ecore.impl.EEnumImpl
import org.eclipse.emf.ecore.EcorePackage
import org.eclipse.emf.ecore.impl.EClassImpl
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.impl.EReferenceImpl

/**
 * A model to text generator that generates case classes with implicits (model -> case class and clase class -> model).
 * Input is an ecore resource.
 * The Output will be written to a file with the given destination path
 *
 * @author Lars George
 */
class TermCaseClassGenerator(destination: String, pkgName: String, objName: String, referenceBasePackage: String) extends IM2TGenerator {
  /**
   * an enumeration of the parameter types for a case class parameter
   */
  object ParameterType extends Enumeration with LogHelper {
    type ParameterType = Value
    val SingleValue, SingleBooleanValue, ListValue = Value
  }
  import ParameterType._

  /**
   * list of datatypes that should be handled as primitive datatypes
   */
  val primitiveTypes = List("Integer", "String", "Float", "Boolean", "Double")

  /**
   * this map is for checking that imports are not generated multiple times
   */
  private val importedClasses = new HashMap[String, Boolean]

  /**
   * the ePackageName of the given model will be saved here
   */
  private var ePackageName = "";

  /**
   * The class name of the root element type
   */
  private var domainRootType = "";

  /**
   *  paramList maps EClassifiers to a list of parameters (the tuple values are parameter names,  the parameter type and the parameter name)
   */
  private var paramList = new HashMap[EClassifier, LinkedList[(String, ParameterType, String)]]

  /**
   * This map will be filled at the beginning to let the transformation know which Classifiers exist.
   * It helps to decide at certain points during the transformation process.
   */
  private var existingClassifiers = new HashMap[EClassifier, Boolean]

  /**
   * start point of the M2T transformation
   */
  override def generate(resource: Resource): Unit = {
    // lookup the package name
    ePackageName = resource.getContents().get(0).asInstanceOf[EPackage].getName()

    // check for all classifiers that will be transformed to case classes
    resource.foreach(classOf[EClassifier], lookupAllClassifiers)

    // exclude certain types for the import statements
    excludeSpecialTypesFromImport()

    // create parameter lists
    resource.foreach(classOf[EClassifier], caseClassForClassifier)

    // do the text generation
    this createFile destination withContent START +
      "package " + pkgName + << +
      << +
      "import metalenses.Traces._" + << +
      "import metalenses.tlp.ms.THArrays.{THArrayNil => HNil, THArray => HList}" + << +
      "import metalenses.tlp.ms.THArrays._" + << +
      "import metalenses.tlp.ms.TTLists.{TTNil => TNil, TTList => TList}" + << +
      "import metalenses.tlp.ms.TTLists._" + << +
      "import metalenses.tlp.ms.Nats._" + << +
      "import metalenses.tlp.ms.Utils._" + << +
      "import metalenses.tlp.ms.Booleans._" + << +
      "import metalenses.tlp.ms.Comparables._" + << +
      "import metalenses.Term._" + << +
      "import metalenses.Lenses._" + << +
      "import scala.collection.mutable.Map" + << +
      "import scala.collection.mutable.ListBuffer" + << +
      "import org.eclipse.emf.ecore.EObject" + << +
      resource.foreach(classOf[EAttribute], getImports) +
      resource.foreach(classOf[EReference], getImportsOfReferences) +
      resource.foreach(classOf[EClassifier], getImportsOfClassifier) +
      getFactoryImport() +
      << +
      //resource.foreach(classOf[EClassifier], caseClassForClassifier) +
      << +
      "object " + objName + "{" + << +
      "\tdef let(obj: IGeneratedCC) = obj" + << +
      << +
      resource.foreach(classOf[EClassifier], createImplicit) +
      "}" + <<
  }

  /**
   * fills the 'existingClassifiers' map with the available data
   */
  def lookupAllClassifiers(obj: EClassifier): String = {
    existingClassifiers.get(obj) match {
      case Some(x) => ""
      case None =>
        if (obj.eContainer().eContainer() == null) {
          domainRootType = obj.getName()
        }
        existingClassifiers += obj -> true
    }

    ""
  }

  /**
   * returns the needed import for the factory methods of the model objects
   */
  def getFactoryImport(): String = {
    val importFactoryName = referenceBasePackage + "." + ePackageName + ".impl." + firstToUpper(ePackageName) + "FactoryImpl";
    // check if we already imported that type
    importedClasses.get(importFactoryName) match {
      case Some(x) => ""
      case None =>
        importedClasses += importFactoryName -> true
        START + "import " + importFactoryName + <<
    }
  }

  /**
   * returns the needed import for an attribute
   */
  def getImports(attr: EAttribute): String = {

    // check if we already imported that type
    importedClasses.get(attr.getEAttributeType().getInstanceClassName()) match {
      case Some(x) => ""
      case None =>
        if (attr.getEAttributeType().getInstanceClassName() == null) ""
        else {
          importedClasses += attr.getEAttributeType().getInstanceClassName() -> true
          START + "import " + attr.getEAttributeType().getInstanceClassName() + <<
        }
    }
  }

  /**
   * returns the needed imports for reference objects
   */
  def getImportsOfReferences(ref: EReference): String = {

    // check if we already imported that type
    importedClasses.get(ref.getEType().getName()) match {
      case Some(x) => ""
      case None =>
        if (ref.getEType().getName() == null) ""
        else {
          importedClasses += ref.getEType().getName() -> true
          START + "import " + referenceBasePackage + "." + ePackageName + "." + ref.getEType().getName() + <<
        }

    }
  }

  /**
   * returns the needed imports for classifier objects
   */
  def getImportsOfClassifier(obj: EClassifier): String = {
    // check if we already imported that type
    importedClasses.get(obj.getName()) match {
      case Some(x) => ""
      case None =>
        importedClasses += obj.getName() -> true
        START + "import " + referenceBasePackage + "." + ePackageName + "." + obj.getName() + <<
    }
  }

  /**
   * Generates a case class for an EClassifier
   */
  def caseClassForClassifier(obj: EClassifier): String = {

    // enums and abstract classes will not be converted
    if (obj.isInstanceOf[EEnumImpl] || (obj.isInstanceOf[EClassImpl] && obj.asInstanceOf[EClassImpl].eIsSet(EcorePackage.ECLASS__ABSTRACT)))
      return ""

    val a: EClassImpl = null

    // all attributes of this class
    var attributes = "" //obj.foreach(classOf[EAttribute], getAttribute, ", ", true)

    // all derived attributes
    var derivedAttrs: EList[EAttribute] = obj.asInstanceOf[EClassImpl].eGet(EcorePackage.ECLASS__EALL_ATTRIBUTES, true, true).asInstanceOf[EList[EAttribute]]
    for (attr <- derivedAttrs) {
      attributes = attributes.concat(getAttribute(attr) + ", ")

      // save the parameter
      if (!obj.eContents().contains(attr))
        saveToParameterList(obj, firstToUpper(attr.getName()), getParamType(attr), convertStandardValueTypes(attr.getEAttributeType()))
    }

    if (attributes.length() >= 2)
      attributes = attributes.substring(0, attributes.length() - 2)

    // all reference attributes
    val referenceAttributes = obj.foreach(classOf[EReference], getReferenceAttribute, ", ")
    if (referenceAttributes.length() > 0 && attributes.length > 0) attributes += ", "

    START + "case class " + obj.getName() + "CC (" + attributes +
      referenceAttributes + ") extends IGeneratedCC" + <<
  }

  /**
   * helper method to define the param type of a given parameter
   */
  private def getParamType(param: EAttribute): ParameterType = {
    var paramTypeName: String = convertStandardValueTypes(param.getEAttributeType())
    var isCollection = false;

    if (paramTypeName == null)
      logger.error("Attribute type is null: " + param)

    // is it a collection of?
    if (param.getUpperBound() == -1 || (param.getUpperBound() - param.getLowerBound() > 1)) {
      //paramTypeName = "org.eclipse.emf.common.util.EList[" + paramTypeName + "]"      
      var isCollection = true;
    }

    // what kind of a parameter do we have?
    var paramType: ParameterType = ParameterType.SingleValue
    if (isCollection) paramType = ParameterType.ListValue
    if (paramTypeName != null && paramTypeName.equals("Boolean")) paramType = ParameterType.SingleBooleanValue

    paramType
  }

  /**
   * helper method that returns the string version of an EAttribute for a parameter list
   */
  private def getAttribute(param: EAttribute): String = {
    var paramTypeName: String = convertStandardValueTypes(param.getEAttributeType())
    var isCollection = false;

    if (paramTypeName == null)
      logger.error("Attribute type is null: " + param)

    // is it a collection of?
    if (param.getUpperBound() == -1 || (param.getUpperBound() - param.getLowerBound() > 1)) {
      // paramTypeName = "org.eclipse.emf.common.util.EList[" + paramTypeName + "]"
      var isCollection = false;
    }

    // what kind of a parameter do we have?
    var paramType: ParameterType = ParameterType.SingleValue
    if (isCollection) paramType = ParameterType.ListValue
    if (paramTypeName != null && paramTypeName.equals("Boolean")) paramType = ParameterType.SingleBooleanValue

    // write the param type to list of params of the containing EClassifier    
    saveToParameterList(param.getEContainingClass().asInstanceOf[EClassifier], firstToUpper(param.getName()), paramType, paramTypeName)

    START + this.getSafeName(param.getName()) + " : " + paramTypeName
  }

  /**
   * helper method that returns returns the string version of an EReference for a parameter list
   */
  private def getReferenceAttribute(ref: EReference): String = {

    var isCollection = (ref.getUpperBound() == -1 || (ref.getUpperBound() - ref.getLowerBound() > 1));
    var paramTypeName: String = null;

    // is it a collection of?
    if (isCollection) {
      // paramTypeName = "org.eclipse.emf.common.util.EList[" + ref.getEType().getName() + "]"
      paramTypeName = ref.getEType().getName()
    } else {
      // if the paramType belongs to a classifier a case class is generated for, use the CC type           
      if (ref.getEType().getName() != null) {
        paramTypeName = this.existingClassifiers.get(ref.getEContainingClass().asInstanceOf[EClassifier]) match {
          case Some(x) => ref.getEType().getName()
          case None => ref.getEType().getName()
        }
      } else {
        // if the eType is null try it with the reference name as type
        logger.error("Reference type is null: " + ref)
      }
    }

    // what kind of a parameter do we have?
    var paramType: ParameterType = ParameterType.SingleValue
    if (isCollection) paramType = ParameterType.ListValue
    if (paramTypeName.equals("Boolean")) paramType = ParameterType.SingleBooleanValue

    // write the param type to list of params of the containing EClassifier    
    saveToParameterList(ref.getEContainingClass().asInstanceOf[EClassifier], firstToUpper(ref.getName()), paramType, paramTypeName)

    START + getSafeName(ref.getName()) + " : " + paramTypeName
  }

  /**
   * helper method to save the generated parameters for each classifier
   */
  private def saveToParameterList(classifier: EClassifier, getterName: String, paramType: ParameterType, paramTypeName: String): Unit = {
    paramList.get(classifier) match {
      case Some(classifierParameters) =>
        if (!classifierParameters.contains((getterName, paramType, paramTypeName)))
          classifierParameters.push((getterName, paramType, paramTypeName))

      case None =>
        val newList = new LinkedList[(String, ParameterType, String)]
        newList.push((getterName, paramType, paramTypeName))
        paramList += classifier -> newList
    }
  }

  /**
   * creates two implicits for each classifier. one to and one from the newly created case class
   * @param obj
   * 	the classifier the implicits should belong to
   */
  def createImplicit(obj: EClassifier): String = {
    // enums and abstract classes will not be converted
    if (obj.isInstanceOf[EEnumImpl] || (obj.isInstanceOf[EClassImpl] && obj.asInstanceOf[EClassImpl].eIsSet(EcorePackage.ECLASS__ABSTRACT)))
      return ""
    else
      START + createImplicitElement2Term(obj) + createImplicitTerm2Element(obj)
  }

  /**
   * helper method to create the implicit from the model object to the case class
   */
  private def createImplicitElement2Term(obj: EClassifier): String = {
    START + "\timplicit def " + firstToLower(obj.getName()) + "2term " + getTypeParameterList(obj) + "(obj : " + obj.getName() + ")" + getImplicitParameterList(obj) + " : CtorTerm[" + obj.getName() + ", " + getCtorTypeList(obj) + "] = " + << +
      "\t\tTraces.trySetRoot(obj) += obj -> CtorTerm(classOf[" + obj.getName() + "], " + getCTorValueList(obj) + ")" + << +
      "\timplicit val v" + obj.getName + "2t" + " = " + firstToLower(obj.getName()) + "2term(_:" + obj.getName() + ")" + <<
  }

  /**
   *
   */
  private def getImplicitParameterList(obj: EClassifier): String = {
    var paramterList = this.paramList.get(obj).get

    var text = START
    for (param <- paramterList.reverse) {
      text += (param match {
        // is containment reference  
        case (attrName, paramType, typeName) if !isPrimitiveValue(typeName) && isContainmentReference(obj, attrName, typeName) && !text.contains(firstToLower(typeName) + "2t : " + typeName + " => CtorTerm[" + typeName + ", TL" + typeName + "]") => firstToLower(typeName) + "2t : " + typeName + " => CtorTerm[" + typeName + ", TL" + typeName + "], "
        case _ => ""
      })
    }

    if (text.length() > 0)
      "(implicit " + text.substring(0, text.length() - 2) + ")"
    else
      text
  }

  /**
   *
   */
  private def getImplicitParameterList2(obj: EClassifier): String = {
    var paramterList = this.paramList.get(obj).get

    var text = START
    for (param <- paramterList.reverse) {
      text += (param match {
        // is containment reference  
        case (attrName, paramType, typeName) if !isPrimitiveValue(typeName) && isContainmentReference(obj, attrName, typeName) && !text.contains("t2" + typeName + " : CtorTerm[" + typeName + ", TL" + typeName + "] => " + typeName) => "t2" + typeName + " : CtorTerm[" + typeName + ", TL" + typeName + "] => " + typeName + ", "
        case _ => ""
      })
    }

    if (text.length() > 0)
      "(implicit " + text.substring(0, text.length() - 2) + ")"
    else
      text
  }

  /**
   *
   */
  private def getTypeParameterList(obj: EClassifier): String = {
    var paramterList = this.paramList.get(obj).get

    var text = START
    for (param <- paramterList.reverse) {
      text += (param match {
        // is containment reference  
        case (attrName, paramType, typeName) if !isPrimitiveValue(typeName) && isContainmentReference(obj, attrName, typeName) && !text.contains("TL" + typeName + " <: TList") => "TL" + typeName + " <: TList, "
        case _ => ""
      })
    }

    if (text.length() > 0)
      "[" + text.substring(0, text.length() - 2) + "]"
    else
      text
  }

  /**
   *
   */
  private def getCtorTypeList(obj: EClassifier): String = {
    var paramterList = this.paramList.get(obj).get

    var text = START
    for (param <- paramterList.reverse) {
      text += (param match {
        // list of primitive datatype
        case (_, paramType, typeName) if (paramType.equals(ParameterType.ListValue)) && isPrimitiveValue(typeName) => "ListTerm[ValueTerm[" + typeName + "]] :: "

        // single value of primitive dataype
        case (_, paramType, typeName) if (!paramType.equals(ParameterType.ListValue)) && isPrimitiveValue(typeName) => "ValueTerm[" + typeName + "] :: "

        // list of references
        case (_, paramType, typeName) if (paramType.equals(ParameterType.ListValue)) && !isPrimitiveValue(typeName) => "ListTerm[RefTerm[" + typeName + "]] :: "

        // single value containment reference
        case (attrName, paramType, typeName) if (!paramType.equals(ParameterType.ListValue)) && !isPrimitiveValue(typeName) && isContainmentReference(obj, attrName, typeName) => "CtorTerm[" + typeName + ", TL" + typeName + "] :: "

        // single value cross reference
        case (attrName, paramType, typeName) if (!paramType.equals(ParameterType.ListValue)) && !isPrimitiveValue(typeName) && !isContainmentReference(obj, attrName, typeName) => "RefTerm[" + typeName + "] :: "
      })
    }

    text + "TNil"
  }

  /**
   * check if this is a containment attribute of the element
   */
  private def isContainmentReference(obj: EClassifier, attrName: String, paramName: String): Boolean = {
    var derivedAttrs = obj.asInstanceOf[EClassImpl].eGet(EcorePackage.ECLASS__EALL_REFERENCES, true, true).asInstanceOf[EList[EReference]]
    for (attr <- derivedAttrs) {
      if (attr.getName().toLowerCase().equals(attrName.toLowerCase()) &&
        attr.isInstanceOf[EReference] &&
        attr.asInstanceOf[EReference].isContainment()) {
        return true
      }
    }
    
    return false
  }

  /**
   * checks if this typeName belongs to a primitive datatype
   */
  private def isPrimitiveValue(typeName: String): Boolean = {
    primitiveTypes.contains(typeName)
  }

  /**
   *
   */
  private def getCTorValueList(obj: EClassifier): String = {
    var paramterList = this.paramList.get(obj).get

    var text = START
    for (param <- paramterList.reverse) {
      text += (param match {
        // list of primitive datatype
        // TODO LIST IMPLEMENTATION
        case (attrName, paramType, typeName) if (paramType.equals(ParameterType.ListValue)) && isPrimitiveValue(typeName) => "ListTerm(obj.get" + attrName + "()) :: "

        // single value of primitive dataype
        case (attrName, paramType, typeName) if (!paramType.equals(ParameterType.ListValue)) && isPrimitiveValue(typeName) => "ValueTerm(obj.get" + attrName + "()) :: "

        // list of references
        // TODO LIST IMPLEMENTATION
        case (attrName, paramType, typeName) if (paramType.equals(ParameterType.ListValue)) && !isPrimitiveValue(typeName) => "ListTerm(obj.get" + attrName + "()) :: "

        // single value containment reference
        case (attrName, paramType, typeName) if (!paramType.equals(ParameterType.ListValue)) && !isPrimitiveValue(typeName) && isContainmentReference(obj, attrName, typeName) => "obj.get" + attrName + "() :: "

        // single value cross reference
        case (attrName, paramType, typeName) if (!paramType.equals(ParameterType.ListValue)) && !isPrimitiveValue(typeName) && !isContainmentReference(obj, attrName, typeName) => "Traces.eo2refterm(obj.get" + attrName + "()) :: "
      })
    }

    text + "HNil"
  }

  /**
   * helper method to create the implicit from the case class to the model object
   */
  private def createImplicitTerm2Element(obj: EClassifier): String = {
    START + << + "\timplicit def term2" + firstToLower(obj.getName()) + getTypeParameterList(obj) + "(t : CtorTerm[" + obj.getName() + ", " + getCtorTypeList(obj) + "])" + getImplicitParameterList2(obj) + " : " + obj.getName() + " = {" + << +
      "\t\tvar obj : " + obj.getName() + " = null" + << +
      "\t\tTraces2.trySetRoot(t)" + << +
      "\t\tobj = " + firstToUpper(this.ePackageName) + "FactoryImpl.init().create" + obj.getName() + "()" + << +
      getFactoryValues(obj) + << +
      "\t\tTraces2 += t -> obj" + << +
      "\t}" + <<
    // TODO herausfinden ob man es überhaupt braucht
    // "\timplicit val vt2" + obj.getName() + " = term2" + obj.getName() + "(_: CtorTerm[" + obj.getName() + ", " + getCtorTypeList(obj) + "])" + <<
  }

  /**
   *
   */
  private def getFactoryValues(obj: EClassifier): String = {
    var paramterList = this.paramList.get(obj).get

    var text = START
    var index = 0
    for (param <- paramterList.reverse) {
      text += (param match {
        // list of primitive datatype
        case (attrName, paramType, typeName) if (paramType.equals(ParameterType.ListValue)) && isPrimitiveValue(typeName) => "\t\tobj.get" + attrName + "().addAll(0, Traces.listOf(t.nth(_" + index + "))) " + <<

        // single value of primitive dataype
        case (attrName, paramType, typeName) if (!paramType.equals(ParameterType.ListValue)) && isPrimitiveValue(typeName) => "\t\tobj.set" + attrName + "(t.nth(_" + index + ")) " + <<

        // list of references
        case (attrName, paramType, typeName) if (paramType.equals(ParameterType.ListValue)) && !isPrimitiveValue(typeName) => "\t\tobj.get" + attrName + "().addAll(Traces.listOf(t.nth(_" + index + "))) " + <<

        // single value containment reference
        case (attrName, paramType, typeName) if (!paramType.equals(ParameterType.ListValue)) && !isPrimitiveValue(typeName) && isContainmentReference(obj, attrName, typeName) => "\t\tobj.set" + attrName + "(t.nth(_" + index + ")) " + <<

        // single value cross reference
        case (attrName, paramType, typeName) if (!paramType.equals(ParameterType.ListValue)) && !isPrimitiveValue(typeName) && !isContainmentReference(obj, attrName, typeName) => "\t\tobj.set" + attrName + "(Traces.lazyref[" + obj.getName() + "](obj.get" + attrName + "(_), t.nth(_" + index + ").ref)) " + <<
      })

      index += 1
    }

    text
  }

  /**
   * helper method to generate the getter and setter calls for each parameter
   */
  private def setValueForEachParameter(obj: EClassifier): String = {
    val retBuff: StringBuffer = new StringBuffer()

    // do we have some parameters?
    paramList.get(obj) match {
      case None => ""
      case Some(classifierParameters) =>
        for (param <- classifierParameters) {

          // is it a collection datatype?
          if (param._2 == ParameterType.ListValue) {
            retBuff.append("\t\tobj.get" + param._1 + "().addAll(0, t." + getSafeName(firstToLower(param._1)) + ")\n")
          } else {
            retBuff.append("\t\tobj.set" + param._1 + "(t." + getSafeName(firstToLower(param._1)) + ")\n")
          }
        }
    }

    retBuff.toString()
  }

  /**
   * helper method to generate the string of the parameter list definition of an case class constructor
   */
  private def createParameterList(parameterList: LinkedList[(String, ParameterType, String)]): String = {

    val retBuff = new StringBuffer()

    for (value <- parameterList.reverseIterator) {
      if (value._2 == ParameterType.SingleBooleanValue)
        retBuff.append("obj.is" + value._1 + "(), ")
      else if (value._2 == ParameterType.ListValue)
        retBuff.append("if(obj != null) obj.get" + value._1 + "() else null, ")
      else
        retBuff.append("if(obj != null) obj.get" + value._1 + "() else " + getDefaultReturnValue(value._3) + ", ")
    }

    if (retBuff.length() > 2)
      retBuff.deleteCharAt(retBuff.length() - 2)

    retBuff.toString()
  }

  /**
   * Default values for DataTypes in the parameter lists if no value was set properly
   */
  private def getDefaultReturnValue(paramTypeName: String): String =
    paramTypeName match {
      case "Double" => "0.0"
      case _ => "null"
    }

  /**
   * convert standard e-datatypes to normal scala/java types
   */
  private def convertStandardValueTypes(typeName: EDataType): String = {
    typeName.getName() match {
      case "EString" => "String"
      case "EDouble" => "Double"
      case "EBigDecimal" => "BigDecimal"
      case "EBoolean" => "Boolean"
      case _ => typeName.getName()
    }
  }

  /**
   * helper method to avoid having valuenames that can not be used in scala
   */
  private def getSafeName(str: String): String = {
    str match {
      case "type" => "typeValue"
      case "return" => "returnValue"
      case "case" => "caseValue"
      case "class" => "classValue"
      case _ => str
    }
  }

  /**
   * helper method that adds some entries to the map of already generated imports, so that they actually not be generated
   * needs to be done due to the fact that emf offers some other data types than we have in scala
   */
  private def excludeSpecialTypesFromImport(): Unit = {
    this.importedClasses += "boolean" -> true
    this.importedClasses += "double" -> true
    this.importedClasses += "null" -> true
  }

  /**
   * helper string method (first character will be in upper case)
   */
  private def firstToUpper(str: String): String = {
    str.substring(0, 1).toUpperCase() + str.substring(1)
  }

  /**
   * helper string method (first character will be in upper case)
   */
  private def firstToLower(str: String): String = {
    str.substring(0, 1).toLowerCase() + str.substring(1)
  }

}
package de.digitalforger.smtl.m2t

import java.io.FileWriter

import scala.collection.JavaConversions.asScalaBuffer

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.EObject

import de.digitalforger.smtl.logging.LogHelper

/**
 * This trait defines the methods needed for M2T generator classes. They are called by a M2TTransformation
 *
 * @author Lars George
 */
trait IM2TGenerator {

  /**
   * This method is called by the transformation
   */
  def generate(resource: Resource): Unit;

  /**
   * This method returns a FileSupport object with which a new file can be created
   *
   * @param fileName
   * 	the name of the new file
   *
   * @return A FileSupport object that offers methods to work with new to create files
   */
  def createFile(fileName: String): FileSupport = {
    new FileSupport(fileName)
  }

  /**
   * creates a new file and sets its context
   *
   * @param fileName
   * 	the name of the new file
   *
   * @param content
   * 	the content of the new file
   */
  def createFile(fileName: String, content: String): Unit = {
    new FileSupport(fileName).withContent(content)
  }

  /**
   * Getter of an ExtendedString, so the work with the return string if more efficient
   */
  def START(): ExtendedString = {
    new ExtendedString()
  }

  /**
   * service method for a line ending
   */
  def <<() = "\n"

  /**
   * service method for a line ending
   */
  def EOL() = "\n"

  /**
   * adding new function support to a resource
   *
   * @param res
   * 	the resource that will get extended method support
   */
  implicit def ResourceToExtendedResource(res: Resource): ExtendedResource = {

    val extendedResource = new ExtendedResource(res)
    extendedResource
  }

  /**
   * adding new function support to an eobject
   *
   *@param obj
   *	the object that will get extended metho support
   */
  implicit def EObjectToExtendedEObject(obj: EObject): ExtendedEObject = {

    val extendedEObject = new ExtendedEObject(obj)
    extendedEObject
  }
  
  /**
   * Converting an ExtendedString to a normal String
   * 
   * @param extStr
   * 	The extended String that will be converted to a string
   */
  implicit def ExtendedToString(extStr : ExtendedString) : String = extStr.toString()
}

/**
 * A class that offers helper methods for string operations
 */
class ExtendedString() {
  
  /**
   * The StringBuffer that will hold the content. Its less memory consuming than working with a string-object
   */
  val strBuff = new StringBuffer()

  /**
   * adds a string to the extended string (uses a StringBuffer to be more efficient)
   * 
   * @param str 
   * 	the string to be added
   * 
   * @return the 'this' reference (ExtendedString)
   */
  def +(str: String): ExtendedString = {
    strBuff.append(str)
    this
  }
  
  
  /**
   * @see +(str: String) 
   */
  def +=(str: String): ExtendedString = {
    strBuff.append(str)
    this
  }
  
  /**
   * converts the internally used StringBuffer to an string
   * 
   * @return The string representation of an Extended String
   */
  def END(): String = {
    strBuff.toString()
  }
  
  /**
   * @inheritDoc
   */
  override def toString() : String = {
    END()
  }    
}



/**
 * A class that offers special methods for EObjects in the context of an model to text transformation
 * 
 * @author Lars George
 */
class ExtendedEObject(obj: EObject) extends LogHelper {
  /**
   * searches in the eobject for any instances of the given type and executes the given function with it
   *
   * @param cls
   * 	the type of instances that should be searched
   * @param fnc
   * 	the function to be applied on the found instances*
   *
   * @return the result of all method invocations with the found instances of the given type
   *
   */
  def foreach[T](cls: Class[T], fnc: (T) => String): String = {
    val ret: StringBuffer = new StringBuffer()

    if (this.obj == null) {
      logger.warn("The extended eobject has no resource to work with")
      return ""
    }

    for (rootElements <- this.obj.eContents()) {
      ret.append(foreach(rootElements, cls, fnc, ""))
    }

    ret.toString()

  }

  /**
   * This is the same as foreach(cls: Class[T], fnc: (T) => String) but it inserts the delimiter String between each element
   * 
   * @param cls
   * 	the type of instances that should be searched
   * @param fnc
   * 	the function to be applied on the found instances*
   * @param withLastDelete
   * 	If set to true, the last comma will be deleted before returning the string
   *
   * @return the result of all method invocations with the found instances of the given type
   */
  def foreach[T](cls: Class[T], fnc: (T) => String, delimiter : String, withLastDelete : Boolean = true): String = {
   val ret: StringBuffer = new StringBuffer()

    if (this.obj == null) {
      logger.warn("The extended eobject has no resource to work with")
      return ""
    }

    for (rootElements <- this.obj.eContents()) {
      ret.append(foreach(rootElements, cls, fnc, delimiter))     
    }
        
    if(withLastDelete && ret.length() >= delimiter.length())
    	ret.deleteCharAt(ret.length() - delimiter.length())
    	
    ret.toString()

  }

  /**
   * recursive foreach method to search in the given EObject for the given type and execute the given method with it
   * 
   * @param obj
   * 	The EObject that should be used
   * @param fnc
   * 	the function to be applied on the found instances
   * @param delimiter
   * 	The characters that should be used to seperate each found element
   */
  private def foreach[T](obj: EObject, cls: Class[T], fnc: (T) => String, delimiter : String): String = {
    val ret: StringBuffer = new StringBuffer()

    if (cls.isAssignableFrom(obj.getClass())) {
      ret.append(fnc(obj.asInstanceOf[T]))
      ret.append(delimiter)
    }

    // parse the content of the resource for the given type
    for (objContent <- obj.eContents()) {
      ret.append(foreach(objContent, cls, fnc, delimiter))      
    }

    ret.toString()
  }
}

/**
 * An ExtendedResource offers certain methods to work with the resource in the context of a model to text transformation
 */
class ExtendedResource(resource: Resource) extends LogHelper {

  /**
   * searches in the resource for any object of the given type and executes the given function with it
   *
   * @param cls
   * 	the type of instances that should be searched
   * @param fnc
   * 	the function to be applied on the found instances
   *
   * @return the result of all method invocations with the found instances of the given type
   *
   */
  def foreach[T](cls: Class[T], fnc: (T) => String): String = {
    val ret: StringBuffer = new StringBuffer()

    if (resource == null) {
      logger.warn("The extended resource has no object to work with")
      return ""
    }

    for (rootElements <- resource.getContents()) {
      ret.append(foreach(rootElements, cls, fnc))
    }

    ret.toString()
  }

  /**
   * recursive method that searches in the resource for the given type and executes the given method on it
   * 
   * @param cls
   * 	the type of instances that should be searched
   * @param fnc
   * 	the function to be applied on the found instances
   * 
   * @return the appended string from the user functions 
   */
  private def foreach[T](obj: EObject, cls: Class[T], fnc: (T) => String): String = {
    val ret: StringBuffer = new StringBuffer()

    if (obj == null) {
      logger.warn("The extended resource has no object to work with")
      return ""
    }

    if (cls.isAssignableFrom(obj.getClass())) {
      ret.append(fnc(obj.asInstanceOf[T]))
    }

    // parse the content of the eobject for the given type
    for (objContent <- obj.eContents()) {
      ret.append(foreach(objContent, cls, fnc))
    }

    ret.toString()
  }

}

/**
 * A class to support better syntactical shugar. It offers methods to build fluid "sentences" in method invocations
 */
class FileSupport(fileName: String) {

  /**
   * Creates a new file and sets its content
   *
   * @param content
   * 	the content of the new file
   */
  def withContent(content: String) = {
    val fileWrite = new FileWriter(fileName)
    fileWrite.write(content)
    fileWrite.close()
  }

}
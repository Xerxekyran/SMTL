package de.digitalforger.smtl.util

import java.io.IOException
import java.lang.Boolean
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl
import org.eclipse.emf.ecore.xmi.XMLResource
import org.eclipse.emf.ecore.EcorePackage
import de.digitalforger.smtl.logging.LogHelper
import de.digitalforger.smtl.m2t.TransformationM2T
import de.digitalforger.smtl.util.internal.CaseClassGenerator
import org.eclipse.emf.ecore.EPackage
import de.digitalforger.smtl.util.internal.ExperimentalCaseClassGenerator
import de.digitalforger.smtl.util.internal.TermCaseClassGenerator

/**
 * This object offers methods to generate case classes for EObjects automatically.
 *
 * @author Lars George
 */
object ImplicitGenerator extends LogHelper {

  /**
   * this method parses the ecore file from the given path and generates case classes for each element it finds.
   * additionally implicit definitions for thos new case classes are generate. the result is saved to the given destinationPath location
   *
   * @param sourcePath
   * 	The path to the source ecore file
   * @param destinationPath
   * 	The path where to save the result
   * @param objectName
   * 	The name of the scala object that should be created containing the new case class implicits
   * @param packageName
   * 	The name of the package the newly created object should have
   * @param referenceBasePackage
   * 	The base package name of the reference classes in the model
   */
  @throws(classOf[IOException])
  def analyzeEcore(sourcePath: String, destinationPath: String, objectName: String, packageName: String, referenceBasePackage: String): Unit = {

    // init the resource loading
    initResourceFactory()
    val resSet = new ResourceSetImpl()
    var loadOptions = resSet.getLoadOptions()
    loadOptions.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
    loadOptions.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());

    // get the source model
    val rs: ResourceSet = new ResourceSetImpl()
    val res = rs.getResource(URI.createURI(sourcePath), true)

    EcorePackage.eINSTANCE.eClass()    
      
    new TransformationM2T from res using new CaseClassGenerator(destinationPath, packageName, objectName, referenceBasePackage) transform sourcePath
    //new TransformationM2T from res using new ExperimentalCaseClassGenerator(destinationPath, packageName, objectName, referenceBasePackage) transform sourcePath
    //new TransformationM2T from res using new TermCaseClassGenerator(destinationPath, packageName, objectName, referenceBasePackage) transform sourcePath

  }

  /**
   * Makes any file loadable for a resource set
   */
  private def initResourceFactory(): Unit = {
    val etfm = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap()
    if (!etfm.containsKey("*")) {
      etfm.put("*", new XMIResourceFactoryImpl())
    }
  }
}
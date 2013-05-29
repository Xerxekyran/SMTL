package de.digitalforger.smtl.core.model.emf

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.asScalaIterator
import scala.collection.JavaConversions.mutableMapAsJavaMap
import scala.collection.mutable.HashSet
import scala.collection.mutable.HashMap

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EGenericType
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EcorePackage

import de.digitalforger.smtl.core.model.IReferenceModel
import de.digitalforger.smtl.logging.LogHelper

/**
 * The EMF implementation of an IReferenceModel.
 *
 *  Based on the ATL implementation
 *  
 *  @author Lars George
 */
class EMFReferenceModel(refModel: EMFReferenceModel, modelFactory: EMFModelFactory)
  extends EMFModel(refModel, modelFactory) with LogHelper with IReferenceModel {

  private var metaElementByName = new HashMap[String, EObject]()
  private val referencedResources = new HashSet[Resource]()
  private val elementsByType = new HashMap[EClass, Set[EObject]]()

  /**
   * getter of the referenced resources
   */
  def getReferencedResources() = referencedResources

  /**
   * @inheritDoc
   */
  override def getMetaElementByName(name: String): AnyRef = {
    metaElementByName.get(name).get
  }

  /**
   * Register EMF Packages
   */
  def register() = {
    registerPackages()
    addAllReferencedResources(getResource())
    metaElementByName = initMetaElementsInAllResources();
  }

  /**
   * Adding references for the given resource
   *
   * @param res
   * 	The resource that will be observed
   */
  protected def addAllReferencedResources(res: Resource): Unit = {
    for (it <- res.getAllContents()) {
      if (it.isInstanceOf[EClass]) {
        addReferencedResourcesFor(it.asInstanceOf[EClass], new HashSet[EClass]())
      } else if (it.isInstanceOf[EGenericType] && it.asInstanceOf[EGenericType].getEClassifier().isInstanceOf[EClass]) {
        addReferencedResourcesFor(it.asInstanceOf[EGenericType].getEClassifier().asInstanceOf[EClass], new HashSet[EClass]());
      }
    }
    getReferencedResources().remove(res)
  }

  /**
   * Indexes all eClasses in the main resource and the referenced resources
   */
  private def initMetaElementsInAllResources(): HashMap[String, EObject] = {
    val eClassifiers = new HashMap[String, EObject]()

    for (res <- getReferencedResources()) {
      initMetaElements(eClassifiers, res.getContents().iterator(), null)
    }
    initMetaElements(eClassifiers, getResource().getContents().iterator(), null)
    eClassifiers
  }

  /**
   * Init all meta elements
   */
  private def initMetaElements(eClassifiers: HashMap[String, EObject], it: Iterator[EObject], base: String): Unit = {
    for (eObject <- it) {
      if (eObject.isInstanceOf[EPackage]) {
        var name = eObject.asInstanceOf[EPackage].getName()
        if (base != null) {
          name = base + "::" + name
        }
        initMetaElements(eClassifiers, eObject.asInstanceOf[EPackage].eContents().iterator(), name)
      } else if (eObject.isInstanceOf[EClassifier]) {
        var name = eObject.asInstanceOf[EClassifier].getName()
        // register the classifier with its name
        register(eClassifiers, name, eObject)
        if (base != null) {
          name = base + "::" + name
          // register the classifier with its full name  
          register(eClassifiers, name, eObject)
        }
      } else {
        initMetaElements(eClassifiers, eObject.eContents().iterator(), base)
      }
    }
  }

  /**
   * Register the given elements
   */
  private def register(eClassifiers: HashMap[String, EObject], name: String, classifier: EObject): Unit = {
    if (eClassifiers.containsKey(name)) {
      logger.warn("EMFRerenceModel with several classifiers: " + name)
    }
    eClassifiers.put(name, classifier)
  }

  /**
   * Searches and adds all resources that are referenced from eClass to referencedResources
   *
   * @param eClass
   * 	the eclass that will be searched for references in
   * @param ignore
   * 	A set of classes that will be ignored
   */
  private def addReferencedResourcesFor(eClass: EClass, ignore: HashSet[EClass]): Unit = {
    if (ignore.contains(eClass)) return

    ignore.add(eClass)
    val resources = getReferencedResources()

    // references
    for (eRef <- eClass.getEReferences()) {
      if (eRef.isContainment()) {
        val eType = eRef.getEType()
        if (eType.eResource() != null) {
          resources.add(eType.eResource())
        } else {
          logger.warn("EMFReferenceModel = null reference: " + eType.toString())
        }

        if (eType.isInstanceOf[EClass]) {
          addReferencedResourcesFor(eType.asInstanceOf[EClass], ignore)
        }
      }
    }

    // attributes
    for (eAtt <- eClass.getEAllAttributes()) {
      val eType = eAtt.getEType()
      if (eType.eResource() != null) {
        resources.add(eType.eResource())
      } else {
        logger.warn("EMFReferenceModel = null reference: " + eType.toString())
      }
    }

    // super classes
    for (eSuperType <- eClass.getESuperTypes()) {
      if (eSuperType.eResource() != null) {
        resources.add(eSuperType.eResource())
        addReferencedResourcesFor(eSuperType, ignore)
      } else {
        logger.warn("EMFReferenceModel = null reference: " + eSuperType.toString())
      }
    }

  }

  /**
   * Register all packages
   */
  private def registerPackages() = {
    for (it <- getElementsByType(EcorePackage.eINSTANCE.getEPackage())) {
      val p: EPackage = it.asInstanceOf[EPackage]

      var nsURI = p.getNsURI()
      if (nsURI == null) {
        nsURI = p.getName()
        p.setNsURI(nsURI)
      }

      val mf = getModelFactory()

      synchronized {
        mf.getResourceSet().getPackageRegistry().put(nsURI, p)

      }

    }
  }

  /**
   * @inheritDoc
   */
  override def getElementsByType(metaElement: AnyRef): Set[EObject] = {
    val eClass = metaElement.asInstanceOf[EClass]

    elementsByType.get(eClass) match {
      case Some(s) => s
      case None => {
        var retVal = new HashSet[EObject]()
        val res = getResource()
        if (res != null) {
          for (it <- res.getAllContents()) {
            if (eClass.isInstance(it)) {
              retVal.add(it)
            }
          }
        }
        val ret = retVal.toSet
        elementsByType.put(eClass, ret)

        ret
      }
    }
  }

}
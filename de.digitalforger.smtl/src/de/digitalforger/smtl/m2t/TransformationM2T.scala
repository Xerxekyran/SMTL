package de.digitalforger.smtl.m2t

import scala.collection.JavaConversions.asScalaBuffer
import java.util.ArrayList
import scala.collection.mutable.HashSet
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import de.digitalforger.smtl.core.ITransformation
import org.eclipse.emf.ecore.resource.Resource
import scala.collection.mutable.HashMap

/**
 * A Model-To-Text transformation object. Uses aIM2TGenerator to generate the text from a model
 * 
 * @author Lars George
 */
class TransformationM2T extends ITransformation {

  private var generator: IM2TGenerator = null

  /**
   * @inheritdoc
   */
  override def from(fromMetaModelURI: String) = {
    super.from(fromMetaModelURI)
    this
  }

  /**
   * @inheritdoc
   */
  override def from(fromMetaModel: Resource) = {
    super.from(fromMetaModel)
    this
  }

  /**
   * @inheritdoc
   */
  override def in(loadFromFile: String) = {
    super.in(loadFromFile)
    this
  }

  /**
   * @inheritdoc
   */
  override def in(loadFromIterable: Iterable[_ <: EObject]) = {
    super.in(loadFromIterable)
    this
  }

  /**
   * sets the generator for this model to text transformation
   */
  def using(generator: IM2TGenerator) = {
    this.generator = generator
    this
  }

  /**
   * Performs this transformation
   * 
   * @param loadFromFile
   * 	The path to the source model
   */
  def transform(loadFromFile: String): Unit = {
    in(loadFromFile)
    if (this.fromModel == null) {
      logger.error("No model to transform from is set")
      return
    }

    this.generator.generate(this.fromModel.getResource())
  }

  /**
   * Performs this transformation
   * 
   * @param loadFromIterable
   * 	A list of instantiated source model elements
   */
  def transform(loadFromIterable: Iterable[_ <: EObject]): Unit = {
    in(loadFromIterable)
    if (this.fromModel == null) {
      logger.error("No model to transform from is set")
      return
    }

    this.generator.generate(this.fromModel.getResource())
  }

}
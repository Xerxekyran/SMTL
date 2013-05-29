package de.digitalforger.smtl.trace

/**
 * This enum defines the connection types of elements in models during
 * a model transformation
 *
 * @author Lars George
 */
object ETraceConnectionType extends Enumeration {

  type ETraceConnectionType = Value

  val Created, Changed = Value
}
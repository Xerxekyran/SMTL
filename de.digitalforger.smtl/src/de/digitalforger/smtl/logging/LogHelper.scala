package de.digitalforger.smtl.logging

import org.apache.log4j.Logger

/**
 * This trait adds simple logging support
 *
 * @author Lars George
 */
trait LogHelper {
  val loggerName = this.getClass.getName
  
  /**
   * The logger instance for each class
   */
  lazy val logger = Logger.getLogger(loggerName)
}
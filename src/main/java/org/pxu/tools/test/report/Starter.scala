package org.pxu.tools.test.report

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Prabhu Periasamy
 */
class Main {

  val logger = LoggerFactory.getLogger(classOf[Main])

  def starter() {
    logger.info("Starting...")
    val analyzer = new Analyzer();
    analyzer.startup()
    logger.info("Startup complete.")
  }

}

object StartUp {
  def main(args: Array[String]) {
    val instance = new Main;
    instance.starter()
  }
}
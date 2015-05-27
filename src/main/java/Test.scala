
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author praboo_p
 */
class HelloWorld {

  val logger = LoggerFactory.getLogger(classOf[HelloWorld])
  def print() { logger.debug("Hello, world!") }

}

object Test {
  def main(args: Array[String]) {
    val instance = new HelloWorld;
    instance.print()
  }
}
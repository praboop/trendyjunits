package org.pxu.tools.test.report

import com.sun.net.httpserver._
import java.net.InetSocketAddress
import org.slf4j.LoggerFactory
import java.io._
import java.nio.file.{ Paths, Files }
import java.util.concurrent._

class Analyzer {

  val logger = LoggerFactory.getLogger(classOf[Analyzer])
  val WEB_ROOT = System.getProperty("web.root",new File("").getAbsolutePath + File.separator + "target" + File.separator + "web") ;
  val START_PAGE = "welcome.html";

  // Map containing requested resource file ending and associated type
  val types: Map[String, String] = collection.immutable.HashMap("css" -> "text/css", "js" -> "application/javascript", "json" -> "application/json", "html" -> "text/html", "ico" -> "image/x-icon")
  val extnPath: Map[String, String] = collection.immutable.HashMap("css" -> "css", "js" -> "js", "html" -> "pages", "ico" -> "images")

  // Resource name to Resource content
  val buffContent = collection.mutable.Map[String, String]()

  def buildFromCachedResource(response: ResponseBuilder) {
    
    var fromcache = true;

    if (!response.isStaticResource() || !buffContent.contains(response.path)) {
      fromcache = false
      val fullPath = response.getFullPath()
      
      if (!new File(fullPath).exists) {
        logger.error("Resource not found: " + fullPath)
        response.setErrorCode(404)
        return
      }

      val source = io.Source.fromFile(fullPath)
      val lines = try source.getLines mkString "\n" finally source.close()
      buffContent.put(response.path, lines)
    }

    logger.debug("returning resource " + response.simplepath + " from cache " + fromcache)
    response.content = buffContent.get(response.path).get
  }

  def getContentToPost(): String = {

    return ""

  }

  private class ResponseBuilder(val typeStr: String, extn: String, var path: String) {

    var returnCode: Integer = 200
    var content: String = ""
    val simplepath: String = path

    def isStaticResource(): Boolean = {
      if (extn.equals("html")) // angular
        return false;

      return !types.getOrElse(extn, "none").equals("none")
    }

    def getFullPath(): String = {
      return WEB_ROOT + File.separator + extnPath.get(extn).get + File.separator + path
    }

    def setErrorCode(errorCode: Integer) {
      returnCode = errorCode
    }

    def finishResponse(t: HttpExchange) {
      val os = t.getResponseBody();
      t.getResponseHeaders().add("Content-type", typeStr);
      t.sendResponseHeaders(returnCode, content.length());
      os.write(content.getBytes());
      os.close();
    }

  }

  object MainPageHandler extends HttpHandler {
    def handle(t: HttpExchange): Unit = {

      try {
        //logger.debug("Got request for: " + t.getRequestURI())
        var path = t.getRequestURI().toString()
        if (path.equals("/")) {
          path = START_PAGE
        }
        val nameSplit = path.split('.')
        
        if (nameSplit.length < 2)
          throw new Exception("Unable to figure out the extension of the resource: " + path)
        
        val extn = nameSplit.last

        if (!types.contains(extn))
          throw new Exception("The resource requested is not handled: " + path)

        val typeStr = types.get(extn).get
        val response: ResponseBuilder = new ResponseBuilder(typeStr, extn, path)
        buildFromCachedResource(response);
        response.finishResponse(t)
      } catch {
        case e: Exception => logger.error("Error handling request", e)
      }
    }
  }

  def startup() {
    try {
      var listenPort = Integer.parseInt(System.getProperty("httpport", "8000"))
      var server = HttpServer.create(new InetSocketAddress(listenPort), 10)
      var context = server.createContext("/", MainPageHandler)
      server.setExecutor(Executors.newCachedThreadPool())
      server.start()

    } catch {
      case e: Exception => e.printStackTrace();
    }
  }
}
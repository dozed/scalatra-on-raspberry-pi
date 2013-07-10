/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 15.06.13
 * Time: 00:56
 * To change this template use File | Settings | File Templates.
 */

import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

object JettyMain {

  def run = {
    val server = new Server

    server.setGracefulShutdown(5000)
    server.setSendServerVersion(false)
    server.setSendDateHeader(true)
    server.setStopAtShutdown(true)

    val connector = new SelectChannelConnector
    connector.setPort(8080)
    connector.setMaxIdleTime(90000)
    server.addConnector(connector)

    val context = new WebAppContext
    context.setContextPath("/")

    val resourceBase = getClass.getClassLoader.getResource("webapp").toExternalForm
    context.setResourceBase(resourceBase)

    // use this instead of web.xml
    // - what if there is the need to add another servlet? -> shouldn't
    // - can the web.xml file be dropped?
    // - can the WEB-INF folder be dropped?
    // - what about precompiled templates?

    //    context.setInitParameter(ScalatraListener.LifeCycleKey, "ScalatraBootstrap")
        context.addEventListener(new ScalatraListener)
    //    context.addServlet(classOf[DefaultServlet], "/")

    server.setHandler(context)

    server.start
    server.join
  }
}
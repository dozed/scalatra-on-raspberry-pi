package org.dots42.rascal

import org.scalatra._
import scalate.ScalateSupport
import org.podval.iot.i2c.I2c
import org.podval.iot.platform.raspberrypi.Bcm2835Gpio

case class X(x: Int)

class RascalServlet extends ScalatraOnRaspberryPiStack {

  get("/") {
    val x = X(2600)
    println(x)
    <html>
      <body>
        <h1>Test</h1>
        Say
        <a href="hello-scalate">hello to Scalate</a>
        .
      </body>
    </html>
  }


}

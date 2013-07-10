import com.pi4j.io.gpio.{RaspiPin, PinState, GpioFactory}
import java.io.File

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 15.06.13
 * Time: 18:00
 * To change this template use File | Settings | File Templates.
 */

import org.podval.iot.i2c.I2c
import org.podval.iot.platform.raspberrypi.Bcm2835Gpio

object RaspberryPi {
  lazy val revision: Int = {
    // XXX: revision is available from /proc/cpuinfo after keyword "Revision"
    // XXX also: in /proc/cmdline, after bcm2708.boardrev
    // see, for instance, https://github.com/quick2wire/quick2wire-python-api
    2
  }

  lazy val i2cController = new I2c


  def i2c0 = i2cController.bus(0)


  def i2c1 = i2cController.bus(1)


  def i2c = if (revision > 1) i2c1 else i2c0


  lazy val gpio = new Bcm2835Gpio

}

trait TemporaryDirectory {

  val MaximumTries = 10

  val temporaryDirectory = new File(System.getProperty("java.io.tmpdir"))

  val random = new java.util.Random

  def createDirectory(dir: File): Option[String] = {
    if (dir.exists) {
      if (dir.isDirectory)
        None
      else
        Some(dir + " exists and is not a directory.")
    }
    else {
      dir.mkdirs()
      None
    }
  }

  def createTemporaryDirectory: Either[String, File] = {
    def create(tries: Int): Either[String, File] = {
      if (tries > MaximumTries)
        Left("Could not create temporary directory.")
      else {
        val randomName = "sbt_" + java.lang.Integer.toHexString(random.nextInt)
        val f = new File(temporaryDirectory, randomName)

        if (createDirectory(f).isEmpty)
          Right(f)
        else
          create(tries + 2)
      }
    }
    create(0)
  }

}

object MainClass extends App {

  // JettyMain.run

  // val pin1 = RaspberryPi.gpio.pin(1)

  val gpio = GpioFactory.getInstance

  // provision gpio pin #01 as an output pin and turn on
  val pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH)
  println("--> GPIO state should be: ON")

  Thread.sleep(5000)

  // turn off gpio pin #01
  pin.low
  println("--> GPIO state should be: OFF")

  Thread.sleep(5000)

  // toggle the current state of gpio pin #01 (should turn on)
  pin.toggle
  println("--> GPIO state should be: ON")

  Thread.sleep(5000)

  // toggle the current state of gpio pin #01  (should turn off)
  pin.toggle
  println("--> GPIO state should be: OFF")

  Thread.sleep(5000)

  // turn on gpio pin #01 for 1 second and then off
  println("--> GPIO state should be: ON for only 1 second")
  pin.pulse(1000, true) // set second argument to 'true' use a blocking call

  // stop all GPIO activity/threads by shutting down the GPIO controller
  // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
  gpio.shutdown

}

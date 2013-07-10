import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

import sbtassembly.Plugin._
import AssemblyKeys._

object ScalatraOnRaspberryPiBuild extends Build {
  val Organization = "org.dots42"
  val Name = "Scalatra on Raspberry Pi"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.10.1"
  val ScalatraVersion = "2.2.1"

  lazy val project = Project(
    "scalatra-on-raspberry-pi",
    file("."),
    settings = Defaults.defaultSettings ++ assemblySettings ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository",
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "compile",
        "com.pi4j" % "pi4j-core" % "0.0.5" % "compile",
        "org.podval.iot.platform" % "org.podval.iot.platform.raspberrypi" % "0.1-SNAPSHOT" % "compile",
        "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
      ),

      // settings for templates
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile) {
        base =>
          Seq(
            TemplateConfig(
              base / "webapp" / "WEB-INF" / "templates",
              Seq.empty, /* default imports should be added here */
              Seq(
                Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
              ), /* add extra bindings here */
              Some("templates")
            )
          )
      },

      // handle conflicts during assembly task
      mergeStrategy in assembly <<= (mergeStrategy in assembly) {
        (old) => {
          case "about.html" => MergeStrategy.first
          case x => old(x)
        }
      },

      // copy web resources to /webapp folder
      resourceGenerators in Compile <+= (resourceManaged, baseDirectory) map {
        (managedBase, base) =>
          val webappBase = base / "src" / "main" / "webapp"
          for {
            (from, to) <- webappBase ** "*" x rebase(webappBase, managedBase / "main" / "webapp")
          } yield {
            Sync.copy(from, to)
            to
          }
      }

    )
  )
}

import Dependencies._
import org.scalafmt.sbt.ScalafmtPlugin.autoImport._
import sbt.Keys.{crossScalaVersions, scalaVersion}
import sbt.{Compile, Test}

object Settings {
  val defaultScalaVersion = scalaVersion := versions.scala2

  val defaultCrossScalaVersions = crossScalaVersions := Seq(versions.scala2, versions.scala3)

  val fmtCheck = scalafmtCheckAll := {
    (Compile / scalafmtSbtCheck).value
    (Compile / scalafmtCheck).value
    (Test / scalafmtCheck).value
  }

  val fmt = scalafmtAll := {
    (Compile / scalafmtSbt).value
    (Compile / scalafmt).value
    (Test / scalafmt).value
  }
}

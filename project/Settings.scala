import Dependencies._
import org.scalafmt.sbt.ScalafmtPlugin.autoImport._
import sbt.Keys._
import sbt.{Compile, Test, url}

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

  private val versionSetting = version := "0.1.0-SNAPSHOT"

  private val organizationSetting = organization := "org.scalacn.exchange"

  private val licensesSetting = licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

  val projectVersionSetting = Seq(versionSetting, organizationSetting, licensesSetting)

  val defaultScalacOptions = Seq(
    "-deprecation",
    "-Xsource:3",
  )
}

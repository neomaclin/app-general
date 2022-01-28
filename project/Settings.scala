import sbt.Keys._
import sbt.url

object Settings {

  private val versionSetting = version := "0.1.0-SNAPSHOT"

  private val organizationSetting = organization := "com.group.quasi"

  private val licensesSetting = licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

  val projectVersionSetting = Seq(versionSetting, organizationSetting, licensesSetting)

  val defaultScalacOptions = Seq(
//    "-opt-inline-from:**",
//    "-opt:l:inline",
    "-feature",
    "-unchecked",
    "-language:higherKinds",
    "-Xlint:unused",
    "-deprecation",
  )
}

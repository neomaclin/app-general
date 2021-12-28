import Dependencies._
import Settings._

name := "exchange-app"

defaultScalaVersion
defaultCrossScalaVersions
projectVersionSetting

scalacOptions ++= defaultScalacOptions

fmtCheck
fmt

enablePlugins(JavaAppPackaging)

import Dependencies._
import Settings._

name := "exchange-app"

defaultScalaVersion
defaultCrossScalaVersions
projectVersionSetting

libraryDependencies ++= akka

scalacOptions ++= defaultScalacOptions

fmtCheck
fmt

enablePlugins(JavaAppPackaging)

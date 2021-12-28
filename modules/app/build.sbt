import Dependencies._
import Settings.{defaultCrossScalaVersions, defaultScalaVersion, fmt, fmtCheck}

name := "exchange-app"

defaultScalaVersion
defaultCrossScalaVersions

scalacOptions ++= defaultScalacOptions

fmtCheck
fmt

enablePlugins(JavaAppPackaging)

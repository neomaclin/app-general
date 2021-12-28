import Dependencies._
import Settings._

name := "exchange-domain"

libraryDependencies ++= cats

defaultScalaVersion
defaultCrossScalaVersions
projectVersionSetting

scalacOptions ++= defaultScalacOptions

fmtCheck
fmt

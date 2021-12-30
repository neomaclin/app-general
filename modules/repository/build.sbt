import Dependencies._
import Settings._

name := "exchange-repository"

defaultScalaVersion
defaultCrossScalaVersions
projectVersionSetting

libraryDependencies ++= doobie

scalacOptions ++= defaultScalacOptions

fmtCheck
fmt

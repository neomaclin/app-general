import Dependencies._
import Settings._

name := "exchange-api"

defaultScalaVersion
defaultCrossScalaVersions

libraryDependencies ++= tapir ++ circe

scalacOptions ++= defaultScalacOptions

fmtCheck
fmt

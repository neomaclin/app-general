import Dependencies._
import Settings._

name := "exchange-domain"

libraryDependencies ++= cats

defaultScalaVersion
defaultCrossScalaVersions

scalacOptions ++= defaultScalacOptions

fmtCheck
fmt

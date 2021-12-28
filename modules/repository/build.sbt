import Dependencies._
import Settings._

name := "exchange-repository"

defaultScalaVersion
defaultCrossScalaVersions
projectVersionSetting

scalacOptions ++= defaultScalacOptions

fmtCheck
fmt

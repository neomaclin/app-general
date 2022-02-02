import Dependencies._
import Settings._

name := "trade-api"

//defaultScalaVersion
//defaultCrossScalaVersions
//projectVersionSetting

libraryDependencies ++= tapir ++ circe

scalacOptions ++= defaultScalacOptions
//
//fmtCheck
//fmt

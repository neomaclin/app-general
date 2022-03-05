import Dependencies._
import Settings._

name := "api"

scalaVersion := versions.scala2
crossScalaVersions := Seq(versions.scala2, versions.scala3)
scalafmtCheckAll := {
  (Compile / scalafmtSbtCheck).value
  (Compile / scalafmtCheck).value
  (Test / scalafmtCheck).value

}
scalafmtAll := {
  (Compile / scalafmtSbt).value
  (Compile / scalafmt).value
  (Test / scalafmt).value
}

libraryDependencies ++= tapir ++ circe ++ refined ++ jwt

scalacOptions ++= defaultScalacOptions

import Dependencies._
import Settings._

name := "auth-api"

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
  // (IntegrationTest / scalafmt).value
}

libraryDependencies ++= tapir ++ circe ++ refined ++ jwt

scalacOptions ++= defaultScalacOptions



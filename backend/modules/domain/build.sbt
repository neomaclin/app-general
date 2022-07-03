import Dependencies._
import Settings._

name := "domain"

libraryDependencies ++= cats ++ akka ++ refined ++ catsMTL ++ kitten

scalaVersion := versions.scala2
//crossScalaVersions := Seq(versions.scala2, versions.scala3)
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

scalacOptions ++= defaultScalacOptions

import Dependencies._
import Settings._

name := "infra"

libraryDependencies ++= akka ++ akkaHttp ++ alpakka ++ distage ++ configSupport ++ common
libraryDependencies ++= emailDependencies ++ quartzScheduler

scalaVersion := versions.scala2
crossScalaVersions := Seq(versions.scala2, versions.scala3)
scalafmtCheckAll := {
  (Compile / scalafmtSbtCheck).value
  (Compile / scalafmtCheck).value
  (Test / scalafmtCheck).value
  //  (IntegrationTest / scalafmtCheck).value
}
scalafmtAll := {
  (Compile / scalafmtSbt).value
  (Compile / scalafmt).value
  (Test / scalafmt).value
  // (IntegrationTest / scalafmt).value
}

scalacOptions ++= defaultScalacOptions

Compile / TwirlKeys.compileTemplates / sourceDirectories := (Compile / unmanagedSourceDirectories).value
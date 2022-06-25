import Dependencies._
import Settings._

name := "infra"

libraryDependencies ++= akkaStream ++ akkaHttp ++ distage ++ configSupport ++ quartzScheduler ++ logger ++ catsEffect2
libraryDependencies += "com.github.t3hnar" %% "scala-bcrypt" % "4.3.0"

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

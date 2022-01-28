import Dependencies._
import Settings._

enablePlugins(FlywayPlugin)

name := "repository"

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

libraryDependencies ++=  alpakka ++ slickPg

scalacOptions ++= defaultScalacOptions

flywayUrl := "jdbc:postgresql://127.0.0.1/app"
flywayUser := "slick"
flywayPassword := ""
flywayLocations += "db/migration"
//flywayUrl in Test := "jdbc:hsqldb:file:target/flyway_sample;shutdown=true"
//flywayUser in Test := "SA"
//flywayPassword in Test := ""
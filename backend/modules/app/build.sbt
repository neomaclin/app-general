
import Dependencies.versions
import Settings._

name := "app"
scalaVersion := versions.scala2
//crossScalaVersions := Seq(versions.scala2, versions.scala3)

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

enablePlugins(JavaAppPackaging)

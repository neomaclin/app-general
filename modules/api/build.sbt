import Dependencies._

name := "api"

libraryDependencies ++= tapir ++ circe

crossScalaVersions ++= Seq(versions.scala2, versions.scala3)

scalacOptions ++= defaultScalacOptions

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

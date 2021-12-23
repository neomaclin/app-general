import Dependencies._

name := "project-client"

scalacOptions ++= defaultScalacOptions

crossScalaVersions := Seq(versions.scala2Version, versions.scala3Version)

scalafmtCheckAll := {
  (Compile/scalafmtSbtCheck).value
  (Compile/scalafmtCheck).value
  (Test/scalafmtCheck).value
}

scalafmtAll := {
  (Compile/scalafmtSbt).value
  (Compile/scalafmt).value
  (Test/scalafmt).value
}
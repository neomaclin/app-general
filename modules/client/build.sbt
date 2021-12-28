import Dependencies._

name := "client"

scalacOptions ++= defaultScalacOptions

crossScalaVersions ++= Seq(versions.scala2, versions.scala3)

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

import Dependencies._

name := "project-metric"

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

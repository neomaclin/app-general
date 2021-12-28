import Dependencies._

name := "exchange-repository"

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

enablePlugins(JavaAppPackaging)

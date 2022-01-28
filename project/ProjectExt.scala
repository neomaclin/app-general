import sbt.Keys._
import sbt.Project
object ProjectExt {

  implicit class WithCompilerSettingOps(val project:Project) extends AnyVal {
    def withCompilerSettings(compilerOptions:Seq[String]=Nil):Project = {
      project.settings(
        scalacOptions ++= compilerOptions
      )
    }
  }

}

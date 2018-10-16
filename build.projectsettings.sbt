lazy val sharedTestDirectory = settingKey[File]("Shared Test root folder")
lazy val referenceConfPath = settingKey[File]("Path to reference conf")
lazy val gitCommit = settingKey[Seq[String]]("Git Commit key and value for reference conf")

sharedTestDirectory := (sourceDirectory in Test).value / "shared"
referenceConfPath := (baseDirectory in LocalRootProject).value / "conf" / "reference.conf"

def writeFile(file: File, contents: Seq[String]): File = {
  IO.touch(file, setModified = false)
  if(IO.read(file).compareTo(contents.mkString("\n")) != 0) {
    IO.write(file, contents.mkString("\n"))
  }
  file
}

gitCommit := Seq("app.git.commit = '%s'".format(git.gitHeadCommit.value.getOrElse("UNKNOWN_COMMIT")))

scalaSource in Test                             := (sourceDirectory in Test).value / "unit"
javaSource in Test                              := (sourceDirectory in Test).value / "unit"
unmanagedSourceDirectories in Test              += sharedTestDirectory.value
parallelExecution in IntegrationTest            := false
scalaSource in IntegrationTest                  := (sourceDirectory in Test).value / "integration"
javaSource in IntegrationTest                   := (sourceDirectory in Test).value / "integration"
unmanagedSourceDirectories in IntegrationTest   += sharedTestDirectory.value
resourceDirectory in IntegrationTest            := (resourceDirectory in Test).value
resourceGenerators in Compile                   += Def.task {
                                                        Seq(writeFile(referenceConfPath.value, gitCommit.value))
                                                    }.taskValue

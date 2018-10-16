import java.util.Properties

val appProperties = {
  val prop = new Properties()
  IO.load(prop, new File("conf/application.common.conf"))
  prop
}

// Root/Service specific settings
val projectName = appProperties.getProperty("project.name").replaceAll("\"", "")
val projectVersion = appProperties.getProperty("project.version").replaceAll("\"", "")
val projectOrganization = appProperties.getProperty("project.organization")

name := projectName
version := projectVersion

// Disable publish of root/service
skip in publish := true
publishArtifact := false
publish := {}

// parallelExecution in Test := false

val commonSettings = Defaults.itSettings ++ Seq(
  scalaVersion        := "2.12.3",
  organization        := projectOrganization,
  crossScalaVersions  := Seq("2.11.12", scalaVersion.value)
)

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(GitVersioning)
  .settings(commonSettings: _*)
  .configs(IntegrationTest)
  .aggregate(models)
  .dependsOn(models)

lazy val models = (project in file("submodules/models"))
  .settings(commonSettings: _*)
  .configs(IntegrationTest)

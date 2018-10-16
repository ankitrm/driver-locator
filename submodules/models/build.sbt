name                := "driver-locator-api"
version             := "0.0.1-0"
isSnapshot          := version.value.contains("-")

publishTo := {
  val name: String = if (isSnapshot.value) "pre-releases" else "releases"
  Some(s"company-nexus-$name" at s"http://nexus.infra.company.name.com:8081/nexus/content/repositories/$name/")
}

val TestAndIt = "it,test"
val scalatestVersion = "3.0.5"
val spec2CoreVersion = "4.3.4"

libraryDependencies ++= Seq(
  "org.scalatest"                    %% "scalatest"           % scalatestVersion          % TestAndIt,
  "org.specs2"                       %% "specs2-core"         % spec2CoreVersion          % Test
)

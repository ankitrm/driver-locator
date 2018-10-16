resolvers in ThisBuild ++= Seq(
  Resolver.mavenLocal,
  Resolver.typesafeRepo("releases"),
  DefaultMavenRepository)

val scalaLoggingVersion = "3.9.0"
val logbackVersion = "1.2.3"
val logstashEncoderVersion = "5.1"
val janinoVersion = "3.0.8"
val guiceMultiBindingVersion = "4.1.0"
val reactiveMongoVersion = "0.16.0-play26"
val mockitoCoreVersion = "2.8.47"
val TestAndIt = "it,test"
val scalatestVersion = "3.0.5"
val flapdoodleEmbedMongoVersion = "2.1.1"
val spec2CoreVersion = "4.3.4"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging"       %% "scala-logging"                   % scalaLoggingVersion,
  "ch.qos.logback"                   % "logback-classic"                  % logbackVersion,
  "org.codehaus.janino"              % "janino"                           % janinoVersion,
  "net.logstash.logback"             % "logstash-logback-encoder"         % logstashEncoderVersion,
  "org.reactivemongo"                %% "play2-reactivemongo"             % reactiveMongoVersion,
  "org.mockito"                      %  "mockito-core"                    % mockitoCoreVersion          % TestAndIt,
  "org.specs2"                       %% "specs2-core"                     % spec2CoreVersion            % Test,
  "org.scalatest"                    %% "scalatest"                       % scalatestVersion            % TestAndIt,
  "de.flapdoodle.embed"              %  "de.flapdoodle.embed.mongo"       % flapdoodleEmbedMongoVersion % TestAndIt
)

libraryDependencies += guice

libraryDependencies += ws

name := "integration"

version := "1.0"

scalaVersion := "2.11.11"

lazy val root = (project in file(".")).
  settings(
    name := "myntelligence-text-transformation",
    version := "1.0",
    scalaVersion := "2.11.8",
    mainClass in Compile := Some("com.myntelligence.text.tranformation.ScrapperService"),
    assemblyJarName := "integration.jar"

  ).settings(
  resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository",
  name := "Core"
).enablePlugins(AssemblyPlugin)

libraryDependencies ++= Seq(
  "org.apache.spark"            %% "spark-core"         % "2.1.0" % "provided", // exclude("org.slf4j", "slf4j-log4j12"), //
  "org.apache.spark"            %% "spark-streaming"    % "2.1.0" % "provided", // exclude("org.slf4j", "slf4j-log4j12"), //
  "com.typesafe.scala-logging"  %% "scala-logging"      % "3.5.0",
  "ch.qos.logback"              %  "logback-classic"    % "1.1.7",
  "com.stratio.receiver"        %  "spark-rabbitmq"     % "0.5.1",
  "org.jsoup"                   %  "jsoup"              % "1.10.2",
  "com.typesafe.play"           %% "play-json"          % "2.4.6",
  "com.newmotion"               %% "akka-rabbitmq"      % "4.0.0",
  "com.typesafe.akka"           %% "akka-stream"        % "2.4.12",
  "io.scalac"                   %% "reactive-rabbit"    % "1.1.4"

)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("reference.conf") => MergeStrategy.concat
  case x => MergeStrategy.first
}
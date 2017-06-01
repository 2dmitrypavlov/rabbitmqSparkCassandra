name := "integration"

version := "1.0"

scalaVersion := "2.11.8"

lazy val root = (project in file(".")).
  settings(
    name := "myntelligence-text-transformation",
    version := "1.0",
    scalaVersion := "2.11.8",
    mainClass in Compile := Some("com.jactravel.boot.JactravelRabbitReceiver"),
    assemblyJarName := "uber-jactravel-monitoring.jar"

  ).settings(
  resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository",
  name := "Core"
).enablePlugins(AssemblyPlugin)

libraryDependencies ++= Seq(
  "org.apache.spark"            %% "spark-core"                % "2.1.1" exclude("org.slf4j", "slf4j-log4j12"), // % "provided", //
  "org.apache.spark"            %% "spark-streaming"           % "2.1.1" exclude("org.slf4j", "slf4j-log4j12"), // % "provided", //
  "org.apache.spark"            %% "spark-sql"                 % "2.1.1" exclude("org.slf4j", "slf4j-log4j12"), // % "provided", //
  "com.typesafe.akka"           %% "akka-stream"               % "2.4.12",
  "com.datastax.spark"          %% "spark-cassandra-connector" % "2.0.2",
  "com.google.protobuf"         %  "protobuf-java"             % "3.3.0",
  "com.typesafe.scala-logging"  %% "scala-logging"             % "3.5.0",
  "ch.qos.logback"              %  "logback-classic"           % "1.1.7",
  "com.stratio.receiver"        %  "spark-rabbitmq"            % "0.5.1"

)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("reference.conf") => MergeStrategy.concat
  case x => MergeStrategy.first
}
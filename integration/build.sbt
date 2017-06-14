name := "integration"

version := "1.0"

val dottyVersion = "0.1.2-RC1"
val scala211Version = "2.11.8"

lazy val root = (project in file(".")).
  settings(
    name := "myntelligence-text-transformation",
    version := "1.0",
    scalaVersion := scala211Version, //"2.11.8",
    mainClass in Compile := Some("com.jactravel.boot.JactravelRabbitReceiver"),
    assembleArtifact in assemblyPackageScala := false,
    assemblyJarName := "uber-jactravel-monitoring.jar"
    // To cross compile with Dotty and Scala 2
    //crossScalaVersions := Seq(dottyVersion, scala211Version),
    //scalacOptions ++= { if (isDotty.value) Seq("-language:Scala2") else Nil }

  ).settings(
  resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository",
  name := "Core"
).enablePlugins(AssemblyPlugin)

libraryDependencies ++= Seq(
  "org.apache.spark"            %% "spark-core"                % "2.1.1" exclude("org.slf4j", "slf4j-log4j12"), // % "provided", //
  "org.apache.spark"            %% "spark-streaming"           % "2.1.1" exclude("org.slf4j", "slf4j-log4j12"), // % "provided", //
  "org.apache.spark"            %% "spark-sql"                 % "2.1.1" exclude("org.slf4j", "slf4j-log4j12"), // % "provided", //
  "com.typesafe.akka"           %% "akka-stream"               % "2.4.16",
  "com.datastax.spark"          %% "spark-cassandra-connector" % "2.0.2",
  "com.google.protobuf"         %  "protobuf-java"             % "2.5.0",
  "com.typesafe.scala-logging"  %% "scala-logging"             % "3.5.0",
  "ch.qos.logback"              %  "logback-classic"           % "1.1.7",
  "com.stratio.receiver"        %  "spark-rabbitmq"            % "0.5.1"

)

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$") => MergeStrategy.discard
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("reference.conf") => MergeStrategy.concat
  case _ => MergeStrategy.first
}

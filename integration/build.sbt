name := "integration"

version := "1.0"

val dottyVersion = "0.1.2-RC1"
val scala211Version = "2.11.8"
val sparkVersion = "2.2.1"
val akkaVersion = "2.4.11"

lazy val root = (project in file(".")).
  settings(
    name := "myntelligence-text-transformation",
    version := "1.0",
    scalaVersion := scala211Version, //"2.11.8",
    mainClass in Compile := Some("com.jactravel.monitoring.streaming.ProcessLogging"),
    assembleArtifact in assemblyPackageScala := false,
    assemblyJarName := "uber-jactravel-monitoring.jar",
    libraryDependencies ++= Seq(
      "org.apache.spark"            %% "spark-core"                % sparkVersion exclude("org.slf4j", "slf4j-log4j12"), //% "provided", //
      "org.apache.spark"            %% "spark-streaming"           % sparkVersion exclude("org.slf4j", "slf4j-log4j12"), //% "provided", //
      "org.apache.spark"            %% "spark-sql"                 % sparkVersion exclude("org.slf4j", "slf4j-log4j12"), //% "provided", //
//      "com.typesafe.akka"           %% "akka-actor"                % akkaVersion,
      "com.typesafe.akka"           %% "akka-stream"               % akkaVersion,
      "com.datastax.spark"          %% "spark-cassandra-connector" % "2.0.2",
      "com.google.protobuf"         %  "protobuf-java"             % "2.5.0",
      "com.typesafe.scala-logging"  %% "scala-logging"             % "3.5.0",
      "ch.qos.logback"              %  "logback-classic"           % "1.1.7",
      "com.stratio.receiver"        %  "spark-rabbitmq"            % "0.5.1",
      "org.apache.hadoop"           %  "hadoop-aws"                % "2.6.0" exclude("tomcat", "jasper-compiler") excludeAll ExclusionRule(organization = "javax.servlet"),
      "com.paulgoldbaum"            %% "scala-influxdb-client"     % "0.5.2"
    )

    // spark influx
    // To cross compile with Dotty and Scala 2
    //crossScalaVersions := Seq(dottyVersion, scala211Version),
    //scalacOptions ++= { if (isDotty.value) Seq("-language:Scala2") else Nil }

  ).settings(
  resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository",
  name := "Core"
).enablePlugins(AssemblyPlugin)

// libraryDependencies

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$") => MergeStrategy.discard
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("reference.conf") => MergeStrategy.concat
  case _ => MergeStrategy.first
}

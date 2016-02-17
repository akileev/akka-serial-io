import sbt.Keys._

name := "akka-serial-io"

organization := "com.github.akileev"

scalaVersion := "2.11.7"

version := "1.0.0"

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

homepage := Some(url("https://github.com/akileev/serial-akka-io"))

scalacOptions ++= Seq("-deprecation")

resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/snapshots"

lazy val akkaVersion = "2.4.1"
lazy val scalaTestVersion = "2.2.6"
lazy val jsscVersion = "2.8.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.scream3r" % "jssc" % jsscVersion,
  "org.scalatest" % "scalatest_2.11" % scalaTestVersion % "test"
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false}

pomExtra :=
  <scm>
    <url>git@github.com:akileev/serial-akka-io</url>
    <connection>scm:git:git@github.com:akileev/serial-akka-io</connection>
  </scm>
  <developers>
    <developer>
      <id>akileev</id>
      <name>Artem Kileev</name>
      <url>https://github.com/akileev</url>
    </developer>
  </developers>
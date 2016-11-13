name := "akka-serial-io"

organization := "com.github.akileev"

crossScalaVersions := Seq("2.11.8", "2.12.0")

scalaVersion := "2.11.8"

version := "1.0.2"

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

homepage := Some(url("https://github.com/akileev/akka-serial-io"))

scalacOptions ++= Seq("-deprecation")

resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/snapshots"

lazy val akkaVersion = "2.4.12"
lazy val scalaTestVersion = "3.0.1"
lazy val jsscVersion = "2.8.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.scream3r" % "jssc" % jsscVersion,
  "org.scalatest" % "scalatest_2.12" % scalaTestVersion % "test"
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
    <url>git@github.com:akileev/akka-serial-io</url>
    <connection>scm:git:git@github.com:akileev/akka-serial-io</connection>
  </scm>
  <developers>
    <developer>
      <id>akileev</id>
      <name>Artem Kileev</name>
      <url>https://github.com/akileev</url>
    </developer>
  </developers>

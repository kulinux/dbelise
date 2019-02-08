import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.dbelise"
ThisBuild / organizationName := "dbelise"

lazy val root = (project in file("."))
  .settings(
    name := "dbelise",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "org.hsqldb" % "hsqldb" % "2.4.1" % Test
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.

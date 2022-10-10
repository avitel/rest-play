import com.typesafe.sbt.packager.docker.DockerChmodType

name := "rest-play"
organization := "com.mycompany"

version := "latest"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.9"

libraryDependencies += guice

libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "org.postgresql" % "postgresql" % "42.2.12",
  jdbc,
  "org.playframework.anorm" %% "anorm" % "2.7.0",
  "org.playframework.anorm" %% "anorm-postgres" % "2.7.0"
)

dockerChmodType := DockerChmodType.UserGroupWriteExecute

Universal / javaOptions ++= Seq(  "-Dpidfile.path=/dev/null")

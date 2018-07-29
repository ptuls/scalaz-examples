import Depend._

lazy val buildSettings = Seq(
  name := "scala-skeleton",
  organization := "com.ptune",
  version := "0.0.1",
  scalaVersion := "2.11.8",
  libraryDependencies := Depend.dependencies,
  mainClass in Compile := Some("com.ptune.MainApp"),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen",
    "-Xfuture",
    "-Yrangepos"
  )
)

lazy val root = (project in
  file(".")).settings(buildSettings: _*)

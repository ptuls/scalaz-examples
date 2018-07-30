import sbt._

object Depend {
  lazy val beustVersion = "1.7.2"
  lazy val lazyLoggerVersion = "3.7.2"
  lazy val logBackVersion = "1.1.2"
  lazy val scalazVersion = "7.2.25"
  lazy val slf4jVersion = "1.7.7"
  lazy val sparkVersion = "2.3.0"

  lazy val betterFiles = Seq(
    "com.github.pathikrit" %% "better-files" % "2.17.1")

  lazy val beustJCommander =
    Seq("com.beust" % "jcommander").map(_ % beustVersion)

  lazy val lazyLogging = Seq(
    "com.typesafe.scala-logging" % "scala-logging_2.11" % s"$lazyLoggerVersion",
    "ch.qos.logback" % "logback-classic" % s"$logBackVersion"
  )

  lazy val scalaz = Seq(
    "org.scalaz" %% "scalaz-core",
    "org.scalaz" %% "scalaz-effect"
  ).map(_ % scalazVersion)

  lazy val slf4j = Seq(
    "org.slf4j" % "slf4j-api" % s"$slf4jVersion",
    "org.slf4j" % "jcl-over-slf4j" % s"$slf4jVersion"
  ).map(_.force())

  lazy val spark = Seq(
    "org.apache.spark" %% "spark-core",
    "org.apache.spark" %% "spark-sql",
    "org.apache.spark" %% "spark-mllib",
    "org.apache.spark" %% "spark-streaming",
    "org.apache.spark" %% "spark-hive"
  ).map(_ % sparkVersion)

  lazy val scalaTestCheck = Seq(
    "org.scalatest" %% "scalatest" % "2.2.4",
    "org.scalacheck" %% "scalacheck" % "1.12.1"
  ).map(_.withSources).map(x => x.force()).map(_ % "test")

  lazy val dependencies =
    betterFiles ++
      lazyLogging ++
      scalaz ++
      slf4j ++
      spark ++
      scalaTestCheck
}

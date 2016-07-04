lazy val commonSettings = Seq(
  organization := "com.artisansoftware",
  version := "0.1.0",
  scalaVersion := "2.11.8"
)

libraryDependencies += "org.twitter4j" % "twitter4j-stream" % "4.0.4"
libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.4"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.0-RC3"
libraryDependencies += "org.scalactic" % "scalactic_2.11" % "3.0.0-RC3"
libraryDependencies += "org.scalamock" % "scalamock-scalatest-support_2.11" % "3.2.2"
libraryDependencies += "org.mockito" % "mockito-all" % "2.0.2-beta"
libraryDependencies += "com.lambdaworks" % "jacks_2.11" % "2.5.2"

lazy val root = (project in file(".")).
  settings(commonSettings: _*)



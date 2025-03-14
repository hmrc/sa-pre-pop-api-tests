ThisBuild / scalaVersion := "3.3.4"

lazy val testSuite = (project in file("."))
  .settings(
    name := "sa-pre-pop-api-tests",
    version := "2.0.0",
    scalacOptions += "-feature",
    libraryDependencies ++= AppDependencies(),
    // The testOptions from SbtAutoBuildPlugin supports only ScalaTest. Resetting testOptions for Cucumber Tests.
    Test / testOptions := Seq.empty
  )

addCommandAlias("scalafmtAll", "all scalafmtSbt scalafmt Test/scalafmt")

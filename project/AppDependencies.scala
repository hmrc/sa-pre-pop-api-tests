import sbt.*

object AppDependencies {

  private lazy val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"   %% "api-test-runner" % "0.9.0",
    "ch.qos.logback" % "logback-classic" % "1.5.17",
    "io.cucumber"   %% "cucumber-scala"  % "8.26.1",
    "io.cucumber"    % "cucumber-junit"  % "7.21.1",
    "com.github.sbt" % "junit-interface" % "0.13.3"
  ).map(_ % Test)

  def apply(): Seq[sbt.ModuleID] = test

}

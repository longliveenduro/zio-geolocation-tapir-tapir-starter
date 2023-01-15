val tapirVersion = "1.2.3"
val zioVersion = "2.0.5"

lazy val rootProject = (project in file(".")).settings(
  Seq(
    name := "zio-geolocation-tapir",
    version := "0.1.0-SNAPSHOT",
    organization := "com.tsystems.toil",
    scalaVersion := "3.2.1",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-test" % zioVersion % Test,
      "dev.zio" %% "zio-test-sbt" % zioVersion % Test
    ),
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )
)

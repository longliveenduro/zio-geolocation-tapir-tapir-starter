val tapirVersion = "1.2.3"
val zioVersion = "2.0.4"

lazy val rootProject = (project in file(".")).settings(
  Seq(
    name := "zio-geolocation-tapir",
    version := "0.1.0-SNAPSHOT",
    organization := "com.tsystems.toil",
    scalaVersion := "3.2.2-RC1",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % tapirVersion,
      "com.softwaremill.sttp.tapir"   %% "tapir-refined" % tapirVersion,
      "ch.qos.logback" % "logback-classic" % "1.4.5",
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
      "dev.zio" %% "zio-test" % zioVersion % Test,
      "dev.zio" %% "zio-test-sbt" % zioVersion % Test,
      "com.softwaremill.sttp.client3" %% "zio-json" % "3.8.3" % Test
    ),
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )
)

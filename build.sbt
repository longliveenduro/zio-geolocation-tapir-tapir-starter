val tapirVersion = "1.1.3"
val zioVersion = "2.0.2"

lazy val rootProject = (project in file(".")).settings(
  Seq(
    name := "zio-geolocation-tapir",
    version := "0.1.0-SNAPSHOT",
    organization := "com.tsystems.toil",
    scalaVersion := "3.2.0",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-refined" % tapirVersion,
      "dev.zio" %% "zio-json-interop-refined" % "0.3.0+18-82a81365+20221027-0927-SNAPSHOT", // TODO local built zio-json version, see https://github.com/zio/zio-json/issues/571, use local built version (use "+ publishLocal") from PR in issue
      "ch.qos.logback" % "logback-classic" % "1.2.11",
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
      "dev.zio" %% "zio-test" % zioVersion % Test,
      "dev.zio" %% "zio-test-sbt" % zioVersion % Test,
      "com.softwaremill.sttp.client3" %% "zio-json" % "3.7.1" % Test
    ),
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )
)

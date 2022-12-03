package com.tsystems.toil

import org.slf4j.LoggerFactory
import sttp.tapir.server.interceptor.log.DefaultServerLog
import sttp.tapir.server.ziohttp.{ZioHttpInterpreter, ZioHttpServerOptions}
import zio.http.{HttpApp, Server, ServerConfig}
import zio.*

object Main extends ZIOAppDefault:
  val log = LoggerFactory.getLogger(ZioHttpInterpreter.getClass.getName)

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    val serverOptions: ZioHttpServerOptions[Any] =
      ZioHttpServerOptions.customiseInterceptors
        .serverLog(
          DefaultServerLog[Task](
            doLogWhenReceived = msg => ZIO.succeed(log.debug(msg)),
            doLogWhenHandled = (msg, error) => ZIO.succeed(error.fold(log.debug(msg))(err => log.debug(msg, err))),
            doLogAllDecodeFailures = (msg, error) => ZIO.succeed(error.fold(log.debug(msg))(err => log.debug(msg, err))),
            doLogExceptions = (msg: String, ex: Throwable) => ZIO.succeed(log.debug(msg, ex)),
            noLog = ZIO.succeed(())
          )
        )
        .metricsInterceptor(Endpoints.prometheusMetrics.metricsInterceptor())
        .options
    val app: HttpApp[Any, Throwable] = ZioHttpInterpreter(serverOptions).toHttp(Endpoints.all)

    for
      server <- Server
                .serve(app)
                .provide(
                  ServerConfig.live,
                  Server.live
                )
                .exitCode
      _ <- Console.printLine("Go to http://localhost:8080/docs to open SwaggerUI. Press ENTER key to exit.")
    yield server

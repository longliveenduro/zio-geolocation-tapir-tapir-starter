package com.tsystems.toil

import sttp.tapir.{PublicEndpoint, Schema, endpoint, query, stringBody}
import Library.*
import eu.timepit.refined.*
import eu.timepit.refined.api.Refined
import eu.timepit.refined.api.RefType
import eu.timepit.refined.auto.*
import eu.timepit.refined.numeric.*
import eu.timepit.refined.collection.*
import com.tsystems.toil.RefinedSupport.*
import sttp.tapir.codec.refined.*

import java.util.concurrent.atomic.AtomicReference
import sttp.tapir.Codec.JsonCodec
import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.*
import zio.Task
import zio.ZIO
import zio.json.*
import zio.json.interop.refined.*

object Endpoints {
  case class User(name: String) extends AnyVal
  val helloEndpoint: PublicEndpoint[User, Unit, String, Any] = endpoint.get
    .in("hello")
    .in(query[User]("name"))
    .out(stringBody)
  val helloServerEndpoint: ZServerEndpoint[Any, Any] = helloEndpoint.serverLogicSuccess(user => ZIO.succeed(s"Hello ${user.name}"))

  implicit val publicationZioEncoder: zio.json.JsonEncoder[Publication] = DeriveJsonEncoder.gen[Publication]
  implicit val publicationZioDecoder: zio.json.JsonDecoder[Publication] = DeriveJsonDecoder.gen[Publication]
  implicit val authorZioEncoder: zio.json.JsonEncoder[Author] = DeriveJsonEncoder.gen[Author]
  implicit val authorZioDecoder: zio.json.JsonDecoder[Author] = DeriveJsonDecoder.gen[Author]
  implicit val directorZioEncoder: zio.json.JsonEncoder[Director] = DeriveJsonEncoder.gen[Director]
  implicit val directorZioDecoder: zio.json.JsonDecoder[Director] = DeriveJsonDecoder.gen[Director]
  implicit val bookZioEncoder: zio.json.JsonEncoder[Book] = DeriveJsonEncoder.gen[Book]
  implicit val bookZioDecoder: zio.json.JsonDecoder[Book] = DeriveJsonDecoder.gen[Book]
  implicit val movieZioEncoder: zio.json.JsonEncoder[Movie] = DeriveJsonEncoder.gen[Movie]
  implicit val movieZioDecoder: zio.json.JsonDecoder[Movie] = DeriveJsonDecoder.gen[Movie]
//  given Schema[Publication] = Schema.oneOfWrapped[Publication]

  val publicationsListing: PublicEndpoint[Unit, Unit, List[Publication], Any] = endpoint.get
    .in("publications" / "list" / "all")
    .out(jsonBody[List[Publication]])
  val publicationsListingServerEndpoint: ZServerEndpoint[Any, Any] = publicationsListing.serverLogicSuccess(_ => ZIO.succeed(publications.get()))

  val prometheusMetrics: PrometheusMetrics[Task] = PrometheusMetrics.default[Task]()
  val metricsEndpoint: ZServerEndpoint[Any, Any] = prometheusMetrics.metricsEndpoint

  val docEndpoints: List[ZServerEndpoint[Any, Any]] =
    SwaggerInterpreter().fromEndpoints[Task](List(helloEndpoint, metricsEndpoint.endpoint, publicationsListing), "zio-geolocation-tapir", "1.0.0")

  val all: List[ZServerEndpoint[Any, Any]] = List(helloServerEndpoint, publicationsListingServerEndpoint, metricsEndpoint) ++ docEndpoints
}

object Library {
  case class Author(name: String)
  case class Director(name: String)

  sealed trait Publication
  case class Book(title: String, year: Int Refined Positive, author: Author) extends Publication
  case class Movie(title: String, year: Int Refined Positive, director: Director) extends Publication

  val publications = new AtomicReference(
    List(
      Book("The Sorrows of Young Werther", as[Int Refined Positive](1774), Author("Johann Wolfgang von Goethe")),
      Book("Nad Niemnem", as[Int Refined Positive](1888), Author("Eliza Orzeszkowa")),
      Book("The Art of Computer Programming", as[Int Refined Positive](1968), Author("Donald Knuth")),
      Book("Pharaoh", as[Int Refined Positive](1897), Author("Boleslaw Prus")),
      Movie("Solaris", as[Int Refined Positive](2002), Director("Steven Soderbergh"))
    )
  )
}

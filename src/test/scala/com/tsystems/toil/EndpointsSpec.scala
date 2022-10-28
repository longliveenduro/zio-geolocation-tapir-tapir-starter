package com.tsystems.toil

import com.tsystems.toil.Endpoints._
import sttp.client3.testing.SttpBackendStub
import sttp.client3.{UriContext, basicRequest}
import sttp.tapir.server.stub.TapirStubInterpreter
import zio.test.Assertion._
import zio.test.{ZIOSpecDefault, assertZIO}

import Library._
import sttp.client3.ziojson._
import sttp.tapir.ztapir.RIOMonadError

object EndpointsSpec extends ZIOSpecDefault {
  def spec = suite("Endpoints spec")(
    test("return hello message") {
      // given
      val backendStub = TapirStubInterpreter(SttpBackendStub(new RIOMonadError[Any]))
        .whenServerEndpoint(helloServerEndpoint)
        .thenRunLogic()
        .backend()

      // when
      val response = basicRequest
        .get(uri"http://test.com/hello?name=adam")
        .send(backendStub)

      // then
      assertZIO(response.map(_.body))(isRight(equalTo("Hello adam")))
    },
    test("list available books") {
      // given
      val backendStub = TapirStubInterpreter(SttpBackendStub(new RIOMonadError[Any]))
        .whenServerEndpoint(publicationsListingServerEndpoint)
        .thenRunLogic()
        .backend()

      // when
      val response = basicRequest
        .get(uri"http://test.com/publications/list/all")
        .response(asJson[List[Publication]])
        .send(backendStub)

      // then
      assertZIO(response.map(_.body))(isRight(equalTo(publications.get())))
    }
  )
}

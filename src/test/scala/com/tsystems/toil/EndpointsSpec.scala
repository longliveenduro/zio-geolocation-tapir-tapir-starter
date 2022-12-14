package com.tsystems.toil

import com.tsystems.toil.Endpoints.*
import sttp.client3.testing.SttpBackendStub
import sttp.client3.{UriContext, basicRequest}
import sttp.tapir.server.stub.TapirStubInterpreter
import zio.test.Assertion.*
import zio.test.{ZIOSpecDefault, assertZIO}
import Library.*
import com.tsystems.toil.RefinedTypes.UtmZone
import sttp.client3.ziojson.*
import sttp.tapir.EndpointOutput.StatusCode
import sttp.tapir.ztapir.RIOMonadError
import zio.test.*

object EndpointsSpec extends ZIOSpecDefault {
  val thirtyTwoZone = UtmZone.unsafeFrom(32)
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
      for r <- response
      yield
        val code = r.code
        assertTrue(r.body == Right("Hello adam") && BooleanClass.aBoolean == false)
    },
    test("list available books") {
      // given
      val backendStub = TapirStubInterpreter(SttpBackendStub(new RIOMonadError[Any]))
        .whenServerEndpoint(booksListingServerEndpoint)
        .thenRunLogic()
        .backend()

      // when
      val response = basicRequest
        .get(uri"http://test.com/books/list/all")
        .response(asJson[List[Book]])
        .send(backendStub)

      // then
      assertZIO(response.map(_.body))(isRight(equalTo(books.get())))
    }
  )
}

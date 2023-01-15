package com.tsystems.toil

import zio.test.Assertion.*
import zio.test.*

object BooleanSpec extends ZIOSpecDefault {
  def spec = suite("Java lang boolean problem")(
    test("return hello message") {
        assertTrue(BooleanClass.aBoolean == false)
    }
  )
}

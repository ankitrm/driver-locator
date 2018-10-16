package com.xyz.transport.driverlocator.models.http

import scala.collection.immutable.Seq

import org.scalatest.{Matchers, WordSpec}

/**
  * Unit test of [[ErrorResult]]
  */
class ErrorResultSpec extends WordSpec with Matchers {

  "ErrorResult" should {
    "create a new instance" when {
      "a sequence of string is passed" in {
        ErrorResult(Seq("test string"))
      }
    }
  }
}

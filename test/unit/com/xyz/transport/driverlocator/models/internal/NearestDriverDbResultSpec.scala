package com.xyz.transport.driverlocator.models.internal

import scala.collection.immutable.Seq

import com.xyz.transport.driverlocator.util.BaseTest

class NearestDriverDbResultSpec extends BaseTest {
  "Constructor" should {
    "be able to create a new instance" in {
      NearestDriverDbResult(1L, Geoloc(Seq(1.0, 2.0)), 1.0, 2.0)
    }
  }
}

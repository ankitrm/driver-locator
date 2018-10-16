package com.xyz.transport.driverlocator.models.internal

import scala.collection.immutable.Seq

import com.xyz.transport.driverlocator.models.http.put.DriverLocationRequestBody
import com.xyz.transport.driverlocator.util.BaseTest

class DriverLocationSpec extends BaseTest {

  "Constructor" should {
    "create new instance" when {
      "geolocation, id and accuracy are passed" in {
        DriverLocation(testGeoloc, testId, testAccuracy)
      }
      "driver location id and drive location request body are passed" in {
        DriverLocation(testId, testDriverLocationRequestBody) shouldBe DriverLocation(testGeoloc, testId, testAccuracy)
      }
    }
  }

  private val testLatitude = 89.0
  private val testLongitude = 178.0
  private val testId = 1L
  private val testAccuracy = .9
  private val testGeoloc = Geoloc(Seq(testLongitude, testLatitude))
  private val testDriverLocationRequestBody = DriverLocationRequestBody(testLatitude, testLongitude, testAccuracy)
}

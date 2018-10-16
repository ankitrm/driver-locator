package com.xyz.transport.driverlocator.models.internal

import scala.collection.immutable.Seq

import com.xyz.transport.driverlocator.models.http.get.NearestDriverResponse
import com.xyz.transport.driverlocator.util.BaseTest

class NearestDriverDbResultsSpec extends BaseTest {

  "Constructor" should {
    "be able to create a new instance" in {
      NearestDriverDbResults(Seq(testNearestDriverDbResult))
    }
  }
  "convertToNearestDistanceResponses" should {
    "be able to convert NearestDriverDbResults to NearestDriverResponses" in {
      NearestDriverDbResults(Seq(testNearestDriverDbResult))
        .convertToNearestDistanceResponses shouldBe Seq(testNearestDistanceResponse)
    }
  }

  private val testDistance = 2.0
  private val testId = 1L
  private val testLatitude = 1.0
  private val testLongitude = 2.0
  private val testNearestDriverDbResult = NearestDriverDbResult(testId,
    Geoloc(Seq(testLongitude, testLatitude)), 1.0, testDistance)
  private val testNearestDistanceResponse = NearestDriverResponse(testDistance, testId, testLatitude, testLongitude)
}

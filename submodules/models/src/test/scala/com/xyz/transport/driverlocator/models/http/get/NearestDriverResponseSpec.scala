package com.xyz.transport.driverlocator.models.http.get

import org.scalatest.{Matchers, WordSpec}

/**
  * Unit test of [[NearestDriverResponse]]
  */
class NearestDriverResponseSpec extends WordSpec with Matchers {

  "isValid" should {
    "return true with no errors" when {
      "valid parameters are passed" in {
        val nearestDriverResponse = NearestDriverResponse(validDistance, validId, validLatitude, validLongitude)

        nearestDriverResponse.isValid shouldBe true
        nearestDriverResponse.errorResults.errors.isEmpty shouldBe true
      }
    }

    "return false with errors" when {
      "latitude > 90 is passed" in {
        val invalidDriverResponse = NearestDriverResponse(validDistance, validId, 91.0, validLongitude)

        assertInvalidResponse(invalidDriverResponse, "Latitude")
      }
      "latitude < -90 is passed" in {
        val invalidDriverResponse = NearestDriverResponse(validDistance, validId, -91.0, validLongitude)

        assertInvalidResponse(invalidDriverResponse, "Latitude")
      }
      "longitude > 180 is passed" in {
        val invalidDriverResponse = NearestDriverResponse(validDistance, validId, validLatitude, 181.0)

        assertInvalidResponse(invalidDriverResponse, "Longitude")
      }
      "longitude < 180  is passed" in {
        val invalidDriverResponse = NearestDriverResponse(validDistance, validId, validLatitude, -181.0)

        assertInvalidResponse(invalidDriverResponse, "Longitude")
      }
      "invalid distance is passed" in {
        val invalidDriverResponse = NearestDriverResponse(-1, validId, validLatitude, validLongitude)

        assertInvalidResponse(invalidDriverResponse, "Distance")
      }
      "invalid driver id is passed" in {
        val invalidDriverResponse = NearestDriverResponse(validDistance, -1, validLatitude, validLongitude)

        assertInvalidResponse(invalidDriverResponse, "Id")
      }
    }
  }

  private def assertInvalidResponse(invalidDriverResponse: NearestDriverResponse, invalidField: String): Unit = {
    invalidDriverResponse.isValid shouldBe false
    invalidDriverResponse.errorResults.errors.isEmpty shouldBe false
    invalidDriverResponse.errorResults.errors.head should include(invalidField)
  }

  private val validDistance = 3
  private val validId = 1
  private val validLongitude = 179.0
  private val validLatitude = 89.0
}

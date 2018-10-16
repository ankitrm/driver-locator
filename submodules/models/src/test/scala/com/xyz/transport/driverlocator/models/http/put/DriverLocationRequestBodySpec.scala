package com.xyz.transport.driverlocator.models.http.put

import org.scalatest.{Matchers, WordSpec}

/**
  * Unit test of [[DriverLocationRequestBody]]
  */
class DriverLocationRequestBodySpec extends WordSpec with Matchers {

  "isValid" should {
    "return true with no errors" when {
      "valid parameters are passed" in {
        val driverLocationRequestBody = DriverLocationRequestBody(validLatitude, validLongitude, validAccuracy)

        driverLocationRequestBody.isValid shouldBe true
        driverLocationRequestBody.errorResults.errors.isEmpty shouldBe true
      }
    }

    "return false with errors" when {
      "latitude > 90 is passed" in {
        val invalidDriverLocationRequestBody = DriverLocationRequestBody(91.0, validLongitude, validAccuracy)

        assertInvalidResponse(invalidDriverLocationRequestBody, "Latitude")
      }
      "latitude < -90 is passed" in {
        val invalidDriverLocationRequestBody = DriverLocationRequestBody(-91.0, validLongitude, validAccuracy)

        assertInvalidResponse(invalidDriverLocationRequestBody, "Latitude")
      }
      "longitude > 180 is passed" in {
        val invalidDriverLocationRequestBody = DriverLocationRequestBody(validLatitude, 181.0, validAccuracy)

        assertInvalidResponse(invalidDriverLocationRequestBody, "Longitude")
      }
      "longitude < 180  is passed" in {
        val invalidDriverLocationRequestBody = DriverLocationRequestBody(validLatitude, -181.0, validAccuracy)

        assertInvalidResponse(invalidDriverLocationRequestBody, "Longitude")
      }
    }
  }

  private def assertInvalidResponse(invalidBody: DriverLocationRequestBody, invalidField: String): Unit = {
    invalidBody.isValid shouldBe false
    invalidBody.errorResults.errors.isEmpty shouldBe false
    invalidBody.errorResults.errors.head should include(invalidField)
  }

  private val validAccuracy = 3
  private val validLongitude = 179.0
  private val validLatitude = 89.0
}

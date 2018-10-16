package com.xyz.transport.driverlocator.models.http.get

import org.scalatest.{Matchers, WordSpec}

/**
  * Unit test of [[NearestDriverRequest]]
  */
class NearestDriverRequestSpec extends WordSpec with Matchers {

  "isValid" should {
    "return true with no errors" when {
      "valid parameters are passed" in {
        val nearestDriverRequest = NearestDriverRequest(validRadius, validLimit, validLatitude, validLongitude)

        nearestDriverRequest.isValid shouldBe true
        nearestDriverRequest.errorResults.errors.isEmpty shouldBe true
      }
    }

    "return false with errors" when {
      "latitude > 90 is passed" in {
        val invalidDriverRequest = NearestDriverRequest(validRadius, validLimit, 91.0, validLongitude)

        assertInvalidRequest(invalidDriverRequest, "Latitude")
      }
      "latitude < -90 is passed" in {
        val invalidDriverRequest = NearestDriverRequest(validRadius, validLimit, -91.0, validLongitude)

        assertInvalidRequest(invalidDriverRequest, "Latitude")
      }
      "longitude > 180 is passed" in {
        val invalidDriverRequest = NearestDriverRequest(validRadius, validLimit, validLatitude, 181.0)

        assertInvalidRequest(invalidDriverRequest, "Longitude")
      }
      "longitude < 180  is passed" in {
        val invalidDriverRequest = NearestDriverRequest(validRadius, validLimit, validLatitude, -181.0)

        assertInvalidRequest(invalidDriverRequest, "Longitude")
      }
      "invalid radius is passed" in {
        val invalidDriverRequest = NearestDriverRequest(-1, validLimit, validLatitude, validLongitude)

        assertInvalidRequest(invalidDriverRequest, "Radius")
      }
      "invalid limit is passed" in {
        val invalidDriverRequest = NearestDriverRequest(validRadius, -1, validLatitude, validLongitude)

        assertInvalidRequest(invalidDriverRequest, "Limit")
      }
    }
  }

  private def assertInvalidRequest(invalidDriverRequest: NearestDriverRequest, invalidField: String): Unit = {
    invalidDriverRequest.isValid shouldBe false
    invalidDriverRequest.errorResults.errors.isEmpty shouldBe false
    invalidDriverRequest.errorResults.errors.head should include(invalidField)
  }

  private val validRadius = 3
  private val validLimit = 1
  private val validLongitude = 179.0
  private val validLatitude = 89.0
}

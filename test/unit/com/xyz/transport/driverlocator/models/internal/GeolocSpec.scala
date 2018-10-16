package com.xyz.transport.driverlocator.models.internal

import scala.collection.immutable.Seq

import com.xyz.transport.driverlocator.util.BaseTest

case class GeolocSpec(coordinates: Seq[Double], `type`: String = Geoloc.GeolocType) extends BaseTest {

  "Constructor" should {
    "create a new instance" when {
      "valid parameters are passed" in {
        Geoloc(Seq(testLatitude, testLongitude))
      }

      "throw an illegal argument exception" when {
        "more than two entities are passed" in {
          an[IllegalArgumentException] shouldBe thrownBy(Geoloc(Seq(testLatitude, testLongitude, testLatitude)))
        }
        "latitude < -90 is passed" in {
          an[IllegalArgumentException] shouldBe thrownBy(Geoloc(Seq(-91.0, testLongitude)))
        }
        "latitude > 90 is passed" in {
          an[IllegalArgumentException] shouldBe thrownBy(Geoloc(Seq(91.0, testLongitude)))
        }
        "longitude < -180 is passed" in {
          an[IllegalArgumentException] shouldBe thrownBy(Geoloc(Seq(testLatitude, -181.0)))
        }
        "longitude > 180 is passed" in {
          an[IllegalArgumentException] shouldBe thrownBy(Geoloc(Seq(testLatitude, 181.0)))
        }
      }
    }
  }

  private val testLatitude = 89.0
  private val testLongitude = 178.0
}

package com.xyz.transport.driverlocator.marshalling.impl

import com.xyz.transport.driverlocator.models.http.ErrorResult
import com.xyz.transport.driverlocator.models.http.get.NearestDriverResponse
import com.xyz.transport.driverlocator.models.http.put.DriverLocationRequestBody
import com.xyz.transport.driverlocator.models.internal.{DriverLocation, NearestDriverDbResult}
import com.xyz.transport.driverlocator.util.{BaseTest, FileUtils}

import play.api.libs.json.{Json, OFormat}

/**
  * Unit tests of [[PlayBasedDriverLocatorJsonFormatters]]
  */
class PlayBasedDriverLocatorJsonFormattersSpec extends BaseTest {

  "DriverLocation converter" should {
    "convert Json to object and vice versa" in {
      implicit val driverLocationFormatter: OFormat[DriverLocation] =
        PlayBasedDriverLocatorJsonFormatters.driverLocationFormatter

      val testDriverLocation = FileUtils.fileToInstance[DriverLocation]("fixtures/driver_location.json")
      Json.toJson(testDriverLocation)
    }
  }

  "NearestDriverDbResult converter" should {
    "convert Json to object and vice versa" in {
      implicit val nearestDriverDbResultFormatter: OFormat[NearestDriverDbResult] =
        PlayBasedDriverLocatorJsonFormatters.nearestDriverDbResultFormatter

      val testNearestDriverDbResult =
        FileUtils.fileToInstance[NearestDriverDbResult]("fixtures/nearest_driver_db_result.json")
      Json.toJson(testNearestDriverDbResult)
    }
  }

  "ErrorResult converter" should {
    "convert Json to object and vice versa" in {
      implicit val errorResultFormatter: OFormat[ErrorResult] =
        PlayBasedDriverLocatorJsonFormatters.errorResultJsonFormatter

      val testErrorResult = FileUtils.fileToInstance[ErrorResult]("fixtures/error_result.json")
      Json.toJson(testErrorResult)
    }
  }

  "NearestDriverResponse converter" should {
    "convert Json to object and vice versa" in {
        implicit val nearestDriverResponseFormatter: OFormat[NearestDriverResponse] =
        PlayBasedDriverLocatorJsonFormatters.nearestDriverResponseFormatter

      val testNearestDriverResponse =
        FileUtils.fileToInstance[NearestDriverResponse]("fixtures/nearest_driver_response.json")
      Json.toJson(testNearestDriverResponse)
    }
  }

  "DriverLocationRequestBody converter" should {
    "convert Json to object and vice versa" in {
      implicit val driverLocationRequestBodyFormatter: OFormat[DriverLocationRequestBody] =
        PlayBasedDriverLocatorJsonFormatters.driverLocationRequestFormatter

      val testDriverLocationRequestBody =
        FileUtils.fileToInstance[DriverLocationRequestBody]("fixtures/driver_location_request_body.json")
      Json.toJson(testDriverLocationRequestBody)
    }
  }
}

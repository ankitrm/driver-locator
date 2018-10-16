package com.xyz.transport.driverlocator.controllers

import javax.inject.Singleton

import scala.collection.immutable.Seq
import scala.concurrent.Future

import com.xyz.transport.driverlocator.marshalling.impl.PlayBasedDriverLocatorJsonFormatters
import com.xyz.transport.driverlocator.models.http.get.{NearestDriverRequest, NearestDriverResponse}
import com.xyz.transport.driverlocator.models.http.put.DriverLocationRequestBody
import com.xyz.transport.driverlocator.models.internal.DriverLocation
import com.xyz.transport.driverlocator.services.DriverLocationService
import com.xyz.transport.driverlocator.util.{ControllerBaseTest, FileUtils}

import org.mockito.Mockito.{reset, when}
import play.api.http.Status.{NOT_FOUND, OK, UNPROCESSABLE_ENTITY}
import play.api.libs.json.OFormat
import play.api.mvc.Result
import play.api.test.Helpers.{contentAsJson, contentAsString, status}

@Singleton
class DriverLocationControllerSpec extends ControllerBaseTest {

  before {
    reset(mockDriverLocationService)
  }

  private implicit val driverLocationRequestFormatter: OFormat[DriverLocationRequestBody] =
    PlayBasedDriverLocatorJsonFormatters.driverLocationRequestFormatter

  "saveDriverLocation" should {
    "return ok response" when {
      "valid request is passed" in {
        when(mockDriverLocationService.saveOrUpdate(testDriverLocation)).thenReturn(Future.successful())

        val result: Future[Result] =
          makePutRequestWithJsonBody(controller.saveDriverLocation(testId), testDriverLocationRequestBody)

        status(result) shouldBe OK
        contentAsString(result) shouldBe ""
      }
    }
    "return NotFound" when {
      "an illegal argument exception is thrown" in {
        when(mockDriverLocationService.saveOrUpdate(testDriverLocation))
          .thenReturn(Future.failed(new IllegalArgumentException("test-exception")))

        val result: Future[Result] =
          makePutRequestWithJsonBody(controller.saveDriverLocation(testId), testDriverLocationRequestBody)

        status(result) shouldBe NOT_FOUND
      }
    }
    "return Unprocessable Entity" when {
      "invalid request is passed" in {
        val invalidLatitude = -91
        val testInvalidDriverLocationRequestBody = DriverLocationRequestBody(invalidLatitude, 2, 3)

        val result: Future[Result] =
          makePutRequestWithJsonBody(controller.saveDriverLocation(testId), testInvalidDriverLocationRequestBody)

        status(result) shouldBe UNPROCESSABLE_ENTITY
      }
    }
  }

  "getNearestDrivers" should {
    "return ok response" when {
      "valid response is passed" in {
        when(mockDriverLocationService.getNearest(testNearestDriverRequest))
          .thenReturn(Future(Seq(testNearestDriverResponse)))

        val result: Future[Result] =
          makeRequest(controller.getNearestDrivers(testLatitude, testLongitude, testLimit, testRadius))

        status(result) shouldBe OK
        contentAsJson(result) shouldBe FileUtils.fileToJson("fixtures/nearest_driver_controller_response.json")
      }
    }
    "return unprocessable entity response" when {
      "valid response is passed" in {
        val result: Future[Result] =
          makeRequest(controller.getNearestDrivers(testLatitude, testLongitude, -1, testRadius))

        status(result) shouldBe UNPROCESSABLE_ENTITY
      }
    }
  }

  private val testId = 1L
  private val testLatitude = 1
  private val testLongitude = 2
  private val testLimit = 1
  private val testRadius = 5
  private val testDriverLocationRequestBody = DriverLocationRequestBody(testLatitude, testLongitude, 3)
  private val testDriverLocation = DriverLocation(testId, testDriverLocationRequestBody)

  private val testNearestDriverRequest = NearestDriverRequest(testRadius, testLimit, testLatitude, testLongitude)
  private val testNearestDriverResponse = NearestDriverResponse(3, testLimit, testLatitude, testLongitude)

  private val mockDriverLocationService = mock[DriverLocationService]
  private val controller = new DriverLocationController(stubControllerComponents(),
    PlayBasedDriverLocatorJsonFormatters, mockDriverLocationService)
}

package com.xyz.transport.driverlocator.services.impl

import scala.collection.immutable.Seq
import scala.concurrent.Future

import com.xyz.transport.driverlocator.dao.DriverLocationDao
import com.xyz.transport.driverlocator.models.http.get.{NearestDriverRequest, NearestDriverResponse}
import com.xyz.transport.driverlocator.models.internal.{
  DriverLocation, Geoloc, NearestDriverDbResult, NearestDriverDbResults
}
import com.xyz.transport.driverlocator.util.BaseTest

import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar

class DriverLocationServiceImplSpec extends BaseTest with MockitoSugar {

  "saveOrUpdate" should {
    "call dao with same request" in {
      val mockDriverLocationDao = mock[DriverLocationDao]
      when(mockDriverLocationDao.insertOrUpdate(testDriverLocation)).thenReturn(Future(testDriverLocation))
      val driverLocationService = new DriverLocationServiceImpl(mockDriverLocationDao)

      awaitResult(driverLocationService.saveOrUpdate(testDriverLocation)).isInstanceOf[Unit] shouldBe true
    }
  }

  "getNearest" should {
    "call dao with same request" in {
      val mockDriverLocationDao = mock[DriverLocationDao]
      when(mockDriverLocationDao.getNearestResults(testNearestDriverRequest))
        .thenReturn(Future(testNearestDriverDbResult))
      val driverLocationService = new DriverLocationServiceImpl(mockDriverLocationDao)

      awaitResult(driverLocationService.getNearest(testNearestDriverRequest)) shouldBe Seq(testNearestDriverResponse)
    }
  }
  private val testDriverLocation = DriverLocation(Geoloc(Seq(2,1)),3,2)
  private val testNearestDriverRequest = NearestDriverRequest(1,2,3,2)
  private val testNearestDriverResponse = NearestDriverResponse(2,1,1,2)
  private val testNearestDriverDbResult = NearestDriverDbResults(Seq(NearestDriverDbResult(1,Geoloc(Seq(2, 1)),3,2)))
}

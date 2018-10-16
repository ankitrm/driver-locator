package com.xyz.transport.driverlocator.dao.impl

import scala.collection.immutable.Seq

import com.xyz.transport.driverlocator.marshalling.impl.PlayBasedDriverLocatorJsonFormatters
import com.xyz.transport.driverlocator.models.http.get.NearestDriverRequest
import com.xyz.transport.driverlocator.models.http.put.DriverLocationRequestBody
import com.xyz.transport.driverlocator.models.internal.{
  DriverLocation, Geoloc, NearestDriverDbResult, NearestDriverDbResults
}
import com.xyz.transport.driverlocator.util.BaseMongoTest

import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import play.api.Configuration

class MongoBasedDriverLocationDaoSpec extends BaseMongoTest with MockitoSugar {

  "insertOrUpdate" should {
    "throw an illegal argument exception" when {
      "driverId > maximum upper limit is passed" in {
        val invalidDriverId = 5
        val testInvalidDriverLocation = DriverLocation(invalidDriverId, testDriverLocationRequestBody)

        an[IllegalArgumentException] shouldBe thrownBy(dao.insertOrUpdate(testInvalidDriverLocation))
      }
    }

    "insert a new driver location" in {
      awaitResult(dao.insertOrUpdate(testDriverLocation)) shouldBe testDriverLocation
      count(MongoBasedDriverLocationDao.CollectionName) shouldBe 1
    }
    "update existing driver location" in {
      val testDriverLocationUpdate = testDriverLocation.copy(accuracy = 1.1)
      awaitResult(dao.insertOrUpdate(testDriverLocation))

      awaitResult(dao.insertOrUpdate(testDriverLocationUpdate)) shouldBe testDriverLocationUpdate
      count(MongoBasedDriverLocationDao.CollectionName) shouldBe 1
    }
  }

  "getNearestResults" should {
    "be able to get nearest drivers" in {
      val requestLatitude = 1.01
      val requestLongitude = 2
      val testNearestDriverDbResults = NearestDriverDbResults(Seq(NearestDriverDbResult(testDriverLocation.id,
        Geoloc(Seq[Double](2, 1)), testDriverLocation.accuracy, 1113.188450214498)))
      awaitResult(dao.insertOrUpdate(testDriverLocation))

      val actualNearestDrivers =
        awaitResult(dao.getNearestResults(NearestDriverRequest(10*1000, 1, requestLatitude, requestLongitude)))

      actualNearestDrivers shouldBe testNearestDriverDbResults
    }
  }

  private val testDriverLocationRequestBody = DriverLocationRequestBody(1, 2, .5)
  private val testDriverLocation = DriverLocation(1, testDriverLocationRequestBody)
  private def dao = {
    val conf = mock[Configuration]
    when(conf.get[Long](MongoBasedDriverLocationDao.DriverIdLowerLimitKey)).thenReturn(0)
    when(conf.get[Long](MongoBasedDriverLocationDao.DriverIdUpperLimitKey)).thenReturn(3)
    new MongoBasedDriverLocationDao(reactiveMongoApi, PlayBasedDriverLocatorJsonFormatters, conf)
  }
}



package com.xyz.transport.driverlocator.dao

import scala.concurrent.Future

import com.xyz.transport.driverlocator.models.http.get.NearestDriverRequest
import com.xyz.transport.driverlocator.models.internal.{DriverLocation, NearestDriverDbResults}

trait DriverLocationDao {

  def insertOrUpdate(driverLocation: DriverLocation): Future[DriverLocation]

  def getNearestResults(nearestDriverRequest: NearestDriverRequest): Future[NearestDriverDbResults]
}

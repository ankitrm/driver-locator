package com.xyz.transport.driverlocator.services

import scala.collection.immutable.Seq
import scala.concurrent.Future

import com.xyz.transport.driverlocator.models.http.get.{NearestDriverRequest, NearestDriverResponse}
import com.xyz.transport.driverlocator.models.internal.DriverLocation

trait DriverLocationService {

  def saveOrUpdate(driverLocation: DriverLocation): Future[Unit]

  def getNearest(nearestDriverRequest: NearestDriverRequest): Future[Seq[NearestDriverResponse]]
}

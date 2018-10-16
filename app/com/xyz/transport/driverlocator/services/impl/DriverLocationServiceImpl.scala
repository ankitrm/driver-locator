package com.xyz.transport.driverlocator.services.impl

import javax.inject.{Inject, Singleton}

import scala.collection.immutable.Seq
import scala.concurrent.{ExecutionContext, Future}

import com.xyz.transport.driverlocator.dao.DriverLocationDao
import com.xyz.transport.driverlocator.models.http.get.{NearestDriverRequest, NearestDriverResponse}
import com.xyz.transport.driverlocator.models.internal.DriverLocation
import com.xyz.transport.driverlocator.services.DriverLocationService

@Singleton
class DriverLocationServiceImpl @Inject()(driverLocationDao: DriverLocationDao)
                                         (implicit ctx: ExecutionContext) extends DriverLocationService {
  override def saveOrUpdate(driverLocation: DriverLocation): Future[Unit] =
    driverLocationDao.insertOrUpdate(driverLocation).map(_ => Unit)


  override def getNearest(nearestDriverRequest: NearestDriverRequest): Future[Seq[NearestDriverResponse]] =
    driverLocationDao.getNearestResults(nearestDriverRequest).map(_.convertToNearestDistanceResponses)
}

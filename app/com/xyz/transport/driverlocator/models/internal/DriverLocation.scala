package com.xyz.transport.driverlocator.models.internal

import scala.collection.immutable.Seq

import com.xyz.transport.driverlocator.models.http.put.DriverLocationRequestBody

case class DriverLocation(geoloc: Geoloc, id: Long, accuracy: Double)

object DriverLocation {
  def apply(driverLocationId: Long, driverLocationRequest: DriverLocationRequestBody): DriverLocation = {
    DriverLocation(
      Geoloc(Seq(driverLocationRequest.longitude, driverLocationRequest.latitude)),
      driverLocationId,
      driverLocationRequest.accuracy
    )
  }
}

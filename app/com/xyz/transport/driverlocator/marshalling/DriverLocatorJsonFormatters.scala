package com.xyz.transport.driverlocator.marshalling

import scala.language.higherKinds

import com.xyz.transport.driverlocator.models.http.ErrorResult
import com.xyz.transport.driverlocator.models.http.get.NearestDriverResponse
import com.xyz.transport.driverlocator.models.http.put.DriverLocationRequestBody
import com.xyz.transport.driverlocator.models.internal.{DriverLocation, NearestDriverDbResult}

trait DriverLocatorJsonFormatters[JsonFormat[_]] {
  def driverLocationFormatter: JsonFormat[DriverLocation]

  def driverLocationRequestFormatter: JsonFormat[DriverLocationRequestBody]

  def errorResultJsonFormatter: JsonFormat[ErrorResult]

  def nearestDriverResponseFormatter: JsonFormat[NearestDriverResponse]

  def nearestDriverDbResultFormatter: JsonFormat[NearestDriverDbResult]

}

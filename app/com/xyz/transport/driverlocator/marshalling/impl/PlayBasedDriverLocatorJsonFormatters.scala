package com.xyz.transport.driverlocator.marshalling.impl

import com.xyz.transport.driverlocator.marshalling.DriverLocatorJsonFormatters
import com.xyz.transport.driverlocator.models.http.ErrorResult
import com.xyz.transport.driverlocator.models.http.get.NearestDriverResponse
import com.xyz.transport.driverlocator.models.http.put.DriverLocationRequestBody
import com.xyz.transport.driverlocator.models.internal.{DriverLocation, Geoloc, NearestDriverDbResult}

import play.api.libs.json.{Json, OFormat}

/**
  * Implementation of [[DriverLocatorJsonFormatters]] using Play JSON formats.
  */
object PlayBasedDriverLocatorJsonFormatters extends DriverLocatorJsonFormatters[OFormat] {

  private implicit val geolocJsonFormatter: OFormat[Geoloc] = Json.format[Geoloc]
  override implicit val driverLocationFormatter: OFormat[DriverLocation] = Json.format[DriverLocation]

  override implicit val nearestDriverDbResultFormatter: OFormat[NearestDriverDbResult] =
    Json.format[NearestDriverDbResult]

  override implicit val errorResultJsonFormatter: OFormat[ErrorResult] = Json.format[ErrorResult]

  override val nearestDriverResponseFormatter: OFormat[NearestDriverResponse] = Json.format[NearestDriverResponse]

  override val driverLocationRequestFormatter: OFormat[DriverLocationRequestBody] =
    Json.format[DriverLocationRequestBody]

}

package com.xyz.transport.driverlocator.models.http.get

import com.xyz.transport.driverlocator.models.http.{Coordinates, ErrorResult}

case class NearestDriverResponse(distance: Double,
                                 id: Long,
                                 latitude: Double,
                                 longitude: Double) extends Coordinates {

  val errorResults: ErrorResult = {
    var errorResults = Seq[String]()
    if (latitude < -90.0 || latitude > 90.0) {
      errorResults = errorResults :+ s"Latitude should be between +/- 90.0"
    }
    if (longitude < -180.0 || longitude > 180.0) {
      errorResults = errorResults :+ s"Longitude should be between +/- 180.0"
    }
    if (distance < 0) {
      errorResults = errorResults :+ s"Distance should be positive, $distance given"
    }
    if (id < 0) {
      errorResults = errorResults :+ s"Driver Id should be positive, $id given"
    }
    ErrorResult(errorResults.to[scala.collection.immutable.Seq])
  }

  val isValid: Boolean = errorResults.errors.isEmpty
}

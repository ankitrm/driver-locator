package com.xyz.transport.driverlocator.models.http.get

import com.xyz.transport.driverlocator.models.http.{Coordinates, ErrorResult}

case class NearestDriverRequest(radius: Long,
                                limit: Int,
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
    if (limit <= 0) {
      errorResults = errorResults :+ s"Limit should be positive, $limit given"
    }
    if (radius < 0) {
      errorResults = errorResults :+ s"Radius should be positive, $radius given"
    }
    ErrorResult(errorResults.to[scala.collection.immutable.Seq])
  }

  val isValid: Boolean = errorResults.errors.isEmpty
}

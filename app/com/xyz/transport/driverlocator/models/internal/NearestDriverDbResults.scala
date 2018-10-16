package com.xyz.transport.driverlocator.models.internal

import scala.collection.immutable.Seq

import com.xyz.transport.driverlocator.models.http.get.NearestDriverResponse

case class NearestDriverDbResults(nearestDriverDbResults: Seq[NearestDriverDbResult]) {

  def convertToNearestDistanceResponses: Seq[NearestDriverResponse] =
    nearestDriverDbResults.map {
      dbResult =>
        NearestDriverResponse(dbResult.distance,
          dbResult.id,
          dbResult.geoloc.coordinates{1},
          dbResult.geoloc.coordinates.head)
    }
}

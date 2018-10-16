package com.xyz.transport.driverlocator.models.internal

import scala.collection.immutable.Seq

case class Geoloc(coordinates: Seq[Double], `type`: String = Geoloc.GeolocType) {

  require(coordinates.lengthCompare(2) == 0,
    s"Coordinates should to be of size 2, Given:[$coordinates]")

  require(-90.0 <= coordinates{1} && coordinates{1} <= 90.0,
    s"Latitude should be within [-90.0, 90.0], ${coordinates.head} given")

  require(-180.0 <= coordinates.head && coordinates.head <= 180.0,
    s"Longitude should be within [-180.0, 180.0], $coordinates{1} given")
}
object Geoloc {
  val GeolocType: String = "Point"
}

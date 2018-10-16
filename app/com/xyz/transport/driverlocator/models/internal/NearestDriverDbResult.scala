package com.xyz.transport.driverlocator.models.internal

case class NearestDriverDbResult(id: Long, geoloc: Geoloc, accuracy: Double, distance: Double)

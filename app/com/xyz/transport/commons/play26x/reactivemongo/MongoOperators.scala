package com.xyz.transport.commons.play26x.reactivemongo

import play.api.libs.json.{JsObject, Writes}
import play.api.libs.json.Json.obj

/**
  * Base trait for all Mongo Based Queries
  */
trait MongoOperators {

  protected def eq[T: Writes](key: String, value: T): JsObject = obj(key -> value)

  protected def set[T: Writes](value: T): JsObject =
    obj("$set" -> value)

  protected def near(`type`: String, longitude: Double, latitude: Double): JsObject =
    obj("type" -> `type`, "coordinates" -> Seq(longitude, latitude))
}

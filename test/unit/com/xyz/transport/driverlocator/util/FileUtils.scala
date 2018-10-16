package com.xyz.transport.driverlocator.util

import play.api.libs.json.{Json, JsValue, Reads}
import scala.reflect.io.File

/**
  * Defines common utility functions for reading file and converting it.
  */
object FileUtils {

  def fileToInstance[InstanceType](fileName: String)(implicit reads: Reads[InstanceType]): InstanceType =
    fileToJson(fileName).as[InstanceType]

  def fileToJson(fileName: String): JsValue =
    Json.parse(getFileAsString(fileName))

  private def getFileAsString(fileName: String): String =
    File(Thread.currentThread().getContextClassLoader.getResource(fileName).getPath).slurp
}

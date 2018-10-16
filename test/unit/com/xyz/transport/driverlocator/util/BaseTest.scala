package com.xyz.transport.driverlocator.util

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpec}

trait BaseTest extends WordSpec with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {
  private val longTime = 100 second
  protected implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  def awaitResult[T](f: Future[T]): T = {
    Await.result(f, longTime)
  }
}

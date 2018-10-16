package com.xyz.transport.driverlocator.util

import scala.concurrent.{ExecutionContext, Future}

import akka.util.Timeout
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}
import org.scalatest.mockito.MockitoSugar
import play.api.Configuration
import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Action, AnyContent, AnyContentAsJson, Result}
import play.api.test.{FakeRequest, StubControllerComponentsFactory}

/**
  * Base unit test for all controller classes.
  */
trait ControllerBaseTest extends WordSpec with Matchers with MockitoSugar
  with StubControllerComponentsFactory with BeforeAndAfter {

  protected implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  protected implicit val timeout: Timeout = play.api.test.Helpers.defaultAwaitTimeout
  protected val mockConfig: Configuration = mock[Configuration]

  protected def makeRequest[T](action: Action[AnyContent], body: T)(implicit writes: Writes[T]): Future[Result] =
    action.apply(requestWithJsonBody(body)(writes))

  private def requestWithJsonBody[T](body: T, request: String = "GET")
                                    (implicit writes: Writes[T]): FakeRequest[AnyContentAsJson] =
    FakeRequest().withMethod(request).withJsonBody(Json.toJson(body))

  protected def makePutRequestWithJsonBody[T](action: Action[AnyContent], body: T)
                                             (implicit writes: Writes[T]): Future[Result] =
    action.apply(requestWithJsonBody(body, "PUT")(writes))

  protected def makeRequest[T](action: Action[AnyContent]): Future[Result] =
    action.apply(FakeRequest())
}

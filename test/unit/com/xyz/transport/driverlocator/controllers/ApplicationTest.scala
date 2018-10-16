package com.xyz.transport.driverlocator.controllers

import scala.concurrent.Future

import com.xyz.transport.driverlocator.util.ControllerBaseTest

import org.mockito.Mockito.when
import play.api.Configuration
import play.api.http.Status.OK
import play.api.mvc.Result
import play.api.test.Helpers.{contentAsString, status}

/**
  * Unit tests of [[Application]]
  */
class ApplicationTest extends ControllerBaseTest {

  "Application health check" should {
    "return 200" when {
      "the application is up" in {
        val config = mock[Configuration]
        when(config.get[String](Application.ApplicationNameProp)).thenReturn("test-app")
        when(config.get[String](Application.ApplicationVersionProp)).thenReturn("1.0")
        when(config.get[String](Application.GitCommitProp)).thenReturn("abcd")

        val controller = new Application(config, stubControllerComponents())
        val result: Future[Result] = makeRequest(controller.health)

        status(result) shouldBe OK
        contentAsString(result) should include("test-app")
        contentAsString(result) should include("1.0")
        contentAsString(result) should include("abcd")
      }
    }
  }
}

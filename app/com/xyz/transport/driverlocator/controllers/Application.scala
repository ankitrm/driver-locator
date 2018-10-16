package com.xyz.transport.driverlocator.controllers

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

import play.api.Configuration
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

@Singleton
class Application @Inject()(config: Configuration, controllerComponents: ControllerComponents)
                           (implicit ctx: ExecutionContext) extends AbstractController(controllerComponents) {

  import Application.{ApplicationNameProp, ApplicationVersionProp, GitCommitProp}

  private val appName = config.get[String](ApplicationNameProp)
  private val appVersion = config.get[String](ApplicationVersionProp)
  private val gitCommit = config.get[String](GitCommitProp)

  def health: Action[AnyContent] = Action.async { _ =>
    Future(
      Ok(
        s"$appName is healthy. Version: $appVersion Commit: $gitCommit"
      )
    )
  }
}

object Application {
  val ApplicationNameProp: String = "project.name"
  val ApplicationVersionProp: String = "project.version"
  val GitCommitProp: String = "app.git.commit"
}

package com.xyz.transport.driverlocator.controllers

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

import com.xyz.transport.driverlocator.marshalling.DriverLocatorJsonFormatters
import com.xyz.transport.driverlocator.models.http.ErrorResult
import com.xyz.transport.driverlocator.models.http.get.{NearestDriverRequest, NearestDriverResponse}
import com.xyz.transport.driverlocator.models.http.put.DriverLocationRequestBody
import com.xyz.transport.driverlocator.models.internal.DriverLocation
import com.xyz.transport.driverlocator.services.DriverLocationService
import com.xyz.transport.driverlocator.utils.LoggerSupport

import play.api.libs.json.{JsError, Json, JsSuccess, OFormat}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

@Singleton
class DriverLocationController @Inject()(controllerComponents: ControllerComponents,
                                         driverLocatorFormatter: DriverLocatorJsonFormatters[OFormat],
                                         driverLocationService: DriverLocationService)
                                        (implicit ctx: ExecutionContext)
  extends AbstractController(controllerComponents) with LoggerSupport {

  private implicit val driverLocationRequestFormatter: OFormat[DriverLocationRequestBody] =
    driverLocatorFormatter.driverLocationRequestFormatter
  private implicit val errorResultFormatter: OFormat[ErrorResult] =
    driverLocatorFormatter.errorResultJsonFormatter
  private implicit val nearestDriverResponseFormatter: OFormat[NearestDriverResponse] =
    driverLocatorFormatter.nearestDriverResponseFormatter


  def saveDriverLocation(id: Long): Action[AnyContent] = Action.async { request =>
    request
      .body
      .asJson
      .map(json => json.validate[DriverLocationRequestBody]) match {
      case Some(JsSuccess(driverLocationRequestBody, _)) =>
        processSaveDriverLocationRequest(id, driverLocationRequestBody)
      case Some(JsError(err)) =>
        logger.error("Invalid json request(something changed in contract?): ", err)
        Future(BadRequest(s"Cannot parse the request to Json. Errors: $err"))
      case _ =>
        logger.error("Verify the request is json")
        Future(BadRequest(s"Request is not Json"))
    }
  }

  private def processSaveDriverLocationRequest(id: Long,
                                               driverLocationRequestBody: DriverLocationRequestBody) = {
    logger.info("Driver Location request to save :{} ", driverLocationRequestBody)
    if (driverLocationRequestBody.isValid) {
      driverLocationService.saveOrUpdate(DriverLocation(id, driverLocationRequestBody)).map { _ =>
        logger.info("Saved driver location request")
        Ok
      }.recoverWith({
        case illegalArgumentException: IllegalArgumentException =>
          logger.error("Invalid input: ", illegalArgumentException)
          Future(NotFound)
      })
    } else {
      val driverRequestJsonBody = Json.toJson(driverLocationRequestBody.errorResults)
      logger.error("Error saving driver location request: {}", driverRequestJsonBody)
      Future(UnprocessableEntity(driverRequestJsonBody))
    }
  }

  def getNearestDrivers(latitude: Double,
                        longitude: Double,
                        limit: Int,
                        radiusMeters: Long): Action[AnyContent] = Action.async { _ =>

    val nearestDriverRequest = NearestDriverRequest(radiusMeters, limit, latitude, longitude)
    logger.info("Request received to get nearest drivers for parameters: {}", nearestDriverRequest)
    if (nearestDriverRequest.isValid) {
      driverLocationService.getNearest(nearestDriverRequest).map({
        response =>
          logger.info(s"Fetched ${response.size} driver's locations")
          Ok(Json.toJson(response))
      })
    } else {
      val errorResults = nearestDriverRequest.errorResults
      logger.error(s"Invalid input from the user. Error list: $errorResults")
      Future(UnprocessableEntity(Json.toJson(errorResults)))
    }
  }
}

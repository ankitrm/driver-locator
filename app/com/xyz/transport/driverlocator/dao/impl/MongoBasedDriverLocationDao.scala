package com.xyz.transport.driverlocator.dao.impl

import java.io.IOException
import javax.inject.Inject

import scala.collection.immutable.Seq
import scala.concurrent.{ExecutionContext, Future}

import com.xyz.transport.commons.play26x.reactivemongo.BaseMongoDao
import com.xyz.transport.driverlocator.dao.DriverLocationDao
import com.xyz.transport.driverlocator.marshalling.DriverLocatorJsonFormatters
import com.xyz.transport.driverlocator.models.http.get.NearestDriverRequest
import com.xyz.transport.driverlocator.models.internal.{DriverLocation, NearestDriverDbResult, NearestDriverDbResults}
import com.xyz.transport.driverlocator.models.internal.Geoloc.GeolocType
import com.xyz.transport.driverlocator.utils.LoggerSupport

import play.api.Configuration
import play.api.libs.json.OFormat
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.Cursor
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.play.json._


class MongoBasedDriverLocationDao @Inject()(protected val reactiveMongoApi: ReactiveMongoApi,
                                            driverLocatorFormatter: DriverLocatorJsonFormatters[OFormat],
                                            config: Configuration)
                                           (implicit val executionContext: ExecutionContext)
  extends LoggerSupport with DriverLocationDao with BaseMongoDao {

  import MongoBasedDriverLocationDao._

  private implicit val driverLocationFormatter: OFormat[DriverLocation] =
    driverLocatorFormatter.driverLocationFormatter
  private implicit val nearestDriverDbResultFormatter: OFormat[NearestDriverDbResult] =
    driverLocatorFormatter.nearestDriverDbResultFormatter

  private val driverIdLowerLimit = config.get[Long](DriverIdLowerLimitKey)
  private val driverIdUpperLimit = config.get[Long](DriverIdUpperLimitKey)

  override protected def collectionName: String = CollectionName

  override protected def indexes: Seq[Index] = Seq(
    Index(Seq(GeolocFieldName -> IndexType.Geo2DSpherical), background = true)
  )

  override def insertOrUpdate(driverLocation: DriverLocation): Future[DriverLocation] = {
    val driverLocationId = driverLocation.id
    require(driverLocationId > driverIdLowerLimit && driverLocationId <= driverIdUpperLimit,
      s"Id should be greater than $driverIdLowerLimit and lower than $driverIdUpperLimit], Given: [$driverLocationId]")

    collection.map({
      _.findAndUpdate(eq(IdFieldName, driverLocationId), set(driverLocation), fetchNewObject = true, upsert = true)
    }).recoverWith({
      case throwable: Throwable =>
        logger.error("Unexpected error while updating/inserting driver location: [{}]. Exception: ",
          driverLocation, throwable)
        Future.failed(throwable)
    }).flatMap(res => {
      logger.debug("Verifying request to insert/update driver location with request: [{}]. Result: [{}]]"
        , driverLocation, res)
      res.map(_.result[DriverLocation]
        .getOrElse(throw new IOException(s"Failed to create/update driver location with request: $driverLocation")))
    })
  }

  override def getNearestResults(nearestDriverRequest: NearestDriverRequest): Future[NearestDriverDbResults] = {
    collection.flatMap({ driverLocationCollection =>
      import driverLocationCollection.BatchCommands.AggregationFramework.GeoNear
      driverLocationCollection.aggregatorContext[NearestDriverDbResult](
        GeoNear(
          near(GeolocType, nearestDriverRequest.longitude, nearestDriverRequest.latitude),
          spherical = true,
          distanceField = Some(DistanceFieldName),
          limit = nearestDriverRequest.limit,
          maxDistance = Some(nearestDriverRequest.radius)
        )).prepared.cursor
        .collect[Seq](-1, Cursor.FailOnError[Seq[NearestDriverDbResult]]())
    }).map(nearestDriverLocations => NearestDriverDbResults(nearestDriverLocations))
  }
}

object MongoBasedDriverLocationDao {
  private[impl] val CollectionName = "driver-locations"
  private[impl] val IdFieldName = "id"
  private[impl] val DriverIdLowerLimitKey = "validation.driverid.lowerlimit"
  private[impl] val DriverIdUpperLimitKey = "validation.driverid.upperlimit"
  private[impl] val GeolocFieldName = "geoloc"
  private[impl] val DistanceFieldName = "distance"
}

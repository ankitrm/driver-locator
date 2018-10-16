package com.xyz.transport.driverlocator.util

import scala.concurrent.Future

import com.xyz.transport.driverlocator.util.mongo.{MongodProps, MongoEmbedDatabase}

import de.flapdoodle.embed.mongo.distribution.Version
import play.api.Configuration
import play.api.inject.DefaultApplicationLifecycle
import play.api.libs.json.Json
import play.modules.reactivemongo.{DefaultReactiveMongoApi, ReactiveMongoApi}
import reactivemongo.api.MongoConnection.ParsedURI
import reactivemongo.api.MongoConnectionOptions
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.ImplicitBSONHandlers.JsObjectDocumentWriter
import reactivemongo.play.json.collection.JSONCollection

/**
  * Base test class which requires in memory MongoDB.
  */
trait BaseMongoTest extends BaseTest with MongoEmbedDatabase {

  private val port = 12345
  private lazy val DbName = "test"
  private lazy val mongod: MongodProps = mongoStart(port, version = Version.V3_6_5)
  protected val reactiveMongoApi: ReactiveMongoApi = createReactiveMongoApi
  private val SystemCollectionPrefix = "system"

  override def afterEach(): Unit = {
    super.afterEach()

    val result: Future[List[WriteResult]] = reactiveMongoApi.database
      .flatMap(database => database.collectionNames)
      .map(collectionNames => collectionNames.filterNot(_.startsWith(SystemCollectionPrefix)).map(removeAllData))
      .flatMap(Future.sequence(_))

    awaitResult(result)
  }

  private def removeAllData(collectionName: String) =
    reactiveMongoApi.database.flatMap(database => {
      database.collection[JSONCollection](collectionName).delete().one(Json
        .obj())
    })

  def count(collectionName: String): Int =
    awaitResult(reactiveMongoApi.database.flatMap(database => {
      database.collection[JSONCollection](collectionName).count()
    }))

  override protected def afterAll: Unit = {
    super.afterAll()

    reactiveMongoApi.driver.close()
    mongoStop(mongod)
  }

  private def createReactiveMongoApi = {
    assume(mongod.mongodProcess.isProcessRunning)

    new DefaultReactiveMongoApi(
      name = DbName,
      parsedUri = ParsedURI(List("localhost" -> port), MongoConnectionOptions(), List(), None, None),
      dbName = DbName,
      strictMode = true,
      configuration = Configuration.empty,
      applicationLifecycle = new DefaultApplicationLifecycle {
        override def addStopHook(hook: () => Future[_]): Unit = ()
      })
  }
}

package com.xyz.transport.commons.play26x.reactivemongo

import scala.collection.immutable.Seq
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.DurationInt

import com.typesafe.scalalogging.LazyLogging
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.indexes.Index
import reactivemongo.core.errors.DatabaseException
import reactivemongo.play.json.collection.JSONCollection

/**
  * Base trait for all Mongo Based DAO implementations.
  */
trait BaseMongoDao extends LazyLogging with Indexes with MongoOperators {

  import BaseMongoDao.DuplicateKeyErrorCode

  private val timeoutDurationForIndexCreation = 60 seconds

  protected implicit val executionContext: ExecutionContext

  Await.result(ensureIndexes, timeoutDurationForIndexCreation)

  protected def reactiveMongoApi: ReactiveMongoApi

  protected def collectionName: String

  private def ensureIndexes: Future[Unit] = {
    logger.info(s"Creating Indexes for $collectionName")
    Future(indexes.foreach(ensureIndex))
  }

  private def ensureIndex(index: Index): Future[Unit] = {
    val errorMessage = "Failed to ensure index"
    collection.flatMap(_.indexesManager.create(index).map(updateResult)).recoverWith({
      case databaseException: DatabaseException
        if databaseException.code.contains(DuplicateKeyErrorCode) =>
        val duplicateKeyException = DuplicateKeyException(databaseException.getMessage())
        logger.error(errorMessage, duplicateKeyException)
        Future.failed(duplicateKeyException)
      case throwable: Throwable =>
        logger.error(errorMessage, errorMessage)
        Future.failed(throwable)
    })
  }

  protected def collection: Future[JSONCollection] =
    reactiveMongoApi.database.map(_.collection[JSONCollection](collectionName))

  protected def updateResult(u: WriteResult): Unit = u match {
    case passedResult if passedResult.ok => logger.debug(s"${passedResult.n} records updated")
    case failedResult => WriteFailed(getErrorMessage(failedResult))
  }

  private def getErrorMessage(u: WriteResult) = {
    u.writeErrors.map(error => s"Code: ${error.code}, Desc: ${error.errmsg}").mkString("Error: [", ",", "]")
  }
}

abstract class MongoDBError(errorMessage: String) extends RuntimeException(errorMessage)

case class WriteFailed(errorMessage: String) extends MongoDBError(errorMessage)

case class DuplicateKeyException(errorMessage: String) extends MongoDBError(errorMessage)

trait Indexes {
  protected def indexes: Seq[Index] = Seq.empty
}

object BaseMongoDao {
  private val DuplicateKeyErrorCode = 11000
}

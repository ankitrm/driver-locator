package com.xyz.transport.driverlocator.modules

import com.google.inject.{AbstractModule, TypeLiteral}
import com.xyz.transport.driverlocator.dao.DriverLocationDao
import com.xyz.transport.driverlocator.dao.impl.MongoBasedDriverLocationDao
import com.xyz.transport.driverlocator.marshalling.DriverLocatorJsonFormatters
import com.xyz.transport.driverlocator.marshalling.impl.PlayBasedDriverLocatorJsonFormatters
import com.xyz.transport.driverlocator.services.DriverLocationService
import com.xyz.transport.driverlocator.services.impl.DriverLocationServiceImpl
import play.api.libs.json.OFormat

/**
  * Main module which wires the classes.
  */
class ServiceModule extends AbstractModule {

  override def configure(): Unit = {
    bindServices()
    bindMarshalling()
    bindDao()
  }

  private def bindServices(): Unit = {
    bind(classOf[DriverLocationService]).to(classOf[DriverLocationServiceImpl])
  }

  private def bindMarshalling(): Unit = {
    bind(new TypeLiteral[DriverLocatorJsonFormatters[OFormat]] {}).toInstance(PlayBasedDriverLocatorJsonFormatters)
  }

  private def bindDao(): Unit = {
    bind(classOf[DriverLocationDao]).to(classOf[MongoBasedDriverLocationDao])
  }
}

package com.xyz.transport.driverlocator.util.mongo

import de.flapdoodle.embed.mongo.{Command, MongodExecutable, MongodProcess, MongodStarter}
import de.flapdoodle.embed.mongo.config.{MongoCmdOptionsBuilder, MongodConfigBuilder, Net, RuntimeConfigBuilder}
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.config.IRuntimeConfig
import de.flapdoodle.embed.process.config.io.ProcessOutput
import de.flapdoodle.embed.process.runtime.Network


/**
  * Extend this trait (with Journal enabled) to perform embedded Mongo operations in tests.
  */
trait MongoEmbedDatabase {

  private val port = 12345: Int

  private val runtimeConfig = new RuntimeConfigBuilder()
    .defaults(Command.MongoD)
    .processOutput(ProcessOutput.getDefaultInstanceSilent)
    .build()

  protected def mongoStart(port: Int = port,
                           version: Version = Version.V3_6_5,
                           runtimeConfig: IRuntimeConfig = runtimeConfig): MongodProps = {
    val mongodExe: MongodExecutable = mongodExec(port, version, runtimeConfig)
    MongodProps(mongodExe.start(), mongodExe)
  }

  protected def mongoStop(mongodProps: MongodProps): Unit = {
    Option(mongodProps).foreach(_.mongodProcess.stop())
    Option(mongodProps).foreach(_.mongodExe.stop())
  }

  private def runtime(config: IRuntimeConfig): MongodStarter = MongodStarter.getInstance(config)

  private def mongodExec(port: Int, version: Version, runtimeConfig: IRuntimeConfig): MongodExecutable =
    runtime(runtimeConfig).prepare(
      new MongodConfigBuilder()
        .cmdOptions(new MongoCmdOptionsBuilder().useNoJournal(false).build())
        .version(version)
        .net(new Net(port, Network.localhostIsIPv6()))
        .build()
    )
}

sealed case class MongodProps(mongodProcess: MongodProcess, mongodExe: MongodExecutable)


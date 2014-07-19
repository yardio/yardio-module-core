package io.yard.module.core

import scala.collection.mutable.Buffer
import scala.concurrent.duration._

import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout

import play.api.libs.concurrent.Akka
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.ws.WS
import play.api.Play.current

import io.yard.common.utils._
import io.yard.common.models._
import io.yard.connector.api.Connector

object Api extends Log {
  lazy val logger = initLogger("yardio.module.core.Api")
  implicit val timeout = Timeout(5 seconds)

  // Connector
  private var _connector: Option[Connector] = None
  lazy val connector = _connector.getOrElse { throw new Exception("You MUST register one connector")}

  def registerConnector(connect: Connector) = _connector match {
    case None => {
      _connector = Some(connect)
    }
    case _ => throw new Exception(" You can register only one connector")
  }

  // Providers
  private var providers: Buffer[Provider] = Buffer.empty

  def registerProvider(provider: Provider) = {
    providers += provider
    provider
  }

  def getProviders: Seq[Provider] = providers.toSeq

  def getProvider(name: String): Option[Provider] = providers.find { _.name == name }


  // Modules
  private var modules: Buffer[Module] = Buffer.empty

  def registerModule(module: Module) = {
    modules += module
    module
  }

  def getModules: Seq[Module] = modules.toSeq

  def getModule(name: String): Option[Module] = modules.find { _.name == name }


  // Actors
  def tellAll(message: Any) = modules foreach { _.actor map { _ ! message } }
  def !!(message: Any) = tellAll(message)

  def askAll(message: Any) = modules foreach { _.actor map { _ ? message } }
  def ??(message: Any) = askAll(message)


  // Messaging
  def send(message: Message) = {
    val organization: Organization = organizations.default
    println(s"SEND [${message}] TO [${organization}]")
    /*getProviders foreach (_.send(message, organization))*/
  }

  def send(message: Message, organization: Organization) = {
    println(s"SEND [${message}] TO [${organization}]")
    /*getProviders foreach (_.send(message, organization))*/
  }


  // Organizations
  private lazy val orgas = Seq(Organization("Movio"))

  object organizations {
    def all: Seq[Organization] = connector.organizations.all
    def default: Organization = connector.organizations.default
    def byNameOption(name: String): Option[Organization] = connector.organizations.byNameOption(name)
    def byName(name: String): Organization = connector.organizations.byName(name)
    def from(value: String): Organization = connector.organizations.from(value)
  }
}

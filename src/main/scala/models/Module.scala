package io.yard.core
package models

import akka.actor.{ ActorRef, Props }

import io.yard.core.controllers.ModuleController

case class Module(
  name: String,
  description: String,
  controller: Option[ModuleController] = None,
  actorProps: Option[Props] = None,
  actor: Option[ActorRef] = None // Don't try to set this one
  )

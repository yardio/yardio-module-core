package io.yard.core
package controllers

import play.api._
import play.api.mvc._

import io.yard.core.Api
import io.yard.core.controllers._
import  io.yard.core.utils._

object yardioController extends ModuleController with Answer with Config {

  lazy val logger = initLogger("yardio.controllers.yardio")

  def hasRoute(rh: RequestHeader) = true

  def applyRoute[RH <: RequestHeader, H >: Handler](rh: RH, default: RH ⇒ H) = {
    val subpath = rh.path.drop(path.length)
    val paths = subpath.split("/").filter { _ != "" }.toSeq.lift

    (rh.method, paths(0), paths(1)) match {
      // Root route
      case ("GET", None, None)  ⇒ Action { Ok(io.yard.html.index(Api.getModules, core.teams, path + "/modules/")) }
      // Registered modules routes
      case (_, Some("modules"), Some(moduleName)) ⇒ {
        Api.getModule(moduleName) match {
          case Some(module) ⇒ module.controller.map { c =>
            c.setPrefix(path + "/modules/" + moduleName)
            c.applyRoute(rh, default)
          } getOrElse default(rh)
          case _            ⇒ default(rh)
        }
      }
      // Whatever...
      case _ ⇒ default(rh)
    }
  }
}

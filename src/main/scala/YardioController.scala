package io.yard.module.core
package controllers

import play.api._
import play.api.mvc._

import io.yard.common.utils._
import io.yard.common.models.ModuleController
import io.yard.module.core.Api

object YardioController extends ModuleController with Answer with Log {

  lazy val logger = initLogger("yardio.controllers.yardio")

  def hasRoute(rh: RequestHeader) = true

  def applyRoute[RH <: RequestHeader, H >: Handler](rh: RH, default: RH ⇒ H) = {
    val subpath = rh.path.drop(path.length)
    val paths = subpath.split("/").filter { _ != "" }.toSeq.lift

    (rh.method, paths(0), paths(1)) match {
      // Root route
      case ("GET", None, None)  ⇒ Action { Ok(io.yard.html.index(Api.getModules, Api.organizations.all, path + "/modules/")) }
      // Registered module routes
      case (_, Some("modules"), Some(moduleName)) ⇒ {
        Api.getModule(moduleName) match {
          case Some(module) ⇒ module.controller.map { c =>
            c.setPrefix(path + "/modules/" + moduleName)
            c.applyRoute(rh, default)
          } getOrElse default(rh)
          case _            ⇒ default(rh)
        }
      }
      // Registered provider routes
      case (_, Some("providers"), Some(providerName)) ⇒ {
        Api.getProvider(providerName) match {
          case Some(provider) ⇒ provider.controller.map { c =>
            c.setPrefix(path + "/providers/" + providerName)
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

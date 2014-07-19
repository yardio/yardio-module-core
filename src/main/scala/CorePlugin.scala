package io.yard.module.core

import io.yard.common.models.Module

class CorePlugin(application: play.api.Application) extends play.api.Plugin {
  override def onStart = {
    Api.registerModule(Module(
      "core",
      "Core module",
      Some(io.yard.module.core.controllers.CoreController),
      None
    ))
  }
}

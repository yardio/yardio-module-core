package io.yard.core

class CorePlugin(application: play.api.Application) extends play.api.Plugin {
  override def onStart = {
    Api.registerModule(
      "core",
      "Core module",
      Some(io.yard.core.controllers.CoreController),
      None
    )
  }
}

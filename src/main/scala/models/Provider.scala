package io.yard.core
package models

sealed trait Provider {
  def name: String
}

case class SlackProvider(name: String = "slack") extends Provider
case class FlowDockProvider(name: String = "flowdock") extends Provider
case class HipChatProvider(name: String = "hipchat") extends Provider

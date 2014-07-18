package io.yard.core
package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class SlackUser(id: String, name: Option[String]) extends User

object SlackUser {
  implicit val slackUserFormat = Json.format[SlackUser]
}

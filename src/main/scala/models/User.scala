package io.yard.core
package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

trait User {
  def id: String
  def name: Option[String]
}

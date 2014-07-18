package io.yard.core
package models

case class SlackTeam(id: String, name: String, modules: Option[Seq[String]], tokens: Map[String, Seq[String]]) extends Organization

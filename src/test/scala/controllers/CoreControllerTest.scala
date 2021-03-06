package io.yard.module.core.test
package controllers

import scala.concurrent.Future

import org.scalatest._
import org.scalatestplus.play._

import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import io.yard.module.core.test.yardioSpec
import io.yard.module.core.controllers.CoreController
import io.yard.common.models._

class CoreControllerTest extends yardioSpec {

  def applyRoute[A](request: FakeRequest[A]): Future[Result] =
    CoreController.applyRoute(request, (d: FakeRequest[A]) => Action { Results.NotFound }) match {
      case a: Action[A] => a(request)
      case _ => throw new Exception("Nope... just nope")
    }

  "CoreController" must {
    "correctly handle valid outgoing webhooks" in {
      val result: Result = await(applyRoute(Requests.outgoing.valid))
      result.header.status mustBe 200
    }

    "fail to handle invalid outgoing webhooks" in {
      val result: Result = await(applyRoute(Requests.outgoing.invalid))
      result.header.status mustBe 400
    }

    "fail to handle outgoing webhooks on the wrong url" in {
      val result: Result = await(applyRoute(Requests.outgoing.wrongUrl))
      result.header.status mustBe 404
    }
  }
}

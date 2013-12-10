package controllers

import play.api.test.{FakeRequest, WithApplication}
import scala.concurrent.Future
import play.api.mvc.{AnyContentAsFormUrlEncoded, SimpleResult}
import play.api.test.Helpers._
import models.TestDetails

class PostSpec //extends AbstractControllerRouteSpec
{

//  "Post " should {
//    "post return OK " in new WithApplication {
//      val data = Map[String, Seq[String]](
//        TestDetails.url -> Seq("https://github.com/jaceklaskowski/scalania.git"),
//        TestDetails.subProject -> Seq("exercises"),
//        TestDetails.packageName -> Seq("pl.japila.scalania.s99"),
//        TestDetails.objectName -> Seq("S99_P21"),
//        TestDetails.signature -> Seq("Seq[(Any, Int, Seq[Any]) => Seq[Any]]"),
//        TestDetails.testName -> Seq("P21Spec"),
//        TestDetails.numbers -> Seq("7680647,7680700")
//      )
//      val body = AnyContentAsFormUrlEncoded(data)
//      val request = FakeRequest(POST, routes.Application.checkSolution.url).withBody(body)
//      val simpleResult: Future[SimpleResult] = callReverseRoutingPost(request)
//
//      contentType(simpleResult) must beSome.which(_ == "text/html")
//    }
//  }

}
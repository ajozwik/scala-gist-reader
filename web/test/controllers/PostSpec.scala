package controllers

import play.api.test.{FakeRequest, WithApplication}
import scala.concurrent.Future
import play.api.mvc.{AnyContentAsFormUrlEncoded, SimpleResult}
import play.api.test.Helpers._

class PostSpec extends AbstractControllerRouteSpec {

  "Post " should {
    "post return OK " in new WithApplication {
      val data = Map[String, Seq[String]](
        "packageName" -> Seq("pl.japila.scalania.s99"),
        "objectName" -> Seq("S99_P21"),
        "signature" -> Seq("Seq[(Any, Int, Seq[Any]) => Seq[Any]]"),
        "testName" -> Seq("P21Spec"),
        "numbers" -> Seq("7680647,7680700")
      )
      val body = AnyContentAsFormUrlEncoded(data)
      val request = FakeRequest(POST, routes.Application.checkSolution.url).withBody(body)
      val simpleResult: Future[SimpleResult] = callReverseRoutingPost(request)

      contentType(simpleResult) must beSome.which(_ == "text/html")
    }
  }

}
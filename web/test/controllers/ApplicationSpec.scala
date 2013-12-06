package controllers


import play.api.test._
import play.api.test.Helpers._
import scala.concurrent.Future
import play.api.mvc.SimpleResult

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends AbstractControllerRouteSpec {

  "Application" should {

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "render the index page" in new WithApplication {
      val simpleResult: Future[SimpleResult] = callReverseRoutingGet(routes.Application.index)

      contentType(simpleResult) must beSome.which(_ == "text/html")
    }

    "empty post " in new WithApplication {
      val request = FakeRequest(POST,routes.Application.checkSolution.url)
      val simpleResult: Future[SimpleResult] = callReverseRoutingPost(request,BAD_REQUEST)

      contentType(simpleResult) must beSome.which(_ == "text/html")
    }

  }
}

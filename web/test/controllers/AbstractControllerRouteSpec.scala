package controllers

import org.specs2.mutable
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import scala.concurrent.Future
import org.slf4j.LoggerFactory
import play.api.http.Writeable
import play.api.mvc.Call
import play.api.mvc.SimpleResult
import akka.util.Timeout
import java.util.concurrent.TimeUnit

abstract class AbstractControllerRouteSpec extends mutable.SpecificationWithJUnit {
  private val logger = LoggerFactory.getLogger(getClass)


  protected def callReverseRoutingPost[T](request: Request[T], responseStatus: Int = OK)(implicit w: Writeable[T])= callReverseRouting(request, responseStatus)(w)


  protected def callReverseRoutingGet(call: Call, responseStatus: Int = OK) = callReverseRouting[AnyContentAsEmpty.type](FakeRequest(GET, call.url), responseStatus)


  private def callReverseRouting[T](request: Request[T], responseStatus: Int)(implicit w: Writeable[T]): Future[SimpleResult] = {
    logger.debug("Body: {}", request.body)
    val optionResult = route(request)(w)
    (optionResult should not).beNone
    val futureResult = optionResult.get
    logger.debug("{}", status(futureResult)(Timeout(1, TimeUnit.MINUTES)).toString)
    status(futureResult) must equalTo(responseStatus)
    futureResult
  }
}

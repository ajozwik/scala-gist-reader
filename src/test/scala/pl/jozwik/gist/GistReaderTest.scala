package pl.jozwik.gist

import org.specs2.mutable

object GistReaderTest {
  val port = 63823
  val gistId = 7680909
}

class GistReaderTest extends mutable.Specification {

  import GistReaderTest._


  "Gist reader " should {
    "extract file from gist " in new context {
      val HTTP = "http://localhost:" + port + "/"
      val res = GistReader.readFileFromGist(gistId,HTTP)
      val b = res match {
        case Right(r) => true
        case Left(l) => false
      }
      b === true
    }
  }


  trait context extends mutable.BeforeAfter {
    var server: HttpServer = _

    def before: Any = {
      server = HttpServer(port)
    }

    def after: Any = {
      server.listenChannel.close()
    }

  }

}

package pl.jozwik.gist

import org.apache.http.impl.client.HttpClients
import org.apache.http.client.methods.HttpGet
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import scala.util.{Success, Try,Failure}

object GistReader {

  private lazy val logger = LoggerFactory.getLogger(GistReader.getClass)
  val DEFAULT_URL = "https://api.github.com/gists/"
  val httpclient = HttpClients.createDefault()

  private def httpRequest(url:String) = {
    val httpget = new HttpGet(url)
    val response = httpclient.execute(httpget)
    try {
      IOUtils.toString(response.getEntity.getContent)
    } finally {
      response.close()
    }
  }



  def readFileFromGist(url:String,number:Int):Either[String,Any] = {
    val jsonResponse = Try(httpRequest(url+number))
    jsonResponse match {
      case Success(body) => extractJson(body)
      case Failure(e) => Left(e.getMessage)
    }

  }


  private def extractJson(body: String) = {
    val l = List("files", null, "content")
    JsonExtractor.parseBody(body, l) match {
      case None => Left(s"no such key: $l")
      case Some(any) => Right(any)
    }
  }

  def main(args:Array[String]) {
    val scala22 = readFileFromGist(DEFAULT_URL,7680909)
    val scalaIsSorted = readFileFromGist(DEFAULT_URL,7681231)
    logger.debug(s"$scala22")
    logger.debug(s"$scalaIsSorted")
  }
}

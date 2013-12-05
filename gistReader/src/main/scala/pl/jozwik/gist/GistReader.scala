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


  def readFilesFromGist(numbers:Seq[Int],url:String = DEFAULT_URL):Seq[Either[String,(Any,Int)]] = {
    numbers.map(n => readFileFromGist(n,url))
  }

  def readFileFromGist(number:Int,url:String = DEFAULT_URL):Either[String,(Any,Int)] = {
    val jsonResponse = Try(httpRequest(url+number))
    jsonResponse match {
      case Success(body) => extractJson(body) match {
        case Left(m) => Left(m)
        case Right(any) => Right((any,number))
      }
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
    val files = readFilesFromGist(Seq(7680909,7681231))

    val begin =
      s"""
        |class ForTest{
        |
     """.stripMargin

    val end =
      s"""
        |}
     """.stripMargin
    val builder = new StringBuilder(begin)
    files.foreach(either => either match{
      case Right(body) => builder.append(body).append('\n').append('\n')
      case Left(e) =>
    })
    builder.append(end)

    logger.debug(s"$builder")
  }
}

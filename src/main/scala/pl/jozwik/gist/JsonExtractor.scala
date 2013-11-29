package pl.jozwik.gist

import scala.util.parsing.json.JSON
import org.slf4j.LoggerFactory
import scala.annotation.tailrec

object JsonExtractor {

  def parseBody(body: String, tags: List[String]): Option[Any] = {
    val js: Option[Any] = JSON.parseFull(body)
    js match {
      case Some(x) => extract(x, tags)
      case None => None

    }

  }

  @tailrec
  private def extract(x: Any, tags: List[String]): Option[Any] = (x, tags) match {
    case (_, Nil) => Some(x)
    case (map: Map[String, Any], h :: t) => {
      val v = if (h == null) {
        map.values.headOption match {
          case Some(x) => x
          case none => none
        }
      } else {
        map.getOrElse(h, None)
      }
      extract(v, t)
    }
    case (x, _) => None
  }


}

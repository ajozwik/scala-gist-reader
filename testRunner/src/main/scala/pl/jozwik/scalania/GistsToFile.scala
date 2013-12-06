package pl.jozwik.scalania

import pl.jozwik.gist.GistReader._

object GistsToFile {

  private val endMethodName = Seq('(', '[', '=', ':')
  private val defName: String = "def "
  private val closeSolutions = s"""
                  |)
                  |
                  """.stripMargin

  private val endFile =
    s"""
        |}
     """.stripMargin

  private def beginFile(packageName: String, objectName: String, signatureOfMethod: String) =
    s"""
        |package $packageName
        |
        |import annotation._
        |
        |object $objectName{
        |
        |  val solutions =  $signatureOfMethod (
     """.stripMargin

  def gistsToFile(packageName: String, objectName: String, signatureOfMethod: String, gists: Seq[Int], url: String = DEFAULT_URL): String = {
    val contentOfFiles = readFilesFromGist(gists, url)

    val content = new StringBuilder()
    val solutions = new StringBuilder()
    contentOfFiles.foreach(_ match {
      case Right((body, number)) => {
        val b = body.toString
        val methodName:Option[String] = extractMethodName(b)
        methodName match {
          case None =>
          case Some(name) =>
            val newMethodName = name + "_" + number
            val newBody = b.replaceFirst(name, newMethodName)
            if (!solutions.isEmpty) {
              solutions.append(",").append('\n')
            }
            solutions.append(newMethodName)
            content.append(newBody).append('\n').append('\n')
        }

      }
      case Left(e) =>
    })

    toScalaObject(packageName, objectName, signatureOfMethod, solutions, content)

  }


  private def toScalaObject(packageName: String, objectName: String, signatureOfMethod: String, solutions: StringBuilder, content: StringBuilder): String = {
    beginFile(packageName, objectName, signatureOfMethod) + solutions + closeSolutions + content + endFile
  }

  def extractMethodName(body: String): Option[String] = {

    val defIndex = body.indexOf(defName)
    if (defIndex == -1) {
      None
    } else {
      endMethodName.map(e => body.indexOf(e)).filter(p => p != -1) match {
        case Nil => None
        case l => {
          val min = l.min
          if (defIndex + defName.length < l.min) {
            val name = body.substring(defIndex + defName.length, min).trim
            Some(name)
          }
          else {
            None
          }
        }
      }
    }
  }
}

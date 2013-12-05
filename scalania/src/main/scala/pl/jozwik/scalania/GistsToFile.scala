package pl.jozwik.scalania

import pl.jozwik.gist.GistReader._

object GistsToFile {

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

  def gistsToFile(packageName: String, objectName: String, signatureOfMethod: String, gists: Int*): String = {
    val contentOfFiles = readFilesFromGist(gists.toSeq)

    val content = new StringBuilder()
    val solutions = new StringBuilder()
    contentOfFiles.foreach(_ match {
      case Right((body, number)) => {
        if (!solutions.isEmpty) {
          solutions.append(",").append('\n')
        }
        val b = body.toString
        val methodName = extractMethodName(b)
        val newMethodName = methodName + "_" + number
        val newBody = b.replaceFirst(methodName, newMethodName)
        solutions.append(newMethodName)
        content.append(newBody).append('\n').append('\n')
      }
      case Left(e) =>
    })

    toScalaObject(packageName, objectName, signatureOfMethod, solutions, content)

  }


  private def toScalaObject(packageName: String, objectName: String, signatureOfMethod: String, solutions: StringBuilder, content: StringBuilder): String = {
    beginFile(packageName, objectName, signatureOfMethod) + solutions + closeSolutions + content + endFile
  }

  def extractMethodName(body: String): String = {
    val defIndex = body.indexOf("def")
    val seq = Seq('(', '[', '=', ':')
    val intSeq = seq.map(e => body.indexOf(e)).filter(p => p != -1)
    intSeq match{
      case Nil => ""
      case l => body.substring(defIndex + "def".length, l.min).trim
    }

  }
}

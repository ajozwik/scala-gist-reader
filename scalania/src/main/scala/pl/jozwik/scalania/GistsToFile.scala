package pl.jozwik.scalania

import pl.jozwik.gist.GistReader._
import org.apache.commons.io.FileUtils
import java.io.File
import scala.sys.process.Process
import java.nio.file.FileSystems

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
      case Right(body) => {
        content.append(body).append('\n').append('\n')
        if (!solutions.isEmpty) {
          solutions.append(",").append('\n')
        }

        solutions.append(extractMethodName(body.toString))
      }
      case Left(e) =>
    })

    toScalaObject(packageName, objectName, signatureOfMethod, solutions, content)

  }

  def cloneRepository(scalaniaDir:File) = {
    if (!scalaniaDir.exists()) {
      FileUtils.deleteDirectory(scalaniaDir)
      scalaniaDir.mkdirs()
      val pb = Process(Seq("git", "clone", "https://github.com/jaceklaskowski/scalania.git"), scalaniaDir.getParentFile)
      pb.lines.foreach(line => println(line))
    }
  }


  def storeFileWithTests(scalaniaDir:File,packageName:String,objectName:String,content:String) {
    val srcPath = FileSystems.getDefault().getPath(scalaniaDir.getAbsolutePath,"exercises","src","main","scala")
    val splitted = packageName.split("\\.")
    val packageDir = splitted.foldLeft(srcPath.toFile)((f,str) => new File(f,str))


    val location = new File(packageDir,objectName+".scala")
    Some(new java.io.PrintWriter(location)).foreach{f => try{f.write(content)}finally{f.close}}
  }

  private def toScalaObject(packageName: String, objectName: String, signatureOfMethod: String, solutions: StringBuilder, content: StringBuilder): String = {
    beginFile(packageName, objectName, signatureOfMethod) + solutions + closeSolutions + content + endFile
  }

  private def extractMethodName(body: String) = {
    val defIndex = body.indexOf("def")
    val openParenthesis = body.indexOf('(')
    val openParenthesis2 = body.indexOf('[')
    val index = if (openParenthesis2 == -1) {
      openParenthesis
    } else {
      openParenthesis2 min openParenthesis
    }
    body.substring(defIndex + "def".length, index).trim
  }
}

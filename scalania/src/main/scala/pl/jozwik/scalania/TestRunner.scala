package pl.jozwik.scalania

import java.io.File
import java.nio.file.FileSystems
import org.apache.commons.io.FileUtils
import pl.jozwik.scalania.GistsToFile._
import scala.sys.process.{ProcessLogger, Process}
import pl.jozwik.gist.GistReader
import scala.collection.mutable.ArrayBuffer
import java.net.URL

object TestRunner {

  def main(args: Array[String]) {
    val packageName = args(0)
    val objectName = args(1)
    val signature = args(2)
    val testName = args(3)

    uploadSolutionsAndRunTests(new URL("https://github.com/jaceklaskowski/scalania.git"),"exercises",packageName, objectName, signature, testName, Seq(7680647, 7680700))
  }


  def uploadSolutionsAndRunTests(repositoryUrl:URL,subProject:String,packageName: String, objectName: String, signature: String, testName: String, numbers: Seq[Int], url: String = GistReader.DEFAULT_URL): (Seq[String],String) = {
    val objectContent = gistsToFile(packageName, objectName, signature, numbers, url)


    val tmpDir = new File("tmp")
    val scalaniaDir = new File(tmpDir, "scalania")

    cloneRepository(scalaniaDir, repositoryUrl)

    storeFileWithTests(scalaniaDir,subProject, packageName, objectName, objectContent)

    (runSbt(packageName, testName, scalaniaDir),objectContent)
  }

  private def runSbt(packageName: String, testName: String, scalaniaDir: File):Seq[String] = {
    val sbtPb = Process(Seq("sbt", "testOnly " + packageName + s".$testName", ""), scalaniaDir)
    println(s"$sbtPb")

    val builder = new ArrayBuffer[String]()
    val a: Process = sbtPb.run(ProcessLogger(output => {
      println(output)
      builder += output
    }))
    a.exitValue()
    builder.toSeq
  }

  private def storeFileWithTests(scalaniaDir: File,subProject:String, packageName: String, objectName: String, content: String) {
    val srcPath = FileSystems.getDefault.getPath(scalaniaDir.getAbsolutePath, subProject, "src", "main", "scala")
    val splitted = packageName.split("\\.")
    val packageDir = splitted.foldLeft(srcPath.toFile)((f, str) => new File(f, str))
    val location = new File(packageDir, objectName + ".scala")
    Some(new java.io.PrintWriter(location)).foreach {
      f => try {
        f.write(content)
      } finally {
        f.close()
      }
    }
  }

  private def cloneRepository(destDir: File, url: URL) = {
    if (!destDir.exists()) {
      FileUtils.deleteDirectory(destDir)
      destDir.mkdirs()
      val pb = Process(Seq("git", "clone", url.toString), destDir.getParentFile)
      pb.lines.foreach(line => println(line))
    }
  }
}





package pl.jozwik.scalania

import java.io.File
import java.nio.file.FileSystems
import org.apache.commons.io.FileUtils
import pl.jozwik.scalania.GistsToFile._
import scala.sys.process.Process

object ScalaniaTest {

  def main(args: Array[String]) {
    val packageName = args(0)
    val objectName = args(1)
    val signature = args(2)
    val testName = args(3)

    uploadSolutionsAndRunTests(packageName, objectName, signature, testName, 7680647, 7680700)
  }


  def uploadSolutionsAndRunTests(packageName: String, objectName: String, signature: String, testName: String, numbers: Int*) = {
    val objectContent = gistsToFile(packageName, objectName, signature, numbers: _*)


    val tmpDir = new File("tmp")
    val scalaniaDir = new File(tmpDir, "scalania")

    cloneRepository(scalaniaDir, "https://github.com/jaceklaskowski/scalania.git")

    storeFileWithTests(scalaniaDir, packageName, objectName, objectContent)

    runSbt(packageName, testName, scalaniaDir)
  }

  private def runSbt(packageName: String, testName: String, scalaniaDir: File) = {
    val sbtPb = Process(Seq("sbt", "testOnly " + packageName + s".$testName", ""), scalaniaDir)
    println(s"$sbtPb")

    sbtPb.lines
  }

  private def storeFileWithTests(scalaniaDir: File, packageName: String, objectName: String, content: String) {
    val srcPath = FileSystems.getDefault().getPath(scalaniaDir.getAbsolutePath, "exercises", "src", "main", "scala")
    val splitted = packageName.split("\\.")
    val packageDir = splitted.foldLeft(srcPath.toFile)((f, str) => new File(f, str))
    val location = new File(packageDir, objectName + ".scala")
    Some(new java.io.PrintWriter(location)).foreach {
      f => try {
        f.write(content)
      } finally {
        f.close
      }
    }
  }

  private def cloneRepository(destDir: File, url: String) = {
    if (!destDir.exists()) {
      FileUtils.deleteDirectory(destDir)
      destDir.mkdirs()
      val pb = Process(Seq("git", "clone", url), destDir.getParentFile)
      pb.lines.foreach(line => println(line))
    }
  }
}





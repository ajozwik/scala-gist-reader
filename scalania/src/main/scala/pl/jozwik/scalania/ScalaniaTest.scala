import java.io.File
import pl.jozwik.scalania.GistsToFile._
import scala.sys.process.Process

object ScalaniaTest {

  def main(args: Array[String]) {
    val packageName = args(0)
    val objectName = args(1)
    val signature = args(2)
    val testName = args(3)

    uploadSolutionsAndRunTests(packageName, objectName, signature, testName)
  }


  def uploadSolutionsAndRunTests(packageName: String, objectName: String, signature: String, testName: String) {
    val objectContent = gistsToFile(packageName, objectName, signature, 7680647, 7680700)


    val tmpDir = new File("tmp")
    val scalaniaDir = new File(tmpDir, "scalania")

    cloneRepository(scalaniaDir)

    storeFileWithTests(scalaniaDir, packageName, objectName, objectContent)

    runSbt(packageName, testName, scalaniaDir)
  }

  def runSbt(packageName: String, testName: String, scalaniaDir: File) {
    val sbtPb = Process(Seq("sbt", "testOnly " + packageName + s".$testName", ""), scalaniaDir)
    println(s"$sbtPb")

    sbtPb.lines.foreach(line => println(line))
  }
}





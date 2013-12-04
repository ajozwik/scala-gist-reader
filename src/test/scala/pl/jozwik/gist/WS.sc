import java.io.File
import scala.sys.process.Process

val pb = Process(Seq("sbt" ,"test"),new File("/home/ajozwik/workspaceScala/scalania"))

val lines = pb.lines

lines.foreach(line => println(line))


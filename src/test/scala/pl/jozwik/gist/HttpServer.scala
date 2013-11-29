package pl.jozwik.gist

import java.lang.Character.{LETTER_NUMBER => CR, LINE_SEPARATOR => LF}
import java.net.InetSocketAddress
import java.nio.channels.AsynchronousChannelGroup._
import java.nio.channels.AsynchronousServerSocketChannel._
import java.nio.channels.{AsynchronousSocketChannel => ASC, CompletionHandler}
import java.nio.ByteBuffer._
import java.util.concurrent.Executors._
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source
import java.util.concurrent.atomic.AtomicBoolean

case class HttpServer(port: Int) {
  val channelGroup = withFixedThreadPool(1, defaultThreadFactory())
  val listenChannel = open(channelGroup).bind(new InetSocketAddress(port))
  val atomicBoolean = new AtomicBoolean(true)
  Future {
    atomicBoolean.set(false)
    while (true) {
      ProcessHandler(listenChannel.accept().get())()
    }
  }
  while (atomicBoolean.get) {
    Thread.sleep(10)
  }
}

case class ProcessHandler(ch: ASC) {
  val buf = allocate(1024)
  buf.flip()

  implicit def fn2CH(fn: Int => Unit) = new CompletionHandler[Integer, Void]() {
    def completed(res: Integer, _2: Void): Unit = fn(res)

    def failed(throwable: Throwable, _2: Void) = throw new RuntimeException(throwable)
  }

  def apply() = readLine {
    request_line => readHeader {
      header => ch.write(wrap(html), null, (res: Int) => ch.close())
    }
  }

  def readHeader(fn: List[String] => Unit, lines: ListBuffer[String] = new ListBuffer): Unit = readLine {
    case "" => fn(lines.toList)
    case any => readHeader(fn, lines += any)
  }

  def readLine(fn: String => Unit, sb: StringBuilder = new StringBuilder): Unit = readChar {
    case LF => readChar {
      case CR => fn(sb.toString)
    }
    case CR => fn(sb.toString)
    case any => readLine(fn, sb += any)
  }

  def readChar(fn: Char => Unit) = buf.hasRemaining() match {
    case true => fn(buf.get().toChar)
    case _ =>
      buf.clear()
      ch.read(buf, null, (res: Int) => if (res != -1) {
        buf.flip()
        fn(buf.get().toChar)
      } else ch.close())
  }

  val response = Source.fromInputStream(getClass.getResourceAsStream("/response.txt")).mkString
  val html = s"""
HTTP/1.1 200
Content-Type: text/html charset=ISO-8859-1

$response""".getBytes
}
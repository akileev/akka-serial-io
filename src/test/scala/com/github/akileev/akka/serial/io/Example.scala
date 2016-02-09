package com.github.akileev.akka.serial.io

import akka.actor._
import akka.io.IO
import akka.util.ByteString
import com.github.akileev.akka.serial.io.Serial._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class Example(port: String) extends Actor with ActorLogging {
  import context.system

  override def preStart = {
    IO(Serial) ! Open(self, port, 115200)
  }

  override def postStop = {
    log.info("Stopped")
    val whenTerminated = system.terminate()
    Await.result(whenTerminated, Duration.Inf)
  }

  override def receive = {
    case Opened(p) =>
      log.info("Connected to port {}", p)
      context become open(sender())

    case CommandFailed(_, cause) =>
      log.error(cause, "Could not connect to port")
      context stop self

    case other => log.info("Unknown message: {}", other)
  }

  def open(connection: ActorRef): Receive = {
    case "close" =>
      log.info("Closing")
      connection ! Close

    case s: String =>
      log.info("Sending to the port: {}", s)
      connection ! Write(ByteString(s))

    case Received(data) =>
      log.info("Received data from serial port: {}", data.decodeString("UTF-8"))

    case Closed =>
      log.info("Serial port closed")
      context stop self
  }
}

object Example extends App {
  val port = "COM4"

  val system = ActorSystem("Example")
  val actor = system.actorOf(Props(new Example(port)), "e1")

  //The following lines are just for the sake of an example, never program in
  // akka like that.. normally your logic would be embedded in the example
  // actor
  Thread.sleep(2000)
  actor ! "ATI\r\n"
  Thread.sleep(2000)
  actor ! "close"
  Thread.sleep(2000)

  val whenTerminated = system.terminate()
  Await.result(whenTerminated, Duration.Inf)
}
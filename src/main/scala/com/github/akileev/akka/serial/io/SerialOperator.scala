package com.github.akileev.akka.serial.io

import akka.actor._
import akka.util.ByteStringBuilder
import com.github.akileev.akka.serial.io.Serial._
import jssc.{SerialPort, SerialPortEvent, SerialPortEventListener}

private[io] class SerialOperator(port: SerialPort, handler: ActorRef) extends Actor {
  private case class DataAvailable(count: Int)

  context.watch(handler)

  handler ! Opened(port.getPortName)

  override def preStart = {
    val toNotify = self
    port.addEventListener(new SerialPortEventListener() {
      override def serialEvent(event: SerialPortEvent) {
        event.getEventType match {
          case SerialPortEvent.RXCHAR => toNotify ! DataAvailable(event.getEventValue)
          case _ =>
        }
      }
    })
  }

  override def postStop = {
    handler ! Closed
    if (port.isOpened)
      port.closePort()
  }

  override def receive = {
    case Close =>
      port.closePort()
      if (sender != handler) sender ! Closed
      context.stop(self)

    case Write(data, ack) =>
      port.writeBytes(data.toArray)
      if (ack != NoAck) sender ! ack

    case DataAvailable(count) =>
      val data = read(count)
      if (data.nonEmpty) handler ! Received(data)

    case Terminated(actor) =>
      self ! Close
  }

  private def read(count: Int) = {
    val bsb = new ByteStringBuilder

    val data = port.readBytes(count)

    bsb ++= data
    bsb.result
  }
}
private[io] object SerialOperator {
  def props(port: SerialPort, commander: ActorRef) = Props(new SerialOperator(port, commander))
}
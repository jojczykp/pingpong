package pl.jojczykp.pingpong.actor

import akka.actor.Actor
import pl.jojczykp.pingpong.message.{StopMessage, PingMessage, PongMessage}
import pl.jojczykp.pingpong.tools.Output

class PongActor extends Actor with Output {

	override def receive = {
		case PongMessage(text) => onPong(text)
		case StopMessage() => onStop()
		case _ => onNotRecognized()
	}

	def onPong(text: String) = {
		println("Pong: " + text)
		sender() ! PingMessage(text)
	}

	def onStop() = {
		context.stop(self)
	}

	def onNotRecognized() = {
		println("Pong: Error: Message type not recognized")
	}

}

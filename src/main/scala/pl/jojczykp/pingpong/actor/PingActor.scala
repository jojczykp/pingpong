package pl.jojczykp.pingpong.actor

import akka.actor.{ActorRef, Actor}
import pl.jojczykp.pingpong.message.{StopMessage, PongMessage, StartMessage, PingMessage}
import pl.jojczykp.pingpong.tools.Output

class PingActor(pongActor: ActorRef) extends Actor with Output {

	private[actor] var remainingRepeats: Int = _

	override def receive = {
		case StartMessage(repeats, text) => onStart(repeats, text)
		case PingMessage(text) => onPing(text)
		case _ => onNotRecognized()
	}

	private def onStart(repeats: Int, text: String) = {
		remainingRepeats = repeats
		self ! PingMessage(text)
	}

	private def onPing(text: String) = {
		if (remainingRepeats == 0) {
			stopPingPong()
		} else {
			nextPingPong(text)
		}
	}

	private def stopPingPong() = {
		pongActor ! StopMessage()
		context.stop(self)
	}

	private def nextPingPong(text: String) = {
		println("Ping: " + text)
		remainingRepeats -= 1
		pongActor ! PongMessage(text)
	}

	private def onNotRecognized() = {
		println("Ping: Error: Message type not recognized")
	}

}

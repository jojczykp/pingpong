package pl.jojczykp.pingpong.actor

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestProbe}
import org.scalatest.FlatSpec
import pl.jojczykp.pingpong.message.{PingMessage, PongMessage, StopMessage}
import pl.jojczykp.pingpong.tools.Output

class PongActorSpec extends FlatSpec {

	val TEST_MESSAGE = "test message"

	trait MockedOutput extends Output {
		var messages: Seq[String] = Seq()

		override def println(s: String) = messages :+= s
	}

	trait Context {
		implicit val system = ActorSystem("ActorSystemForTests")
		val pingActor = new TestProbe(system)
		val pongActorRef = TestActorRef(new PongActor() with MockedOutput)
		val pongActor = pongActorRef.underlyingActor
	}

	"Pong actor" should "print pong text from pong message and reply to sender" in new Context {
		pongActorRef receive (PongMessage(TEST_MESSAGE), pingActor.ref)

		assert(pongActor.messages == List("Pong: " + TEST_MESSAGE))
		pingActor expectMsg PingMessage(TEST_MESSAGE)
	}

	"Pong actor" should "stop if no repeats remain" in new Context {
		pongActor receive StopMessage()

		assert(pongActorRef.underlying.isTerminated)
	}

	"Pong actor" should "print error on incorrect message" in new Context {
		pongActor receive AnyRef

		assert(pongActor.messages == List("Pong: Error: Message type not recognized"))
	}

}

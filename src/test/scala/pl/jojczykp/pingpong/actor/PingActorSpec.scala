package pl.jojczykp.pingpong.actor

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestProbe}
import org.scalatest.FlatSpec
import pl.jojczykp.pingpong.message.{PingMessage, PongMessage, StartMessage, StopMessage}
import pl.jojczykp.pingpong.tools.Output

class PingActorSpec extends FlatSpec {

	val REPEATS = 1234
	val TEST_MESSAGE = "test message"

	trait MockedOutput extends Output {
		var messages: Seq[String] = Seq()

		override def println(s: String) = messages :+= s
	}

	trait Context {
		implicit val system = ActorSystem("ActorSystemForTests")
		val pongActor = new TestProbe(system)
		val pingActorRef = TestActorRef(new PingActor(pongActor.ref) with MockedOutput)
		val pingActor = pingActorRef.underlyingActor
	}

	"Ping actor" should "send ping message to itself" in new Context {
		pingActor receive StartMessage(REPEATS, TEST_MESSAGE)

		pongActor expectMsg PongMessage(TEST_MESSAGE)
	}

	"Ping actor" should "print ping text from ping message and start next iteration if repeats remain" in new Context {
		pingActor.remainingRepeats = 1234

		pingActor receive PingMessage(TEST_MESSAGE)

		assert(pingActor.messages == List("Ping: " + TEST_MESSAGE))
		pongActor expectMsg PongMessage(TEST_MESSAGE)
	}

	"Ping actor" should "stop if no repeats remain" in new Context {
		pingActor.remainingRepeats = 0

		pingActor receive PingMessage(TEST_MESSAGE)

		pongActor expectMsg StopMessage()
		assert(pingActorRef.underlying.isTerminated)
	}

	"Ping actor" should "print error on incorrect message" in new Context {
		pingActor receive AnyRef

		assert(pingActor.messages == List("Ping: Error: Message type not recognized"))
	}

}

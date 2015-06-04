package pl.jojczykp.pingpong

import akka.actor.{ActorSystem, Props}
import pl.jojczykp.pingpong.actor.{PingActor, PongActor}
import pl.jojczykp.pingpong.message.StartMessage

object Main extends App {
	val system = ActorSystem("HelloSystem")

	val pongActor = system.actorOf(Props[PongActor], name="PongActor")
	val pingActor = system.actorOf(Props(new PingActor(pongActor)), name="PingActor")

	pingActor ! StartMessage(3, "Ala ma kopa!")
}

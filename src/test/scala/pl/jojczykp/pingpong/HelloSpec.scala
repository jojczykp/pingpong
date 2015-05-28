package pl.jojczykp.pingpong

import org.scalatest.FlatSpec

class HelloSpec extends FlatSpec {

	trait Context {
		def testee = new Hello
	}

	"Hello object" should "say hello" in new Context {
		val said: String = testee.sayHello

		assert(said == "Hello world!")
	}

}

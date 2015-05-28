package pl.jojczykp.pingpong

import org.mockito.BDDMockito._
import org.scalatest.FlatSpec
import org.scalatest.mock.MockitoSugar

class MainSpec extends FlatSpec with MockitoSugar {

	trait Context {
		val MESSAGE = "someMessage"
		val hello = mock[Hello]
	}

	"Main" should "repeat what hello said" in new Context {
		given(hello.sayHello).willReturn(MESSAGE)

		val said: String = hello.sayHello

		assert(said == MESSAGE)
	}

}


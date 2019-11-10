package tests

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.testkit.{ImplicitSender, TestKit}
import auth._
import mvc.View
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import play.api.libs.json.{JsValue, Json}

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._

class TestActors extends TestKit(ActorSystem("TestAuth"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }


  "auth" must {
    "auth appropriately" in {
      //bob pw 123456#@
      //yeehaw pw 6964#69@
      //peewee pw abc123#@56
      val server=system.actorOf(Props(classOf[AuthenticationTestServer]))

      // Wait for the server to start
      expectNoMessage(10000.millis)

      val client = system.actorOf(Props(classOf[SocketClient]))
      expectNoMessage(1000.millis)
      client ! Login("yeehaw","6964#69@")
      expectNoMessage(1000.millis)
      client ! Register("awdawaw","84514512")
      expectNoMessage(1000.millis)
      client ! Login("yeehaw", "1321321321321#@")
    }
  }
  //{"playerParty":{"location":{"x":250,"y":250},"level":5,"inBattle":false},"otherParties":[{"location":{"x":200,"y":200},"level":5,"inBattle":true},{"location":{"x":300.5,"y":300},"level":12,"inBattle":false}]}
}

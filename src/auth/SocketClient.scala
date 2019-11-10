package auth

import akka.actor.{Actor, ActorRef}
import io.socket.client.{IO, Socket}
import io.socket.emitter.Emitter
import play.api.libs.json.{JsValue, Json}
class SocketClient() extends Actor {

  var socket: Socket = IO.socket("http://localhost:8080/")
  socket.on("register_result", new RegisterResult)
  socket.on("failed_login",new FailedtoLogin)
  socket.on("authenticated",new AuthenticatedJSON)
  socket.connect()


  override def receive: Receive = {
    case register: Register =>
      val info:Array[String]=Array(register.username,register.password)
      socket.emit("register",info)
    case login: Login =>
      socket.emit("login",Array(login.username,login.password))
    case saveGame: SaveGame =>
      socket.emit("savegame",Array(saveGame.username,saveGame.charactersJSON))
  }

  override def postStop(): Unit = {
    println("closing socket")
    socket.close()
  }

}
class RegisterResult() extends Emitter.Listener{
  override def call(args: Object*): Unit = {
    println("Client-"+args.apply(0).toString)
  }
}
class FailedtoLogin() extends Emitter.Listener{
  override def call(args: Object*): Unit = {
    println("Client-"+args.apply(0).toString)
  }
}
class AuthenticatedJSON() extends Emitter.Listener{
  override def call(args: Object*): Unit = {
    println("Client-"+args.apply(0).toString)
  }
}


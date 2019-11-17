package auth

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.corundumstudio.socketio.{AckRequest, Configuration, SocketIOClient, SocketIOServer}
import com.corundumstudio.socketio.listener._

import scala.collection.mutable

class AuthenticationTestServer() extends Actor {

  // Very little setup is provided for authentication. If you are completing authentication for your team
  // you must also design and implement a socket server and front end for your demo that will allow you to
  // to prove that all the required functionality is implemented

  var usernametoclient:mutable.Map[String,SocketIOClient]=mutable.Map()
  var clienttousername:mutable.Map[SocketIOClient,String]=mutable.Map()
  val authenticationSystem: ActorRef = this.context.actorOf(Props(classOf[AuthenticationSystem]))

  val config: Configuration = new Configuration {
    setHostname("localhost")
    setPort(8080)
  }

  val server: SocketIOServer = new SocketIOServer(config)
  server.addEventListener("register",classOf[Array[String]],new RegisterListener(this))
  server.addEventListener("login",classOf[Array[String]],new LoginListener(this))
  server.addEventListener("savegame",classOf[Array[String]],new SaveListener(this))
  server.start()

  override def receive: Receive = {
    //errors
    //to auth system
    case register: Register=>
      authenticationSystem ! Register(register.username,register.password)
    case login: Login=>
      authenticationSystem ! Login(login.username,login.password)
    case saveGame: SaveGame=>
      authenticationSystem ! SaveGame(saveGame.username,saveGame.charactersJSON)
      //to client
    case registrationResult: RegistrationResult=>
      usernametoclient(registrationResult.username).sendEvent("register_result",registrationResult.message)
    case failedLogin: FailedLogin =>
      usernametoclient(failedLogin.username).sendEvent("failed_login",failedLogin.message)
    case authenticated: Authenticated=>
      usernametoclient(authenticated.username).sendEvent("authenticated",authenticated.charactersJSON)
  }

  override def postStop(): Unit = {
    println("stopping server")
    server.stop()
  }

}
class RegisterListener(server:AuthenticationTestServer) extends DataListener[Array[String]]{

  override def onData(client: SocketIOClient, data: Array[String], ackSender: AckRequest): Unit = {
    val username:String=data.apply(0)
    val password:String=data.apply(1)
    //println("registerlistener")
    server.self ! Register(username,password)
    if (!server.usernametoclient.contains(username)){
      server.usernametoclient+=(username->client)
      server.clienttousername+=(client->username)
    }
  }
}
class LoginListener(server:AuthenticationTestServer) extends DataListener[Array[String]]{
  override def onData(client: SocketIOClient, data: Array[String], ackSender: AckRequest): Unit = {
    val username:String=data.apply(0)
    val password:String=data.apply(1)
    println("LoginListener")
    println(username + password)
    server.self ! Login(username,password)
    if (!server.usernametoclient.contains(username)){
      server.usernametoclient+=(username->client)
      server.clienttousername+=(client->username)
    }
  }
}
class SaveListener(server:AuthenticationTestServer) extends DataListener[Array[String]]{
  override def onData(client: SocketIOClient, data: Array[String], ackSender: AckRequest): Unit = {
    val username:String=data.apply(0)
    val json:String=data.apply(1)
    server.self ! SaveGame(username,json)
  }
}
object Server{
  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem()
    val server=actorSystem.actorOf(Props(classOf[AuthenticationTestServer]))
  }
}
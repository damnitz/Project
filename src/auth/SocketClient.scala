package auth


import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import io.socket.client.{IO, Socket}
import io.socket.emitter.Emitter
import play.api.libs.json.{JsValue, Json}
import scalafx.application.JFXApp
import javafx.event.{ActionEvent, EventHandler}
import scalafx.scene.control.{Button, Label, TextArea, TextField}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
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
    GUI.resulttext.text.value=args.apply(0).toString
  }
}
class FailedtoLogin() extends Emitter.Listener{
  override def call(args: Object*): Unit = {
    println("Client-"+args.apply(0).toString)
    GUI.resulttext.text.value=args.apply(0).toString
  }
}
class AuthenticatedJSON() extends Emitter.Listener{
  override def call(args: Object*): Unit = {
    println("Client-"+args.apply(0).toString)
    GUI.resulttext.text.value=args.apply(0).toString
  }
}
class HandleLogin(username:TextField,password:TextField,client: ActorRef) extends EventHandler[ActionEvent]{
  override def handle(event: ActionEvent): Unit = {
    client ! Login(username.text.value,password.text.value)
  }
}
class HandleRegister(username:TextField,password:TextField,client:ActorRef) extends EventHandler[ActionEvent]{
  override def handle(event: ActionEvent): Unit = {
    client ! Register(username.text.value,password.text.value)
  }
}
class HandleSaveGame(username:TextField,save:TextField,client:ActorRef) extends EventHandler[ActionEvent]{
  override def handle(event: ActionEvent): Unit = {
    client ! SaveGame(username.text.value,save.text.value)
  }
}
object GUI extends JFXApp{
  val actorSystem = ActorSystem()
  val client = actorSystem.actorOf(Props(classOf[SocketClient]))
  var vbox1:VBox=new VBox()
  var hbox1:HBox=new HBox()
  var hbox2:HBox=new HBox()
  var hbox3:HBox=new HBox()
  var hbox4:HBox=new HBox()
  val usernamelabel:Label=new Label(){
    text="Username"
  }
  val passwordLabel:Label=new Label(){
    text="Password"
  }
  val savelabel:Label=new Label(){
    text="SaveGame"
  }
  var usernametext:TextField=new TextField(){
    //minHeight=10
    text="username"
  }
  var passwordtext:TextField=new TextField(){
    //minHeight=10
    text="password"
  }
  var resulttext:TextArea=new TextArea(){
    editable=false
    minHeight=200
    minWidth=200
  }
  resulttext.setWrapText(true)
  var savegametext:TextField=new TextField(){
    text="enter savegame"
  }
  val loginbutton:Button=new Button(){
    minWidth=100
    minHeight=100
    text="Login"
    onAction= new HandleLogin(usernametext,passwordtext,client)
  }
  val registerbutton:Button=new Button(){
    minWidth=100
    minHeight=100
    text="Register"
    onAction= new HandleRegister(usernametext,passwordtext,client)
  }
  val savebutton:Button=new Button(){
    minWidth=100
    minHeight=100
    text="Save"
    onAction=new HandleSaveGame(usernametext,savegametext,client)
  }

  hbox1.children.addAll(loginbutton,registerbutton,savebutton)
  hbox2.children.addAll(usernamelabel,usernametext)
  hbox3.children.addAll(passwordLabel,passwordtext)
  hbox4.children.addAll(savelabel,savegametext)
  vbox1.children.addAll(hbox1,hbox2,hbox3,hbox4,resulttext)
  this.stage = new PrimaryStage {
    title = "Login Pls"
    scene = new Scene() {
      content = List(
        vbox1
      )
    }

  }
}

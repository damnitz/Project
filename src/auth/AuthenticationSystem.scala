package auth
import akka.actor._
import scala.util.hashing.MurmurHash3._
import java.sql.{Connection, DriverManager, ResultSet}
case class Register(username:String,password:String)
case class Login(username:String,password:String)
case class SaveGame(username:String,partyJSON:String)
case class Error(errormsg:String)

class AuthenticationSystem extends Actor {
  val url = "jdbc:mysql://localhost/testschema"
  val username = "root"
  val password = "1"
  var connection: Connection = DriverManager.getConnection(url, username, password)
  var statement=connection.createStatement()
  statement.execute("CREATE TABLE IF NOT EXISTS playerinfo (USERNAME VARCHAR , PASSWORD INT , JSON VARCHAR )")
  def receive: Receive ={
    case register:Register=>
      if(!register.username.isEmpty){
        val result:ResultSet=statement.executeQuery("SELECT USERNAME FROM playerinfo")
        var usernametaken:Boolean=false
        while(result.next()){//iterating through the column of usernames trying to find if the username trying to register is in there
          val dbusername=result.getString("USERNAME")
          if (register.username==dbusername){
            usernametaken=true
          }
        }
        if (!usernametaken){//if username is not taken
          if(register.password.length>=5 && register.password.contains("#") && register.password.contains("@")){
            val hashedsaltpass:Int=stringHash(register.password+"SALT")
            val prepared=connection.prepareStatement("INSERT INTO playerinfo VALUE (?,?,?)")
            prepared.setString(1,register.username)
            prepared.setInt(2,hashedsaltpass)
            prepared.execute()//next time split up the triple if statement
          }
        }
        else{//executed if username is already in the database
          sender() ! Error("This username has already been taken!")
        }
      }
      else{//executed if username is blank
        sender() ! Error("username cannot be blank")
      }

    case login:Login=>
    case savegame:SaveGame=>
  }
}

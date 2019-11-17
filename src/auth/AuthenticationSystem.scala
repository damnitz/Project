package auth
import akka.actor._

import scala.util.hashing.MurmurHash3._
import java.sql.{Connection, DriverManager, ResultSet}
import scala.util.Random
import scala.collection.mutable._

case class Register(username: String, password: String)
case class RegistrationResult(username: String, registered: Boolean, message: String)
case class Login(username: String, password: String)
case class SaveGame(username: String, charactersJSON: String)
case class FailedLogin(username: String, message: String)
case class Authenticated(username: String, charactersJSON: String)


class AuthenticationSystem() extends Actor {
  val url = "jdbc:mysql://localhost/project"
  val username = "root"
  val password = ""
  var connection: Connection = DriverManager.getConnection(url, username, password)
  var statement=connection.createStatement()
  statement.execute("CREATE TABLE IF NOT EXISTS playerinfo (USERNAME VARCHAR(200) ,JSON VARCHAR(10000),SALT VARCHAR(10)) ")
  statement.execute("CREATE TABLE IF NOT EXISTS playerpass (USERNAME VARCHAR(200) ,PASSWORD INT) ")

  override def receive: Receive ={
    case register:Register=>
      if(!register.username.isEmpty){
        val result:ResultSet=statement.executeQuery("SELECT USERNAME FROM playerpass")

        var usernametaken:Boolean=false
        while(result.next()){//iterating through the column of usernames trying to find if the username trying to register is in there
          val dbusername=result.getString("USERNAME")
          if (register.username==dbusername){
            usernametaken=true
          }
        }

        if (!usernametaken){//if username is not taken
          val generatesalt=Random.alphanumeric.take(4).mkString
          if(register.password.length>=5 && register.password.contains("#") && register.password.contains("@")){
            val hashedsaltpass:Int=stringHash(register.password+generatesalt)
            val prepared=connection.prepareStatement("INSERT INTO playerpass VALUES (?,?)")
            prepared.setString(1,register.username)
            prepared.setInt(2,hashedsaltpass)
            prepared.execute()
            val prepared1=connection.prepareStatement("INSERT INTO playerinfo VALUES (?,?,?)")
            prepared1.setString(1,register.username)
            prepared1.setString(2,"No savefile created")
            prepared1.setString(3,generatesalt)
            prepared1.execute()
            sender() ! RegistrationResult(register.username,true,"You have successfully registered!")
          }

          else if (register.password.length<5 && !register.password.contains("#") && !register.password.contains("@")){
            sender() ! RegistrationResult(register.username,false,"Your password must be atleast 5 characters long, contain '#' and '@'.")
          }
          else if(!register.password.contains("#") && !register.password.contains("@")){
            sender() ! RegistrationResult(register.username,false,"Your password must contain '#' and '@'.")
          }
          else if(register.password.length<5 && !register.password.contains("@")){
            sender() ! RegistrationResult(register.username,false,"Your password must be atleast 5 characters long and contain '@'.")
          }
          else if(register.password.length<5 && !register.password.contains("#")){
            sender() ! RegistrationResult(register.username,false,"Your password must be atleast 5 characters long and contain '#'.")
          }
          else if(!register.password.contains("@")){
            sender() ! RegistrationResult(register.username,false,"Your password must contain '@'.")
          }
          else if(!register.password.contains("#")){
            sender() ! RegistrationResult(register.username,false,"Your password must contain '#'.")
          }
          else if(register.password.length<5){
            sender() ! RegistrationResult(register.username,false,"Your password must be atleast 5 characters long.")
          }
        }

        else{//executed if username is already in the database
          //println("authsystem-usernametaken")
          sender() ! RegistrationResult(register.username,false,"This username has already been taken!")
        }
      }

      else{//executed if username is blank
        //println("authsystem-blankusername")
        sender() ! RegistrationResult(register.username,false,"Username cannot be blank")
      }

    case login:Login=>
      val result:ResultSet=statement.executeQuery("SELECT * from playerpass")
      val statement1=connection.createStatement()
      val resultDB:ResultSet=statement1.executeQuery("SELECT * from playerinfo")
      val getSalt=connection.prepareStatement("SELECT SALT from playerinfo WHERE USERNAME=?")
      getSalt.setString(1,login.username)
      val salt=getSalt.executeQuery()
      var realsalt:String=""
      while(salt.next()){
        val slt=salt.getString("SALT")

        realsalt=slt
      }
      println(realsalt)
      val hashedpass=stringHash(login.password+realsalt)
      var registered:Boolean=false
      var passcorrect:Boolean=false
      var lstusername:ListBuffer[String]=ListBuffer()
      var lstpass:ListBuffer[Int]=ListBuffer()
      var lstjson:ListBuffer[String]=ListBuffer()

      while(resultDB.next()){
        val dbjso=resultDB.getString("JSON")
        lstjson+=dbjso
      }

      while(result.next()){
        val dbusername=result.getString("USERNAME")
        val dbpass=result.getInt("PASSWORD")
        lstusername+=dbusername
        lstpass+=dbpass
        if(login.username==dbusername){
          registered=true
        }
      }
      if(registered){
        for(username<-lstusername){
          for(password<-lstpass){
            if(hashedpass==password && login.username==username ){
              passcorrect=true
            }
          }
        }
        if(passcorrect){
          var index:Int=0
          for(username<-lstusername){
            index+=1
            if (login.username==username){
              sender() ! Authenticated(login.username,lstjson.apply(index-1))
            }
          }
        }
        else{
          sender() ! FailedLogin(login.username,"Your password is incorrect!")
        }
      }
      else{
        sender() ! FailedLogin(login.username,"You are not registered!")
      }
    case savegame:SaveGame=>
      val preparedsave=connection.prepareStatement("UPDATE playerinfo SET JSON=?WHERE USERNAME=?")
      preparedsave.setString(2,savegame.username)
      preparedsave.setString(1,savegame.charactersJSON)
      preparedsave.execute()
      //sender() ! Message("Saved!")
  }
}
//{"playerParty":{"location":{"x":250,"y":250},"level":5,"inBattle":false},"otherParties":[{"location":{"x":200,"y":200},"level":5,"inBattle":true},{"location":{"x":300.5,"y":300},"level":12,"inBattle":false}]}
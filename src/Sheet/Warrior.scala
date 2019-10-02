package Sheet

import scala.collection.mutable.ListBuffer

class Warrior extends Character {
  var Attack:Int=30
  var Defense:Int=20
  var MAtt:Int=0
  var MDef:Int=5
  var MaxHP:Int=200
  var MaxMP:Int=100
  var CurrentHP:Int=this.MaxHP
  var CurrentMP:Int=this.MaxMP

  var Dead:Boolean=false

  var Experience:Int=0
  var Level:Int=0

  var skill1Flag:Boolean=false
  var skill2Flag:Boolean=false
  var skill3Flag:Boolean=false

  override def takeDamage(dmg:Int): Unit ={
    this.CurrentHP=this.CurrentHP-dmg
    if(this.CurrentHP<=0){
      this.Dead=true
      this.CurrentHP=0
    }
  }
  override def PhysicalAttack(defender:Character): Unit ={
    var dmg=this.Attack-defender.Defense
    defender.takeDamage(dmg)
  }
  override def warriorRage(defender:Character): Unit ={
    var dmg=(math.floor(this.Attack*1.2).toInt)-defender.Defense
    this.CurrentHP -= 30
    defender.takeDamage(dmg)
  }
  override def MagicalAttack(defender:Character):Unit = {}

  override def EXPgain(XP:Int): Unit ={
    this.Experience=this.Experience+XP
    var Levelsgained=(math.floor(this.Experience/1000)).toInt
    this.Level=this.Level+Levelsgained
    this.Experience=this.Experience-(Levelsgained*1000)
    if (Levelsgained>=1){
      this.Attack+=Levelsgained*10
      this.Defense+=Levelsgained*10
      this.MAtt+=Levelsgained*0
      this.MDef+=Levelsgained*10
      this.MaxHP+=Levelsgained*10
      this.MaxMP+=Levelsgained*10
    }
    if (this.Level>2 && this.skill1Flag==false){
      battleOptions()+="Rage"
      this.skill1Flag=true
    }
  }

  override def KillCharacterGetXP(Char:Character): Unit ={
    var DroppedXP=Char.MaxHP*5
    this.EXPgain(DroppedXP)
  }

  override def PartyCharacterXP(): Int ={
    var DroppedXP=this.MaxHP*5
    DroppedXP
  }
  override def battleOptions():ListBuffer[String]={
    var options=ListBuffer("Physical Attack")
    options
  }
  override def takeAction(x:String,y:Character): Unit ={
    if (x=="Physical Attack"){
      this.PhysicalAttack(y)
    }
    else if(x=="Rage" && this.skill1Flag==true){
      this.warriorRage(y)
    }
  }
}

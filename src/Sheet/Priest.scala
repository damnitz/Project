package Sheet

import scala.collection.mutable.ListBuffer

class Priest extends Character{
  var Attack:Int=10
  var Defense:Int=5
  var MAtt:Int=30
  var MDef:Int=10
  var MaxHP:Int=100
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
    var dmg=(this.Attack*1.2).toInt-defender.Defense
    defender.takeDamage(dmg)
  }
  override def MagicalAttack(defender:Character):Unit = {
    if(this.CurrentMP>=10) {
      var dmg = this.MAtt - defender.MDef
      this.CurrentMP = this.CurrentMP - 10
      defender.takeDamage(dmg)
    }
  }
  override def heal(healed:Character){
    if(this.CurrentMP>=20){
      this.CurrentMP -= 20
      if(healed.CurrentHP+50>healed.MaxHP) {
        var heal = -(healed.MaxHP-healed.CurrentHP)
        healed.takeDamage(heal)
      }
      else{
        healed.takeDamage(-50)
      }
    }
  }
  override def EXPgain(XP:Int): Unit ={
    this.Experience=this.Experience+XP
    var Levelsgained=(math.floor(this.Experience/1000)).toInt
    this.Level=this.Level+Levelsgained
    this.Experience=this.Experience-(Levelsgained*1000)
    if (Levelsgained>=1){
      this.Attack+=Levelsgained*10
      this.Defense+=Levelsgained*10
      this.MAtt+=Levelsgained*10
      this.MDef+=Levelsgained*10
      this.MaxHP+=Levelsgained*10
      this.MaxMP+=Levelsgained*20
    }
    if (this.Level>2 && this.skill1Flag==false){
      battleOptions()+="Heal"
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
    var options=ListBuffer("Magical Attack")
    options
  }
  override def takeAction(x:String,y:Character): Unit ={
    if (x=="Magical Attack"){
      this.MagicalAttack(y)
    }
    else if(x=="Heal" && this.skill1Flag==true){
      this.heal(y)
    }
  }
}

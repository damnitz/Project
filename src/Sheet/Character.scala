package Sheet

class Character {
  var Attack:Int=50
  var Defense:Int=5
  var MAtt:Int=0
  var MDef:Int=5
  var MaxHP:Int=100
  var MaxMP:Int=100
  var CurrentHP:Int=this.MaxHP
  var CurrentMP:Int=this.MaxMP

  var Dead=false

  var Experience=0
  var Level=0

  def takeDamage(dmg:Int): Unit ={
    this.CurrentHP=this.CurrentHP-dmg
    if(this.CurrentHP<=0){
      this.Dead=true
      this.CurrentHP=0
    }
  }
  def PhysicalAttack(defender:Character): Unit ={
    var dmg=this.Attack-defender.Defense
    defender.takeDamage(dmg)
  }
  def MagicalAttack(defender:Character):Unit = {
    if(this.CurrentMP>=10) {
      var dmg = this.MAtt - defender.MDef
      this.CurrentMP = this.CurrentMP - 10
      defender.takeDamage(dmg)
    }
  }
  def EXPgain(XP:Int): Unit ={
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
      this.MaxMP+=Levelsgained*10
    }
  }
  def KillCharacterGetXP(Char:Character): Unit ={
    var DroppedXP=Char.MaxHP*5
    this.EXPgain(DroppedXP)
  }
  def PartyCharacterXP(): Int ={
    var DroppedXP=this.MaxHP*5
    DroppedXP
  }
}

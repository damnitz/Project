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
}

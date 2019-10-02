package tests
import org.scalatest._
import Sheet.{Character, Priest, Warrior}

class TestMagicalAttack extends FunSuite{
  /*test("MaGik"){
    val char1:Character=new Character
    val char2:Character=new Character
    char1.MAtt=60
    char2.MDef=30
    char1.MagicalAttack(char2)
    assert(char1.CurrentMP==90)
    assert(char2.CurrentHP==70)
  }
  test("no MP"){
    val char3:Character=new Character
    val char4:Character=new Character
    char3.MAtt=100
    char3.CurrentMP=8
    char4.MDef=30
    char3.MagicalAttack(char4)
    assert(char3.CurrentMP==8)
    assert(char4.CurrentHP==100)
  }*/
  test("priest heal"){
    val war1:Character=new Warrior
    val pri1:Character=new Priest
    war1.takeDamage(30)
    pri1.takeAction("Heal",war1)
    assert(pri1.CurrentMP==100)
    assert(war1.CurrentHP==170)
    pri1.EXPgain(3000)

    pri1.takeAction("Heal",war1)
    assert(pri1.CurrentMP==80)
    assert(war1.CurrentHP==200)

    pri1.MagicalAttack(war1)
    assert(pri1.CurrentMP==70)
    assert(war1.CurrentHP==145)

    pri1.takeAction("Heal",war1)
    assert(war1.CurrentHP==195)
  }

}

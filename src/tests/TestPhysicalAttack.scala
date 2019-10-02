package tests
import org.scalatest._
import Sheet.{Character, Warrior}

class TestPhysicalAttack extends FunSuite{
  /*test("DMG"){
    val char1:Character=new Character
    val char2:Character=new Character
    char1.Attack=70
    char2.Defense=60
    char1.PhysicalAttack(char2)
    assert(char2.CurrentHP==90)
  }*/
  test("DMG"){
    val war1:Character=new Warrior
    val war2:Character=new Warrior
    war1.takeAction("Physical Attack",war2)
    assert(war2.CurrentHP==190)
    war1.takeAction("Rage",war2)
    assert(war2.CurrentHP==190)
    assert(war1.CurrentHP==200)
  }
  test("ragee"){
    val war1:Character=new Warrior
    val war2:Character=new Warrior
    war1.EXPgain(3000)
    war1.takeAction("Rage",war2)
    assert(war1.CurrentHP==170)
    assert(war2.CurrentHP==148)
  }
}

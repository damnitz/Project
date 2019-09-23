package tests
import org.scalatest._
import Sheet.Character
import Sheet.Party
class TestEXP extends FunSuite {
  test("XP=69"){
    val char1 = new Character
    char1.EXPgain(6900)
    assert(char1.Level==6)
    assert(char1.Experience==900)
  }
  test("kill"){
    val char2= new Character
    val char3 = new Character
    char3.MaxHP=500
    char3.CurrentHP=500 //2500XP
    char2.KillCharacterGetXP(char3) // Char2 gains 2500XP EXP
    assert(char2.Level==2)
    assert(char2.Experience==500)//leftover xp
    assert(char2.Attack==70)
  }
}

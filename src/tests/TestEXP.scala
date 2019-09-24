package tests
import org.scalatest._
import Sheet.Character
import Sheet.Party
class TestEXP extends FunSuite {
  test("XP=6900"){
    val char1 = new Character
    char1.EXPgain(6900)
    assert(char1.Level==6)
    assert(char1.Experience==900)
    assert(char1.Attack==110)
  }
  test("XP=1"){
    val char1 = new Character
    char1.EXPgain(1)
    assert(char1.Level==0)
    assert(char1.Experience==1)
  }
  test("kill"){
    val char2= new Character
    val char3 = new Character//every level is 1000 xp points so char 3 gained 20 levels
    char3.EXPgain(20000)//Char 3 now has 100+(10*20) maxhealth= 300
    char2.KillCharacterGetXP(char3) // Char2 gains 1500XP because EXP is based on maxhealth*5
    assert(char2.Level==1)
    assert(char2.Experience==500)//leftover xp
    assert(char2.Attack==60)
  }
  test("killing"){
    val char2= new Character
    char2.EXPgain(2500)//char2 is now level 2 with 500 leftover XP
    val char3 = new Character//every level is 1000 xp points so char 3 gained 20 levels
    char3.EXPgain(20000)//Char 3 now has 100+(10*20) maxhealth= 300
    char2.KillCharacterGetXP(char3) // Char2 gains 1500XP because EXP is based on maxhealth*5
    assert(char2.Level==4)
    assert(char2.Experience==0)//leftover xp
    assert(char2.Attack==90)
  }
}

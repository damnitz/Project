package tests
import org.scalatest._
import Sheet.{Character, Party, Priest, Warrior}
class TestEXP extends FunSuite {
  /*test("XP=6900"){
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
    val char3 = new Character

    char3.EXPgain(20000)//Char 3 now has 100+(10*20) maxhealth= 300, every level is 1000 xp points so char 3 gained 20 levels

    char2.KillCharacterGetXP(char3) // Char2 gains 1500XP because EXP is based on maxhealth*5
    assert(char2.Level==1)
    assert(char2.Experience==500)//leftover xp
    assert(char2.Attack==60)
  }
  test("killing"){
    val char2= new Character

    char2.EXPgain(2500)//char2 is now level 2 with 500 leftover XP

    val char3 = new Character

    char3.EXPgain(20000)//Char 3 now has 100+(10*20) maxhealth= 300,every level is 1000 xp points so char 3 gained 20 levels

    char2.KillCharacterGetXP(char3) // Char2 gains 1500XP because EXP is based on maxhealth*5
    assert(char2.Level==4)
    assert(char2.Experience==0)//leftover xp
    assert(char2.Attack==90)
  }*/
  test("warrior rage"){
    val war1:Character=new Warrior
    assert(war1.MaxHP==200)
    assert(war1.Attack==30)
    assert(war1.skill1Flag==false)
    war1.EXPgain(100000)

    assert(war1.MaxHP==1200)
    assert(war1.Level==100)
    assert(war1.Attack==1030)
    assert(war1.MAtt==0)
    assert(war1.skill1Flag==true)
  }
  test("priest"){
    val priest1:Character=new Priest
    assert(priest1.skill1Flag==false)
    assert(priest1.Attack==10)
    assert(priest1.MaxMP==100)
    priest1.EXPgain(12000)

    assert(priest1.Level==12)
    assert(priest1.MaxMP==340)
    assert(priest1.Attack==130)
    assert(priest1.skill1Flag==true)
  }
}

package tests
import org.scalatest._
import Sheet.Character

class TestMagicalAttack extends FunSuite{
  test("MaGik"){
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
  }
}

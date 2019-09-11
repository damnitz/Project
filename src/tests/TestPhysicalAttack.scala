package tests
import org.scalatest._
import Sheet.Character

class TestPhysicalAttack extends FunSuite{
  test("DMG"){
    val char1:Character=new Character
    val char2:Character=new Character
    char1.Attack=70
    char2.Defense=60
    char1.PhysicalAttack(char2)
    assert(char2.CurrentHP==90)
  }
}

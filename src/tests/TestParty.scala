package tests
import org.scalatest._
import Sheet.Character
import Sheet.Party

class TestParty extends FunSuite{
  test("party!"){
    val char1:Character=new Character
    val char2:Character=new Character
    val char3:Character=new Character
    val char4:Character=new Character
    val char5:Character=new Character
    val char6:Character=new Character
    val char7:Character=new Character
    val char8:Character=new Character
    val party1:List[Character]=List(char1,char2,char3,char4)
    val party2:List[Character]=List(char5,char6,char7,char8)
    char5.Experience=10
    char6.Experience=20
    char7.Experience=20
    char8.Experience=10
    char4.Dead=true
    val WinningParty:Party=new Party(party1)
    val LosingParty:Party=new Party(party2)
    WinningParty.DefeatParty(LosingParty)
    assert(char1.Experience==20)
  }
}

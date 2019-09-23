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
    val party2:List[Character]=List(char5,char6,char7,char8)//each char drops 500XP each,2000 total
    char3.Dead=true
    char4.Dead=true//so the remaining two chars on party1 get 1000XP each
    val WinningParty:Party=new Party(party1)
    val LosingParty:Party=new Party(party2)
    WinningParty.DefeatParty(LosingParty)
    assert(char1.Level==1)//every 1000XP=LevelUP
    assert(char2.Level==1)

    assert(char1.Attack==60)//every Level you gain 10 stats, default Attack is 50.
    assert(char2.Attack==60)

    assert(char1.Experience==0)//no leftover XP
    assert(char1.Experience==0)

    assert(char3.Level==0)//dead
    assert(char4.Level==0)//dead
    assert(char3.Experience==0)//dead
    assert(char4.Experience==0)//dead
  }
  test("party2!"){
    val char1:Character=new Character
    val char2:Character=new Character
    val char3:Character=new Character
    val char4:Character=new Character
    val char5:Character=new Character
    char5.EXPgain(15000)//now char5 will have 250HP and will drop 1250XP as a result
    val char6:Character=new Character
    val char7:Character=new Character
    val char8:Character=new Character
    val party1:List[Character]=List(char1,char2,char3,char4)
    val party2:List[Character]=List(char5,char6,char7,char8)//each char drops 500XP each with the exception of char5 who drops 1250XP,2750 total
    char3.Dead=true
    char4.Dead=true//so the remaining two chars on party1 get 1375XP each
    val WinningParty:Party=new Party(party1)
    val LosingParty:Party=new Party(party2)
    WinningParty.DefeatParty(LosingParty)
    assert(char1.Level==1)//every 1000XP=LevelUP
    assert(char2.Level==1)
    assert(char1.Experience==375)//leftover XP
    assert(char2.Experience==375)
    assert(char1.Attack==60)//Every level you gain +10 to stats
    assert(char2.Attack==60)
    assert(char3.Level==0)//dead so didnt gain XP
    assert(char4.Level==0)//dead
    assert(char3.Experience==0)//dead
    assert(char4.Experience==0)//dead
  }
}

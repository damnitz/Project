package Sheet

class Party(var PartyChar:List[Character]) {
  //var PartyChar:List[Character]=List()
  def DefeatParty(Defeated:Party): Unit ={
    var totalXP=0
    for(i<-Defeated.PartyChar){
      totalXP+=i.PartyCharacterXP()
    }
    var NumberAliveVictors=0
    for(i<-this.PartyChar){
      if(i.Dead==false){
        NumberAliveVictors+=1
      }
    }
    var XPSplit=totalXP/NumberAliveVictors
    for(i<-this.PartyChar){
      if(i.Dead==false){
        i.EXPgain(XPSplit)
      }
    }
  }
}

package mvc

import javafx.scene.input.KeyEvent
import play.api.libs.json.{JsValue, Json}
import scalafx.application.JFXApp
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.canvas.Canvas
import scalafx.scene.{Group, ParallelCamera, Scene}
import scalafx.scene.control.{Button, Label, TextArea, TextField}
import scalafx.scene.layout.{GridPane, HBox, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Rectangle, Shape}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
//Map JSON format - {“mapSize”:{“width”: 500, “height”: 500}, “tiles”:[[tile]]}

//ListBuffer(
object View extends JFXApp{
  def createMap():String={
    var tile:ListBuffer[Map[String,JsValue]]=ListBuffer()
    for (y<-1 until 200){
      tile+=Map("type"->Json.toJson(""),"passable"->Json.toJson(false))
      for(i<-1 until 10)
      tile+=Map("type"->Json.toJson(""),"passable"->Json.toJson(true))
      tile+=Map("type"->Json.toJson(""),"passable"->Json.toJson(true))
      tile+=Map("type"->Json.toJson(""),"passable"->Json.toJson(true))
      tile+=Map("type"->Json.toJson(""),"passable"->Json.toJson(true))
      //tile+=Map("type"->Json.toJson(""),"passable"->Json.toJson(false))
      tile+=Map("type"->Json.toJson(""),"passable"->Json.toJson(true))
      tile+=Map("type"->Json.toJson(""),"passable"->Json.toJson(true))
      tile+=Map("type"->Json.toJson(""),"passable"->Json.toJson(true))
      tile+=Map("type"->Json.toJson(""),"passable"->Json.toJson(true))
    }

    println(tile)

    val jsontiles:JsValue=Json.toJson(tile)
    var dimensions:Map[String,Int]=Map()
    dimensions+=("width"->1000)
    dimensions+=("height"->830)
    val jsondimensions:JsValue=Json.toJson(dimensions)
    var map:Map[String,JsValue]=Map()
    map+=("mapSize"->jsondimensions)
    map+=("tiles"->jsontiles)
    val jsonmap:JsValue=Json.toJson(map)
    val json=Json.stringify(jsonmap)
    json
  }

  for(i<-0 until 10){
    val tru:JsValue=Json.toJson(true)
    //print(tru)
  }
  var graphics: Group = new Group {}
  val allsquares:HBox=new HBox()
  def renderMap(jsonString:String): Unit ={
    val parsed=Json.parse(jsonString)
    val dimensions= (parsed \ "mapSize").as [Map[String,Int]]
    val width:Int=dimensions.getOrElse("width",0)
    val height:Int=dimensions.getOrElse("height",0)
    val tiles = (parsed \ "tiles").as [ListBuffer[Map[String,JsValue]]]
    var counttiles:Int=0
    var increment:Int=0
    for (i<- tiles){
      for (y<-i.get("passable")){
        if(y.toString.toBoolean==true){
            var greensquare= new Rectangle() {
              translateX=0-(4000*increment)
              translateY=0+(40*increment)
              width = 40
              height = 40
              fill = Color.Green
              counttiles += 1


              increment= math.floor(counttiles/100).toInt
            }
            this.allsquares.children.add(greensquare)
        }
        else if (y.toString=="false"){
          var redsquare= new Rectangle(){
            translateX=0-(4000*increment)
            translateY=0+(40*increment)
            width = 40
            height = 40
            counttiles += 1
            increment= math.floor(counttiles/100).toInt
            fill = Color.Red
          }
          this.allsquares.children.add(redsquare)
        }
      }
    }
  }
  renderMap(createMap())
  val player= new Circle(){
    translateX= 250
    translateY= 250
    radius= 10
    fill=Color.Pink
  }
  var playermovedX=250
  var playermovedY=250
//Each party is in the format - {“location”:{“x”:4.36, “y”: 107.85}, “level”:5, “inBattle”: false}
  def createparty(): String ={
    var location:Map[String,Double]=Map()
    location+=("x"->250)
    location+=("y"->250)
    var otherpartieslocation1:Map[String,Double]=Map()
    otherpartieslocation1 += ("x"->200)
    otherpartieslocation1 += ("y"->200)
    var otherpartieslocation2:Map[String,Double]=Map()
    otherpartieslocation2 += ("x"->300.5)
    otherpartieslocation2 += ("y"->300)
    val jsonloc1 = Json.toJson(otherpartieslocation1)
    val jsonloc2 = Json.toJson(otherpartieslocation2)
    val jsonloc:JsValue=Json.toJson(location)
    var level:Int=5
    val jsonlevel:JsValue=Json.toJson(level)
    var inBattle:Boolean=false
    val jsonbattle:JsValue=Json.toJson(inBattle)
    var mappy1:Map[String,JsValue]=Map()
    mappy1 += ("location"->jsonloc1)
    mappy1 += ("level" -> jsonlevel)
    mappy1 += ("inBattle"-> Json.toJson(true))
    var mappy2:Map[String,JsValue]=Map()
    mappy2 += ("location"->jsonloc2)
    mappy2 += ("level" -> Json.toJson(12))
    mappy2 += ("inBattle"-> jsonbattle)
    var otherparties : ListBuffer[Map[String,JsValue]]=ListBuffer()
    otherparties+=mappy1
    otherparties+=mappy2
    val otherpartiesjson = Json.toJson(otherparties)
    var mappy:Map[String,JsValue]=Map()
    mappy+=("location"->jsonloc)
    mappy+=("level"->jsonlevel)
    mappy+=("inBattle"->jsonbattle)
    val jsonmap:JsValue=Json.toJson(mappy)
    var allparties:Map[String,JsValue]=Map()
    allparties+=("playerParty"->jsonmap)
    allparties+=("otherParties"->otherpartiesjson)
    val partyjson=Json.toJson(allparties)
    val retval=Json.stringify(partyjson)
    retval

  }


  var allplayers:Group=new Group()
  var playergrp:Group=new Group()
  val parsed=Json.parse(createparty())

  /*if (location1.x < location2.x + dimension2.x && location2.x < location1.x + dimension1.x) { //start off seeing if any of the x values overlap
    if (location1.y < location2.y + dimension2.y && location2.y < location1.y + dimension2.y)*/

  def update: Long => Unit = (time:Long)=>{
    val parsed=this.parsed
    val playerParty= (parsed \ "playerParty").as [Map[String,JsValue]]
    val otherparties=(parsed \ "otherParties").as [ListBuffer[Map[String,JsValue]]]

    var locationx:Double = playermovedX
    var locationy:Double = playermovedY

    var playerlocationmap:Map[String,Double]=Map()
    playerlocationmap += ("x"->locationx)
    playerlocationmap += ("y"->locationy)
    val playerlocationjson = Json.toJson(playerlocationmap)
    var level = playerParty("level")
    var inbootle = playerParty("inBattle")
    var playerpartymap : Map[String,JsValue]=Map()
    playerpartymap+=("location"->playerlocationjson)
    playerpartymap+=("level"->level)
    playerpartymap+=("inBattle"->inbootle)
    val playerpartyjson = Json.toJson(playerpartymap)
    var allparties:Map[String,JsValue]=Map()
    allparties+=("playerParty"->playerpartyjson)
    allparties+=("otherParties"->Json.toJson(otherparties))
    val allpartiesjson=Json.toJson(allparties)
    val stringy = Json.stringify(allpartiesjson)

    for (i<-otherparties){
      val renderparty:Circle=new Circle(){
        translateX=i("location")("x").toString.toDouble
        translateY=i("location")("y").toString.toDouble
        centerX=i("location")("x").toString.toDouble
        centerY=i("location")("y").toString.toDouble
        radius=10
        if (i("inBattle").toString.toBoolean==true){fill=Color.Blue
          stroke=Color.Orange}
        else{fill=Color.Black}
      }
      var stackpane:StackPane=new StackPane()
      var stackpane2:StackPane=new StackPane()

      var label:Label=new Label(){
        text="Level: "+i("level").toString + " InBattle: "+i("inBattle").toString
        translateX=i("location")("x").toString.toDouble
        translateY=i("location")("y").toString.toDouble-12
      }

      label.setTextFill(Color.web("#FFFFFF"))
      stackpane.children.addAll(renderparty,label)
      allplayers.children.add(stackpane)
    }
    var playerstackpane:StackPane=new StackPane()
    var playerlabel:Label=new Label(){
      text="P"
      translateX=250
      translateY=250
    }
    playerstackpane.children.addAll(this.player,playerlabel)
    playergrp.children.add(playerstackpane)
    println(stringy)
    /*allparties+=("playerParty"->playerpartyjson)
    allparties+=("otherParties"->Json.toJson(otherparties))
    val allpartiesjson=Json.toJson(allparties)
    val stringy = Json.stringify(allpartiesjson)*/
  }
  AnimationTimer(View.update).start()
  var stackpane:StackPane=new StackPane()
  var label:Label=new Label("P")

  //stackpane.children.addAll(player,label)
  graphics.children.addAll(stackpane,allsquares,allplayers)
  var scenewidth=500
  var sceneheight=500
  this.stage=new PrimaryStage{
    title= "Map"
    scene = new Scene(scenewidth,sceneheight){
      content=List(graphics,playergrp)
      addEventHandler(KeyEvent.KEY_PRESSED, new PlayerMove(graphics))
    }
  }
}


/*if ((playermovedX-4)<i("location")("x").toString.toDouble+4 && (playermovedX+4)<i("location")("x").toString.toDouble-4
        && i("location")("x").toString.toDouble+4 < (playermovedX-4) && i("location")("x").toString.toDouble-4<(playermovedX+4)){
        if((playermovedY-4)<i("location")("y").toString.toDouble+4 && (playermovedY+4)<i("location")("y").toString.toDouble-4
          && i("location")("y").toString.toDouble+4 < (playermovedY-4) && i("location")("y").toString.toDouble-4<(playermovedY+4)){
          collisions+=("inBattle"->true)
          println(i+("inBattle"->Json.toJson(true)))

        }
      }
      else{
        collisions+=("inBattle"->false)
        println(i+("inBattle"->Json.toJson(true)))
      }*/
package mvc



import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.input.{KeyCode, KeyEvent}
import play.api.libs.json.Json
import scalafx.animation.AnimationTimer
import scalafx.scene.{Group, Scene}
import scalafx.scene.layout.StackPane
import scalafx.scene.shape.Circle

class PlayerMove(background:Group) extends EventHandler[KeyEvent]{
  override def handle(event:KeyEvent): Unit ={
    keyPressed(event.getCode)
  }
  def keyPressed(x:KeyCode): Unit ={
    val name=x.getName
    name match{
      case "W" =>background.translateY.value+=5
        View.playermovedY-=5
      case "A" =>background.translateX.value+=5
        View.playermovedX-=5
      case "S" =>background.translateY.value-=5
        View.playermovedY+=5
      case "D" =>background.translateX.value-=5
        View.playermovedX+=5
    }
    /*if (name=="W"){
      background.translateY.value+=5
      View.playermovedY-=5
    }
    else if (name=="A"){
      background.translateX.value+=5
      View.playermovedX-=5
    }
    else if (name=="S"){
      background.translateY.value-=5
      View.playermovedY+=5
    }
    else if (name=="D"){
      background.translateX.value-=5
      View.playermovedX+=5
    }*/
  }
}

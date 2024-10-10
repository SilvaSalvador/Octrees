import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Parent
import javafx.scene.control.{Button, Label, TextField}


class InitialScreen {

  @FXML
  private var gui:Button = _

  @FXML
  private var tui:Button = _

  @FXML
  private var filename:TextField = _

  @FXML
  private var wid:Label = _

  def guiButton(): Unit = {
    if(filename.getText.isEmpty){
      wid.setText("Error: Please Enter a File!!!")
    }else {
      OtreeIO.writeFile(filename.getText)
      val fxmlLoader = new FXMLLoader(getClass.getResource("Controller.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      gui.getScene().setRoot(mainViewRoot)
    }
    }

  def clickedTui(): Unit = {
  //  tui.getScene.getWindow.hide()
    if(filename.getText.isEmpty){
      wid.setText("Error: Please Enter a File!!!")
    }else {
      OtreeIO.writeFile(filename.getText)
      val fxmlLoader = new FXMLLoader(getClass.getResource("Tui.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      tui.getScene.setRoot(mainViewRoot)
      val str: Array[String] = Array("1")
      UI.main(str)
    }
  }


}

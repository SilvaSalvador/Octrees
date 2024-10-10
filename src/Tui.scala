import javafx.fxml.FXML
import javafx.scene.SubScene

class Tui {


  @FXML
  private var subScene2:SubScene = _

  @FXML
  def initialize(): Unit = {
    InitSubScene.subScene.widthProperty.bind(subScene2.widthProperty)
    InitSubScene.subScene.heightProperty.bind(subScene2.heightProperty)
    subScene2.setRoot(InitSubScene.root)
  }


}

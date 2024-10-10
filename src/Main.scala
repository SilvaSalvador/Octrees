import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene._
import javafx.scene.shape._
import javafx.stage.Stage


class Main extends Application {

  override def start(stage: Stage): Unit = {
    stage.setTitle("New Scene!")
    val fxmlLoader = new FXMLLoader(getClass.getResource("InitialScreen.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    stage.setScene(scene)
    stage.show()

  }
}

object FxApp {

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main], args: _*)
  }
}


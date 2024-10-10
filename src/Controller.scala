import Otree.{getObjectList, greenRemove, sepia}
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.SubScene
import javafx.scene.control.Button
import javafx.scene.shape.{Box, Cylinder}

class Controller {

  @FXML
  private var subScene1:SubScene = _

  @FXML
   private var Scale2X:Button = _

  @FXML
  private var ScaleHalf:Button = _

  @FXML
  private var GreenColor:Button = _

  @FXML
  private var SepiaEfect:Button = _

  @FXML
  private var Save:Button = _

  @FXML
  private var Exit:Button = _


  //method automatically invoked after the @FXML fields have been injected
  @FXML
  def initialize(): Unit = {
    InitSubScene.subScene.widthProperty.bind(subScene1.widthProperty)
    InitSubScene.subScene.heightProperty.bind(subScene1.heightProperty)
    subScene1.setRoot(InitSubScene.root)
  }


  def clickedScale2(): Unit = {
    var file = OtreeIO.readFirstLine("-1")
    val b =  Otree.scaleOctree(2,InitSubScene.obtainOctree())
    InitSubScene.worldRoot.getChildren.removeIf( x => x.isInstanceOf[Box] || x.isInstanceOf[Cylinder])
    Otree.uploadOctree(b,InitSubScene.worldRoot,InitSubScene.camVolume)
    InitSubScene.worldRoot.getChildren.add(InitSubScene.camVolume)
    OtreeIO.saveFile(file,Otree.getfirstBox(b) ::getObjectList(b))
  }

  def clickedScaleHalf(): Unit = {
    var file = OtreeIO.readFirstLine("-1")
    val b =  Otree.scaleOctree(0.5,InitSubScene.obtainOctree())
    InitSubScene.worldRoot.getChildren.removeIf( x => x.isInstanceOf[Box] || x.isInstanceOf[Cylinder])
    Otree.uploadOctree(b,InitSubScene.worldRoot,InitSubScene.camVolume)
    InitSubScene.worldRoot.getChildren.add(InitSubScene.camVolume)
    OtreeIO.saveFile(file,Otree.getfirstBox(b) :: getObjectList(b))
  }

  def clickedGreenColor(): Unit = {
    val newOctreeState=Otree.mapColorEffect(greenRemove,InitSubScene.obtainOctree())
    InitSubScene.worldRoot.getChildren.removeIf( x => x.isInstanceOf[Box] || x.isInstanceOf[Cylinder])
    Otree.uploadOctree(newOctreeState,InitSubScene.worldRoot,InitSubScene.camVolume)
    InitSubScene.worldRoot.getChildren.add(InitSubScene.camVolume)
  }

  def clickedSepiaEffect(): Unit = {
    val newOctreeState=Otree.mapColorEffect(sepia,InitSubScene.obtainOctree())
    InitSubScene.worldRoot.getChildren.removeIf( x => x.isInstanceOf[Box] || x.isInstanceOf[Cylinder])
    Otree.uploadOctree(newOctreeState,InitSubScene.worldRoot,InitSubScene.camVolume)
    InitSubScene.worldRoot.getChildren.add(InitSubScene.camVolume)
  }

  def clickedSaveExit(): Unit = {
    var file =InitSubScene.file
    OtreeIO.saveFile(file,Otree.getfirstBox(InitSubScene.firstOctree) ::getObjectList(InitSubScene.firstOctree))
    Platform.exit()
  }
  def clickedExit(): Unit = {
    Platform.exit()
  }


}

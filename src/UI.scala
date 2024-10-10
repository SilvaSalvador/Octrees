import Otree.{Placement, greenRemove, sepia}
import javafx.scene.shape.{Box, Cylinder}

import scala.annotation.tailrec
object UI extends App {

  val octree =InitSubScene.obtainOctree()
  val finaloct = mainloop(octree)
  InitSubScene.worldRoot.getChildren.removeIf( x => x.isInstanceOf[Box] || x.isInstanceOf[Cylinder])
  Otree.uploadOctree(finaloct,InitSubScene.worldRoot,InitSubScene.camVolume)
  InitSubScene.worldRoot.getChildren.add(InitSubScene.camVolume)

  @tailrec
  def mainloop(octstate:Octree[Placement]):Octree[Placement] = {
    OtreeIO.showOptions()
    val userInput = OtreeIO.getUserInputInt()
    userInput match {
      case Some(userInput) => Some(userInput) match {

        case Some(1) => {
          val newoctstate = Otree.scaleOctree(2, octstate)
          OtreeIO.msgsUc()
          mainloop(newoctstate)
        }
        case Some(2) => {
          val newoctstate = Otree.scaleOctree(0.5, octstate)
          OtreeIO.msgsUc()
          mainloop(newoctstate)
        }
        case Some(3) => {
          val newoctstate = Otree.mapColorEffect(sepia, octstate)
          OtreeIO.msgsUc()
          mainloop(newoctstate)

        }
        case Some(4) => {
          val newoctstate = Otree.mapColorEffect(greenRemove, octstate)
          OtreeIO.msgsUc()
          mainloop(newoctstate)
        }
        case Some(5) =>
          OtreeIO.saveFile(InitSubScene.file, Otree.getfirstBox(octstate) :: Otree.getObjectList(octstate))
          OtreeIO.imgLaunch()
          octstate

        case Some(6) =>
          OtreeIO.imgLaunch()
          octstate

        case Some(_) => OtreeIO.anotherOption()
          mainloop(octstate)
      }
      case None=> OtreeIO.anotherOption()
        mainloop(octstate)
    }
  }
}
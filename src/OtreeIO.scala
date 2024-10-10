import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.{Box, Cylinder, Shape3D}

import java.io._
import scala.io.Source
import scala.io.StdIn.readLine
object OtreeIO {

  def showOptions():Unit={println("Please choose an Option:\n")
    println(" (1)-ScaleOctree factor - 2 \n (2)-ScaleOctree factor - 0.5 \n (3)-mapColourEffect-Sepia \n (4)-mapColourEffect-greenRemove \n (5)Launch Image and Save \n (6) Launch Image Without Exit")}

  def getUserInput(): String = readLine.trim.toUpperCase

  def getUserInputInt():Option[Int]= {
    try {
      Some(scala.io.StdIn.readInt())
    } catch {
      case e: Exception => None
    }
  }
  def anotherOption(): Unit = { println("Invalid option!!\n") }

  def imgLaunch():Unit={println("Image Launched")}

  def msgsUc(): Unit = { println("Method applied") }


  def saveFile(file: String,l:List[Shape3D]) = {

    val pw = new PrintWriter(new File(file))
    pw.write(l.head.asInstanceOf[Box].getWidth.toInt+"-size")
    l.tail.map(x=>pw.write(writeObj(x)))
    pw.close
  }

  def writeObj(o:Shape3D):String={
    o match {
      case cylinder: Cylinder =>
        "\nCylinder " + getColor(cylinder) + " " + o.getTranslateX.toInt + " " + o.getTranslateY.toInt + " " + o.getTranslateZ.toInt + " " + o.getScaleX + " " + o.getScaleY + " " + o.getScaleZ
      case _ => "\nBox" + " " + getColor(o.asInstanceOf[Box]) + " " + o.getTranslateX.toInt + " " + o.getTranslateY.toInt + " " + o.getTranslateZ.toInt + " " + o.getScaleX + " " + o.getScaleY + " " + o.getScaleZ
    }
  }


  def getColor(o:Shape3D):String={
    val r=o.getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor.getRed.toInt*255
    val g=o.getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor.getGreen.toInt*255
    val b=o.getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor.getBlue.toInt*255
    "(" + r + ","+g+","+b+')'
  }

  def readFirstLine(file:String):String={
    val bufferedSource = Source.fromFile(file)
    val line = bufferedSource.getLines.next()
    bufferedSource.close()
    line
  }

  def writeFile(file:String):Unit={
    val pw = new PrintWriter(new File("-1"))
    pw.write(file)
    pw.close()
  }

  //T1
  def readFromFile(file: String) :List[Shape3D]= {
    val bufferedSource = Source.fromFile(file)
    val lines = Source.fromFile(file).getLines().toList   //bufferedSource.getLines.toList
    bufferedSource.close()
    formatObjects(lines)
  }


  def formatObjects(lines:List[String]):List[Shape3D]={
    val firstline=lines(0)
    val parts=firstline.split("-")
    if(parts.size>1){
      Otree.createBox((0,0,0) ,parts(0).toInt)::createObjects(lines.tail,List())
    }
    else Otree.createBox((0,0,0),32)::createObjects(lines,List())
  }

  def  createObjects(l:List[String],f:List[Shape3D]):List[Shape3D]={
    l match{
      case Nil=>f
      case h::t=> createType(aux(h)) :: createObjects(t,f)
    }
  }
  def aux(s: String): List[String] = {
    val t = s.split(" ")
    val z = t(1).split('(')
    val z1 = z(1).split(')')
    val er = z1(0).split(",")
    List.concat(t,er)
  }

  def createType(s : List[String]): Shape3D = {
    s.head match {
      case "Cylinder" => val b = new Cylinder(0.5,1,10)
        if(s.size>5) {
          Otree.scaleAndTranslate(b,s(2).toInt,s(3).toInt,s(4).toInt,s(5).toDouble,s(6).toDouble,s(7).toDouble,s(8).toInt,s(9).toInt,s(10).toInt)
        }
        else {
          Otree.scaleAndTranslate(b,0,0,0,1,1,1,s(2).toInt,s(3).toInt,s(4).toInt)
        }
        b
      case "Box" => val c = new Box (1,1,1)
        if(s.size>5) {
          Otree.scaleAndTranslate(c,s(2).toInt,s(3).toInt,s(4).toInt,s(5).toDouble,s(6).toDouble,s(7).toDouble,s(8).toInt,s(9).toInt,s(10).toInt)
        }
        else {
          Otree.scaleAndTranslate(c,0,0,0,1,1,1,s(2).toInt,s(3).toInt,s(4).toInt)
        }
        c
    }
  }
}

import Otree.Placement
import javafx.scene.{Group, Node}
import javafx.scene.paint.{Color, PhongMaterial}
import javafx.scene.shape._

import scala.annotation.tailrec
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`


sealed trait Octree[+A]

case class OcNode[A](coords: A, up_00: Octree[A], up_01: Octree[A],
                     up_10: Octree[A], up_11: Octree[A],
                     down_00: Octree[A], down_01: Octree[A],
                     down_10: Octree[A], down_11: Octree[A]
                    ) extends Octree[A]

case class OcLeaf[A, B](section: B) extends Octree[A]

case object OcEmpty extends Octree[Nothing]

case class Otree() extends Octree[Placement] {
  def mapColorEffect(func: Color => Color): Octree[Placement] = Otree.mapColorEffect(func, this)

  def scaleOctree(fact: Double): Octree[Placement] = Otree.scaleOctree(fact, this)

  def changeColor(camvolume: Shape3D, wr: Group, l: List[Shape3D]): Unit = Otree.changeColor(camvolume, wr, l)

  def uploadOctree(wr: Group, cv: Shape3D): Any = Otree.uploadOctree(this, wr, cv)

  def constructTree(b: Box, ini: List[Shape3D]): Octree[Placement] = Otree.constructTree(b, ini)

  def getObjectList(): List[Shape3D] = Otree.getObjectList(this)

  def getfirstBox(oct: Octree[Placement]): Shape3D = Otree.getfirstBox(this)

  def scaleAndTranslate(obj: Shape3D, x: Int, y: Int, z: Int, scalex: Double, scaley: Double, scalez: Double, r: Int, g: Int, b: Int): Shape3D = Otree.scaleAndTranslate(obj: Shape3D, x: Int, y: Int, z: Int, scalex: Double, scaley: Double, scalez: Double, r: Int, g: Int, b: Int)
}

object Otree {
  //Auxiliary types
  type Point = (Double, Double, Double)
  type Size = Double
  type Placement = (Point, Size) //1st point: origin, 2nd point: size
  //Shape3D is an abstract class that extends javafx.scene.Node
  //Box and Cylinder are subclasses of Shape3D
  type Section = (Placement, List[Node]) //example: ( ((0.0,0.0,0.0), 2.0), List(new Cylinder(0.5, 1, 10)))

  def getObjectList(oct: Octree[Placement]): List[Shape3D] = {
    oct match {
      case OcEmpty => Nil
      case OcLeaf(section) => section.asInstanceOf[Section]._2.asInstanceOf[List[Shape3D]]
      case OcNode(placement, one, two, three, four, five, six, seven, eight) =>
        getObjectList(one) ::: getObjectList(two) ::: getObjectList(three) ::: getObjectList(four) ::: getObjectList(five) ::: getObjectList(six) ::: getObjectList(seven) ::: getObjectList(eight)
    }
  }

  def constructTree(b: Box, ini: List[Shape3D]): Octree[Placement] = {
    val l = fitsInBox(b, ini)
    val suns: List[Box] = getBoxes(getPlacement(b))
    if (l.isEmpty) {
      OcEmpty
    }
    else if (!somethingFits(suns, l) || containsAll(suns, l)) {
      OcLeaf((getPlacement(b), l))
    }
    else {
      OcNode[Placement](getPlacement(b), constructTree(suns(0), l), constructTree(suns(1), l), constructTree(suns(2), l), constructTree(suns(3), l), constructTree(suns(4), l), constructTree(suns(5), l), constructTree(suns(6), l), constructTree(suns(7), l))
    }
  }

  def somethingFits(l: List[Box], obj: List[Shape3D]): Boolean = {
    generic(fitsInBox, l, obj)
  }

  def containsAll(l: List[Box], obj: List[Shape3D]): Boolean = {
    generic(intersectsNotContain, l, obj)
  }

  @tailrec
  def generic(f: (Box, List[Shape3D]) => List[Shape3D], l: List[Box], obj: List[Shape3D]): Boolean = {
    l match {
      case Nil => false
      case h :: t => if (f(h, obj).nonEmpty) true else generic(f, t, obj)
    }
  }

  def intersectsNotContain(box: Box, shapes: List[Shape3D]): List[Shape3D] = {
    genericAux((b, s) => s.getBoundsInParent.intersects(b.getBoundsInParent) && !b.getBoundsInParent.contains(s.getBoundsInParent), box, shapes)
  }

  def fitsInBox(box: Box, shapes: List[Shape3D]): List[Shape3D] = {
    genericAux((b, s) => b.getBoundsInParent.contains(s.getBoundsInParent), box, shapes)
  }

  def genericAux(f: (Box, Shape3D) => Boolean, box: Box, shapes: List[Shape3D]): List[Shape3D] = {
    shapes match {
      case Nil => Nil
      case head :: tail => if (f(box, head)) head :: genericAux(f, box, tail) else genericAux(f, box, tail)
    }
  }

  def getPlacement(b: Box): Placement = {
    val x = b.getTranslateX - b.getWidth / 2
    val y = b.getTranslateY - b.getHeight / 2
    val z = b.getTranslateZ - b.getDepth / 2
    ((x, y, z), b.getWidth)
  }

  def scaleAndTranslate(obj: Shape3D, x: Int, y: Int, z: Int, scalex: Double, scaley: Double, scalez: Double, r: Int, g: Int, b: Int): Shape3D = {
    obj.setTranslateX(x)
    obj.setTranslateY(y)
    obj.setTranslateZ(z)
    obj.setScaleX(scalex)
    obj.setScaleX(scaley)
    obj.setScaleX(scalez)
    obj.setMaterial(new PhongMaterial(Color.rgb(r, g, b)))
    obj
  }


  def getBoxes(p: Placement): List[Box] = {
    val point = p._1
    val size = p._2
    val x = size / 2
    val p1 = point._1
    val p2 = point._2
    val p3 = point._3
    val redMaterial = new PhongMaterial()
    redMaterial.setDiffuseColor(Color.rgb(150, 0, 0))
    val box1 = new Box(x, x, x) //box 0
    box1.setTranslateX(x / 2 + p1)
    box1.setTranslateY(x / 2 + p2)
    box1.setTranslateZ(x / 2 + p3)
    box1.setMaterial(redMaterial)
    box1.setDrawMode(DrawMode.LINE)

    val box2 = new Box(x, x, x)
    box2.setTranslateX(x / 2 + p1 + x) //box 2
    box2.setTranslateY(x / 2 + p2)
    box2.setTranslateZ(x / 2 + p3)
    box2.setMaterial(redMaterial)
    box2.setDrawMode(DrawMode.LINE)

    val box3 = new Box(x, x, x)
    box3.setTranslateX(x / 2 + p1)
    box3.setTranslateY(x / 2 + p2 + x)
    box3.setTranslateZ(x / 2 + p3)
    box3.setMaterial(redMaterial)
    box3.setDrawMode(DrawMode.LINE)

    val box4 = new Box(x, x, x)
    box4.setTranslateX(x / 2 + p1 + x)
    box4.setTranslateY(x / 2 + p2 + x)
    box4.setTranslateZ(x / 2 + p3)
    box4.setMaterial(redMaterial)
    box4.setDrawMode(DrawMode.LINE)

    val box5 = new Box(x, x, x)
    box5.setTranslateX(x / 2 + p1)
    box5.setTranslateY(x / 2 + p2)
    box5.setTranslateZ(x / 2 + p3 + x)
    box5.setMaterial(redMaterial)
    box5.setDrawMode(DrawMode.LINE)

    val box6 = new Box(x, x, x)
    box6.setTranslateX(x / 2 + p1 + x)
    box6.setTranslateY(x / 2 + p2)
    box6.setTranslateZ(x / 2 + p3 + x)
    box6.setMaterial(redMaterial)
    box6.setDrawMode(DrawMode.LINE)

    val box7 = new Box(x, x, x)
    box7.setTranslateX(x / 2 + p1)
    box7.setTranslateY(x / 2 + p2 + x)
    box7.setTranslateZ(x / 2 + p2 + x)
    box7.setMaterial(redMaterial)
    box7.setDrawMode(DrawMode.LINE)

    val box8 = new Box(x, x, x)
    box8.setTranslateX(x / 2 + p1 + x)
    box8.setTranslateY(x / 2 + p2 + x)
    box8.setTranslateZ(x / 2 + p3 + x)
    box8.setMaterial(redMaterial)
    box8.setDrawMode(DrawMode.LINE)

    box1 :: box2 :: box3 :: box4 :: box5 :: box6 :: box7 :: box8 :: List()
  }


  def uploadOctree(oct: Octree[Placement], wr: Group, cv: Shape3D): Any = {
    oct match {
      case OcEmpty => OcEmpty
      case OcLeaf(section) => val box = createBox(section.asInstanceOf[Section]._1);
        auxColor(cv, box)
        wr.getChildren.add(box)
        section.asInstanceOf[Section]._2.map(x => wr.getChildren.add(x.asInstanceOf[Shape3D]))
      case OcNode(placement, one, two, three, four, five, six, seven, eight) =>
        val b2 = createBox(placement);
        auxColor(cv, b2);
        wr.getChildren.add(b2)
        uploadOctree(one, wr, cv);
        uploadOctree(two, wr, cv);
        uploadOctree(three, wr, cv);
        uploadOctree(four, wr, cv);
        uploadOctree(five, wr, cv);
        uploadOctree(six, wr, cv);
        uploadOctree(seven, wr, cv);
        uploadOctree(eight, wr, cv)
    }
  }

  def createBox(p: Placement): Shape3D = {
    val size = p._2
    val redMaterial = new PhongMaterial()
    redMaterial.setDiffuseColor(Color.rgb(150, 0, 0))
    val b = new Box(size, size, size)
    b.setTranslateX(p._1._1 + size / 2)
    b.setTranslateY(p._1._2 + size / 2)
    b.setTranslateZ(p._1._3 + size / 2)
    b.setMaterial(redMaterial)
    b.setDrawMode(DrawMode.LINE)
    b
  }

  def getfirstBox(oct: Octree[Placement]): Shape3D = {
    oct match {
      case OcEmpty => null
      case OcLeaf(section) => createBox(section.asInstanceOf[Section]._1)
      case OcNode(placement, one, two, three, four, five, six, seven, eight) => createBox(placement.asInstanceOf[Placement])
    }
  }


  def auxColor(camvolume: Shape3D, obj: Shape3D) = {
    if (!obj.getBoundsInParent.intersects(camvolume.getBoundsInParent)) {
      obj.setMaterial(new PhongMaterial(Color.rgb(150, 0, 0)))
    } else {
      obj.setMaterial(new PhongMaterial(Color.rgb(255, 255, 255)))
    }
  }

  def changeColor(camvolume: Shape3D, wr: Group, l: List[Shape3D]): Unit = {
    wr.getChildren.map(x => if (x.isInstanceOf[Box] && !l.contains(x)) auxColor(camvolume, x.asInstanceOf[Shape3D]))
  }


  def scaleOctree(fact: Double, oct: Octree[Placement]): Octree[Placement] = {
    oct match {
      case OcEmpty => OcEmpty
      case OcLeaf(section) => val s = section.asInstanceOf[Section]
        val sec = (scalePlacement(s._1, fact), s._2.map(x => scale(x.asInstanceOf[Shape3D], fact)))
        OcLeaf(sec)
      case OcNode(q, w, e, r, t, y, u, i, o) =>
        OcNode(scalePlacement(q, fact), scaleOctree(fact, w), scaleOctree(fact, e), scaleOctree(fact, r), scaleOctree(fact, t), scaleOctree(fact, y),
          scaleOctree(fact, u), scaleOctree(fact, i), scaleOctree(fact, o))
    }
  }

  def scalePlacement(placement: Placement, factor: Double): Placement = {
    val (point, size) = placement
    ((point._1 * factor, point._2 * factor, point._3 * factor), size * factor)
  }

  def scale(s: Shape3D, fact: Double): Shape3D = {
    s.setScaleX(fact)
    s.setScaleY(fact)
    s.setScaleZ(fact)
    s.setTranslateX(s.getTranslateX * fact)
    s.setTranslateY(s.getTranslateY * fact)
    s.setTranslateZ(s.getTranslateZ * fact)
    s
  }


  def mapColorEffect(func: Color => Color, oct: Octree[Placement]): Octree[Placement] = {
    oct match {
      case OcEmpty => OcEmpty
      case OcLeaf(section) => OcLeaf(section.asInstanceOf[Section]._1, section.asInstanceOf[Section]._2.map(x => changeColor(func, x.asInstanceOf[Shape3D])))
      case OcNode(placement, one, two, three, four, five, six, seven, eight) => OcNode(placement, mapColorEffect(func, one), mapColorEffect(func, two), mapColorEffect(func, three), mapColorEffect(func, four), mapColorEffect(func, five), mapColorEffect(func, six), mapColorEffect(func, seven), mapColorEffect(func, eight))
    }
  }

  def changeColor(f: Color => Color, s: Shape3D): Shape3D = {
    s.setMaterial(new PhongMaterial(f(s.getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor)))
    s
  }

  def sepia(c: Color): Color = {
    val r = c.getRed
    val g = c.getGreen
    val b = c.getBlue
    val red = (0.40 * r + 0.77 * g + 0.20 * b).min(1.0)
    val green = (0.35 * r + 0.69 * g + 0.17 * b).min(1.0)
    val blue = (0.27 * r + 0.53 * g + 0.13 * b).min(1.0)
    new Color(red, green, blue, c.getOpacity)
  }

  def greenRemove(c: Color): Color = {
    new Color(c.getRed, 0, c.getBlue, c.getOpacity)
  }
}
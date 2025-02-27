package tetris.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import tetris.logic.TetrisLogic._

import scala.collection.mutable

/** To implement Tetris, complete the ``TODOs`` below.
 *
 * If you need additional files,
 * please also put them in the ``tetris`` package.
 */
class TetrisLogic(val randomGen: RandomGenerator,
                  val gridDims : Dimensions,
                  var initialBoard: Seq[Seq[CellType]]) {

  def this(random: RandomGenerator, gridDims : Dimensions) =
    this(random, gridDims, makeEmptyBoard(gridDims))

  def this() =
    this(new ScalaRandomGen(), DefaultDims, makeEmptyBoard(DefaultDims))


  def getStartingOrientation(idx : Int): Array[Point] = {
    idx match {
      case 0 => Array[Point](Point(-1, 0), Point(0, 0), Point(1, 0), Point(2, 0)) //
      case 1 => Array[Point](Point(-1, -1), Point(0, 0), Point(-1, 0), Point(1, 0))
      case 2 => Array[Point](Point(-1, 0), Point(0, 0), Point(1, 0), Point(1, -1))
      case 3 => Array[Point](Point(0,-1), Point(0,0), Point(1,-1), Point(1,0)) //
      case 4 => Array[Point](Point(-1, 0), Point(0, 0), Point(0, -1), Point(1, -1))
      case 5 => Array[Point](Point(-1, 0), Point(0, 0), Point(0, -1), Point(1, 0))
      case 6 => Array[Point](Point(-1, -1), Point(0, 0), Point(0, -1), Point(1, 0))
    }
  }
  def evenOrOddWidth() : Point = {
    if(gridDims.width % 2 == 0) Point(gridDims.width/2 - 1, 1)
    else Point(gridDims.width/2, 1)
  }


  val init = evenOrOddWidth()
  var idx = randomGen.randomInt(7)
  var orientation = getStartingOrientation(idx)
  var currTetronimo = Array[Point](init + orientation(0), init + orientation(1), init + orientation(2), init + orientation(3))
  var pastTetronimos = mutable.HashMap[Int, Array[Array[Point]]]()
  var gameOver = false


  def type_I_Rotations(dir : Int) : Unit = {
    var tempTetronimo = Array[Point]()
    var tempOrientation = Array[Point]()
    for (i <- 0 until 4) {
      if (dir == 0) tempOrientation = tempOrientation :+ Point(-orientation(i).y, orientation(i).x)
      else tempOrientation = tempOrientation :+ Point(orientation(i).y, -orientation(i).x)
    }
    for (i <- 0 until 4) {
      tempTetronimo = tempTetronimo :+ currTetronimo(1)  + tempOrientation(i)
    }
    for (i <- 0 until 4) {
      if (tempTetronimo(i).y >= gridDims.height) return
      else if (tempTetronimo(i).x >= gridDims.width) return
      else if (tempTetronimo(i).x < 0) return
    }
    if (!hitTetronimos(tempTetronimo,1)) {
      currTetronimo = tempTetronimo
      orientation = tempOrientation
    }
  }

  def type_II_Rotations(dir : Int) : Unit = {
    var tempTetronimo = Array[Point]()
    var tempOrientation = Array[Point]()
    for (i <- 0 until 4) {
      if (dir == 0)tempOrientation = tempOrientation :+ Point(-orientation(i).y, orientation(i).x)
      else tempOrientation = tempOrientation :+ Point(orientation(i).y, -orientation(i).x)
    }
    for (i <- 0 until 4) {
      if(dir == 0) {
        if (orientation(0) == Point(-1,0)) tempTetronimo = tempTetronimo :+ (currTetronimo(1)+ tempOrientation(i)) + Point(1,0)
        else if (orientation(0) == Point(0,-1)) tempTetronimo = tempTetronimo :+ (currTetronimo(1)+ tempOrientation(i)) + Point(0,1)
        else if(orientation(0) == Point(1,0)) tempTetronimo = tempTetronimo :+ (currTetronimo(1)+ tempOrientation(i)) + Point(-1,0)
        else  tempTetronimo = tempTetronimo :+ (currTetronimo(1)+ tempOrientation(i)) + Point(0,-1)
      }
      else{
        if (orientation(0) == Point(-1, 0)) tempTetronimo = tempTetronimo :+ (currTetronimo(1) + tempOrientation(i)) + Point(0, 1)
        else if (orientation(0) == Point(0, -1)) tempTetronimo = tempTetronimo :+ (currTetronimo(1) + tempOrientation(i)) + Point(1, 0)
        else if (orientation(0) == Point(1, 0)) tempTetronimo = tempTetronimo :+ (currTetronimo(1) + tempOrientation(i)) + Point(0, -1)
        else tempTetronimo = tempTetronimo :+ (currTetronimo(1) + tempOrientation(i)) + Point(-1, 0)
      }
    }
    if (!hitTetronimos(tempTetronimo,1)) {
      currTetronimo = tempTetronimo
      orientation = tempOrientation
    }
  }


  // TODO implement me
  def rotateLeft(): Unit = {
    if (List(1, 2, 4, 5, 6).contains(idx)) type_I_Rotations(1)
    else if(idx == 0) type_II_Rotations(1)
  }

  // TODO implement me
  def rotateRight(): Unit = {
    if(List(1,2,4,5,6).contains(idx))type_I_Rotations(0)
    else if(idx == 0) type_II_Rotations(0)
  }

  def removeLines() : Unit = {
    var removed = 0
    for (i <- 0 until gridDims.height) {
      val height = (gridDims.height - 1) - i + removed
      var count = 0
      for ((k, v) <- pastTetronimos) {
        for (j <- 0 until v.length) {
          for(z <- 0 until  v(j).length){
            if (v(j)(z).y == height) count += 1
          }
        }
      }

      for(j <- 0 until gridDims.width){
        if(initialBoard(height)(j) != Empty) count +=1
      }

      if(count == gridDims.width) {
        for ((k, v) <- pastTetronimos) {
          for (j <- 0 until v.length) {
            for (z <- 0 until v(j).length) {
              if (v(j)(z).y == height) v(j)(z) = Point(-1, -1)
            }
          }
        }

        for ((k, v) <- pastTetronimos) {
          for (j <- 0 until v.length) {
            for (z <- 0 until v(j).length) {
              if (v(j)(z).y < height && v(j)(z).y != -1) v(j)(z) = Point(v(j)(z).x, v(j)(z).y + 1)
            }
          }
        }

        for (j <- 0 until height) {
          for (z <- 0 until gridDims.width) {
            initialBoard = initialBoard.updated(height - j,initialBoard(height - j).updated(z, initialBoard(height - j - 1)(z)))
          }
        }

        removed += 1
      }
    }
  }

  def newTetronimo() : Unit = {
    if (pastTetronimos.contains(idx)) {
      pastTetronimos(idx) = pastTetronimos(idx) :+ currTetronimo
    }
    else pastTetronimos += (idx -> Array[Array[Point]](currTetronimo))

    removeLines()
    idx = randomGen.randomInt(7)
    orientation = getStartingOrientation(idx)
    currTetronimo = Array[Point](init + orientation(0), init + orientation(1), init + orientation(2), init + orientation(3))

    for (i <- 0 until 4){
      for ((k, v) <- pastTetronimos) {
        for (j <- 0 until v.length) {
          if (v(j).contains(currTetronimo(i))) gameOver = true
        }
      }
    }
  }

  def hitTetronimos(tempTetronimo: Array[Point], rtFlag : Int): Boolean = {
    if(rtFlag == 1) {
      for(i <- 0 until 4){
        if (initialBoard(tempTetronimo(i).y)(tempTetronimo(i).x) != Empty) return true
      }
    }

    for (i <- 0 until 4) {
      for ((k, v) <- pastTetronimos) {
        for (j <- 0 until v.length) {
          if (v(j).contains(tempTetronimo(i))) return true
        }
      }
    }
    false
  }

  def onWall(dir: Int): Boolean = {
    for (i <- 0 until 4) {
      if (dir == 0 && currTetronimo(i).y + 1 == gridDims.height) return true
      else if (dir == 1 && currTetronimo(i).x + 1 == gridDims.width) return true
      else if (dir == 2 && currTetronimo(i).x - 1 < 0) return true
    }

    for (i <- 0 until 4) {
      if (dir == 0 && initialBoard(currTetronimo(i).y + 1)(currTetronimo(i).x) != Empty) return true
      else if (dir == 1 && initialBoard(currTetronimo(i).y)(currTetronimo(i).x + 1) != Empty) return true
      else if (dir == 2 && initialBoard(currTetronimo(i).y)(currTetronimo(i).x - 1) != Empty) return true
    }
    false
  }

  def nextPosition(dir: Int): Array[Point] = {
    if (dir == 0 /* down */ ) {
      return Array[Point](currTetronimo(0) + Point(0, 1), currTetronimo(1) + Point(0, 1),
        currTetronimo(2) + Point(0, 1), currTetronimo(3) + Point(0, 1))
    }
    else if (dir == 1 /* right */ ) {
      return Array[Point](currTetronimo(0) + Point(1, 0), currTetronimo(1) + Point(1, 0),
        currTetronimo(2) + Point(1, 0), currTetronimo(3) + Point(1, 0))
    }
    else { /* left */
      return Array[Point](currTetronimo(0) + Point(-1, 0), currTetronimo(1) + Point(-1, 0),
        currTetronimo(2) + Point(-1, 0), currTetronimo(3) + Point(-1, 0))
    }
  }


  // TODO implement me
  def moveLeft(): Unit = {
    if(onWall(2)) return
    else if(hitTetronimos(nextPosition(2),0)) newTetronimo()
    else currTetronimo = nextPosition(2)
  }

  // TODO implement me
  def moveRight(): Unit = {
    if (onWall(1)) return
    else if (hitTetronimos(nextPosition(1),0)) newTetronimo()
    else currTetronimo = nextPosition(1)
  }

  // TODO implement me
  def moveDown(): Unit = {
    if(!onWall(0) && !hitTetronimos(nextPosition(0),0)) {
      currTetronimo = nextPosition(0)
    }
    else newTetronimo()
  }

  // TODO implement me
  def doHardDrop(): Unit = {
    while(!onWall(0) && !hitTetronimos(nextPosition(0),0)) {
      currTetronimo = nextPosition(0)
    }
    newTetronimo()
  }

  // TODO implement me
  def isGameOver: Boolean = gameOver


  def matchCell(_idx : Int) : CellType = {
    _idx match {
      case 0 => return ICell //
      case 1 => return JCell
      case 2 => return LCell
      case 3 => return OCell //
      case 4 => return SCell
      case 5 => return TCell
      case 6 => return ZCell
    }
  }

  // TODO implement me
  def getCellType(p : Point): CellType = {
    for (i <- 0 until 4) {
      if (initialBoard(p.y)(p.x) != Empty) return initialBoard(p.y)(p.x)
    }

    if(currTetronimo.contains(p))return matchCell(idx)
    else if(pastTetronimos.size > 0) {
       for((k,v) <- pastTetronimos){
         for(i <- 0 until v.length){
           if(v(i).contains(p)) return matchCell(k)
         }
       }
    }
    Empty
  }
}

object TetrisLogic {

  val FramesPerSecond: Int = 5 // change this to speed up or slow down the game

  val DrawSizeFactor = 1.0 // increase this to make the game bigger (for high-res screens)
  // or decrease to make game smaller



  def makeEmptyBoard(gridDims : Dimensions): Seq[Seq[CellType]] = {
    val emptyLine = Seq.fill(gridDims.width)(Empty)
    Seq.fill(gridDims.height)(emptyLine)
  }


  // These are the dimensions used when playing the game.
  // When testing the game, other dimensions are passed to
  // the constructor of GameLogic.
  //
  // DO NOT USE the variable DefaultGridDims in your code!
  //
  // Doing so will cause tests which have different dimensions to FAIL!
  //
  // In your code only use gridDims.width and gridDims.height
  // do NOT use DefaultDims.width and DefaultDims.height


  val DefaultWidth: Int = 10
  val NrTopInvisibleLines: Int = 4
  val DefaultVisibleHeight: Int = 20
  val DefaultHeight: Int = DefaultVisibleHeight + NrTopInvisibleLines
  val DefaultDims : Dimensions = Dimensions(width = DefaultWidth, height = DefaultHeight)


  def apply() = new TetrisLogic(new ScalaRandomGen(),
    DefaultDims,
    makeEmptyBoard(DefaultDims))

}
package tetris.logic

// you can alter this file!

case class Point(x : Int, y : Int) {
  def + (p: Point): Point = {
    Point(x + p.x, y + p.y);
  }
}

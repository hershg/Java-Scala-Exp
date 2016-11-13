package Week1

import scala.annotation.tailrec

/**
  * Created by HershYesh on 6/15/16.
  */
class FactorialTailRec {
  def factorial(x: Int): Int = {
    @tailrec
    def loop(acc: Int, x: Int): Int = {
      if (x == 0) acc
      else loop(acc*x, x-1)
    }
    loop(1, x)
  }
}

package Week1

/**
  * Created by HershYesh on 6/11/16.
  */
class NewtonSqrt {
  def abs(x: Double) =
    if (x >= 0) x
    else -x

  def sqrt(x: Double) = {
    def NewtSqrt(guess: Double): Double = {
      if (closeEnough(guess)) guess
      else NewtSqrt(nextGuess(guess))
    }

    def closeEnough(guess: Double) = {
      abs(guess * guess - x) / x < 0.001
    }
    def nextGuess(guess: Double) = {
      (x / guess + guess) / 2
    }

    NewtSqrt(1.0)
  }
}
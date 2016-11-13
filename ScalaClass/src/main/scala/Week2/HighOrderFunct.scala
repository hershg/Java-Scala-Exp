package Week2

import scala.annotation.tailrec

class HighOrderFunct {
  //Take sum of operations of function f on numbers from x to y
  //By passing in function f as parameter, simplifies later definitions of functions like sumInts, sumSquares, ...
  def functSum(f: Int => Int, x: Int, y: Int): Int = {
    if (x > y) 0
    else f(x) + functSum(f, x + 1, y)
  }

  //Tail recursive version of functSum
  def tailRecFunctSum(f: Int => Int, a: Int, b: Int): Int = {
    @tailrec
    def loop(a: Int, acc: Int): Int = {
      if (a > b) acc
      else loop(a + 1, acc + f(a))
    }
    loop(a, 0)
  }


  def sumInts(x: Int, y: Int) = functSum(id, x, y)
  def sumSquares(x: Int, y: Int) = functSum(square, x, y)
  def sumFactorials(x: Int, y: Int) = functSum(factorial, x, y)

  def id(x: Int) = x
  def square(x: Int) = x * x
  def factorial(x: Int): Int = if (x == 0) 1 else x * factorial(x - 1)






  //Can further simplify with anonymous functions:
  //Instead of passing function to use as a parameter, defining it in the parameter space (called anonymous definition)
  def anonSum(x: Int, y: Int) = functSum(x => x, x, y)
  def anonSumSquares(x: Int, y: Int) = functSum(x => x*x*x, x, y)






  //Further simplify: x and y unchanged parameters passed from summing functions to functSum
  //Can remove this by replacing functSum with a function that itself returns a function
  //sumReturnFunct takes as input the function to apply to numbers x to y (basically just the first parameter of functSum)
  // it returns a function that itself takes two ints as input and then returns an int as output
  def sumReturnFunct(f: Int => Int): (Int, Int) => Int = {
    def subF(x: Int, y: Int): Int = {
      if(x > y) 0
      else f(x) + subF(x+1, y)
    }
    subF
  }

  //Here are simplified definitions and uses for new sumReturnFunct. Actual summing function passed as input to sumReturnFunct
  //When simplifiedSumInts/other such functions called, the two parameters for (x,y) given here
  //This provides values for x,y to the subF of sumReturnFunct; now function runs as normal and returns a final value
  def simplifiedSumInts = sumReturnFunct(x => x)
  def simplifiedSumSquares = sumReturnFunct(x => x*x)
  def simplifiedSumFacts = sumReturnFunct(factorial)

  //simplifiedSumInts(1,10)
  //simplifiedSumSquares(1,4)
}

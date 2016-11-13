def sumReturnFunct(f: Int => Int): (Int, Int) => Int = {
  def subF(x: Int, y: Int): Int = {
    if(x > y) 0
    else f(x) + subF(x+1, y)
  }
  subF
}

def simplifiedSumInts = sumReturnFunct(x => x)
def simplifiedSumCubes = sumReturnFunct(x => x*x*x)

simplifiedSumInts(1,10)
simplifiedSumCubes(1,4)

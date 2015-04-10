import scalaz._
import Scalaz._

/**
 * As with applicative, you need to provide only a minimal definition and you get LOTS
 * of methods for free. Check out Scalaz's github page.
 */

object MonadExample {
  implicit def myOptionMonad: Monad[MyOption] = new Monad[MyOption] {
    def bind[A, B](fa: MyOption[A])(f: A => MyOption[B]): MyOption[B] = fa match {
      case MySome(a) => f(a)  // notice how we just take the value out of the monad and apply our function
      case MyNone    => MyNone
    }

    def point[A](a: => A): MyOption[A] = MySome(a)
  }

  def runExample() {
    println("\nMonad\n=====")

    val testOption1: MyOption[Int] = 1.point[MyOption]
    val testOption2: MyOption[Int] = MyNone

    def f = (x: Int) => MySome(x)
    def g = (_: Int) => MyNone

    println("Function returning Some, value is Some:")
    println(testOption1 >>= f)
    println("Function returning None, value is Some:")
    println(testOption1 >>= g)
    println("Function returning Some, value is None:")
    println(testOption2 >>= f)
    println("Function returning None, value is None:")
    println(testOption2 >>= g)
  }
}

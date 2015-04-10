import scalaz._
import Scalaz._


/**
 * Creating an Applicative instance. Note that you only have to define a minimal
 * amount of methods and you get lots of convenient functions for free (this is
 * true for all the typeclasses in Scalaz)
 *
 * Note: ap is actually a member of the Apply typeclass, but because every
 * Applicative is an Apply as well, you can define it here (and it looks much better).
 *
 * Note 2: with typeclasses like defined in Scalaz there's sometimes more than one way
 * to make an object a member of a given typeclass (that's because some typeclasses
 * require the type to be a member of a different typeclass and some methods can be
 * implemented in terms of other methods (eq. Applicative and Monoidal, see:
 * https://wiki.haskell.org/Typeclassopedia#Alternative_formulation)
 */
object ApplicativeExample {
  // we already have the functor instance for MyOption (@FunctorExample.scala)
  // this is important since every applicative is a Functor as well
  // (hence the full name in some books is actually: Applicative Functor)

  implicit def myOptionApplicative: Applicative[MyOption] = new Applicative[MyOption] {
    // the idea here is that we want to get a non-None result iff both the function-holding
    // option is non-None (i.e. Some(function)) and the value itself is a Some
    def ap[A, B](fa: => MyOption[A])(f: => MyOption[A => B]): MyOption[B] = f match {
      case MySome(func) => fa match {
        case MySome(x) => MySome(func(x))
        case MyNone    => MyNone
      }
      case MyNone       => MyNone
    }

    // here we just put a value in a default applicative context.
    // This is of course MySome -- the value is obviously there :)
    def point[A](a: => A): MyOption[A] = MySome(a)
  }

  def runExample() {
    println("\nApplicative\n===========")

    val testOption1: MyOption[Int] = 1.point[MyOption]
    val testOption2: MyOption[Int] = 2.pure[MyOption]                    // pure is the same as point (and that's the name in Haskell)
    val testOption3: MyOption[List[Int]] = List(1, 2, 3).point[MyOption] // point works for arbitrary types, not just Ints :)
    val testOption4: MyOption[Int] = MyNone

    def f = (x: Int) => (y: Int) => x + y  // equivalent, but arguably less readable: {(_: Int) + (_: Int)}.curried
    def g: MyOption[Int => Int] = MyNone

    println("Both values are Some:")
    println(testOption1 <*> (testOption2 map f))
    println("Function is None, values are Some:")
    println(testOption1 <*> g)
    println("Function is Some, at least one of the values is None:")
    println(testOption1 <*> (testOption4 map f))
    println(testOption4 <*> (testOption1 map f))

  }
}

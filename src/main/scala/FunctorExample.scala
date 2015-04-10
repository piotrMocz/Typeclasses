import scalaz._
import Scalaz._


object FunctorExample {

  implicit val myOptionFunctor: Functor[MyOption] = new Functor[MyOption] {
    def map[A, B](fa: MyOption[A])(f: A => B): MyOption[B] = fa match {
      case MySome(a) => MySome(f(a))
      case MyNone    => MyNone
    }
  }

  def runExample() {
    println("\nFunctor\n=======")

    // if you left out the types, it wouldn't compile (the implicits for
    // instances will not resolve)
    val testOption1: MyOption[Int] = MySome(1)
    val testOption2: MyOption[List[Int]] = MySome(List(1, 2, 3))
    val testOption3: MyOption[Int] = MyNone

    println("Show a value:")
    println(testOption1.show)
    println("Map over Some:")
    println(testOption1.map(x => x + 5))

    println("Show another Some:")
    println(testOption2.shows)
    println("Map over another Some:")
    println(testOption2.map(x => (x ++ List(4, 5)).shows))

    println("Show None:")
    println(testOption3.shows)
    println("Map over None:")
    println(testOption3.map(x => x + 5))

    // same with built-in types:
    // Some(5).show doesn't compile, even though scalaz defines a Show instance for Option
  }
}

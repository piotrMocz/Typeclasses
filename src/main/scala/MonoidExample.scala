/**
 * This file aims to demonstrate the creation of a typeclass,
 * adding some convenience methods (mentioned below) and making
 * our type an instance of a given typeclass. It borrows heavily
 * from the great "learn scalaz" tutorial.
 * Note, that we don't need to modify Log in any way to make it
 * an instance of Monoid.
 */

case class Log(msgs: List[String], ids: List[Int])

// typeclass declaration:
trait Monoid[A] {
  def mempty: A
  def mappend(a: A, b: A): A
}

// this let's us use a <+> b instead of mappend(a, b):
trait MonoidOps[A] {
  val M: Monoid[A]
  val value: A
  def <+>(a: A): A = M.mappend(value, a)
}


object MonoidExample {

  // boosting monoid instances with the <+> operator:
  implicit def toMonoidOp[A: Monoid](a: A): MonoidOps[A] = new MonoidOps[A] {
    val M = implicitly[Monoid[A]]
    val value = a
  }

  // Monoid instance for our Log type:
  implicit val logMonoid = new Monoid[Log] {
    def mempty = Log(List.empty, List.empty)
    def mappend(a: Log, b: Log): Log = (a, b) match {
      case (Log(msgsA, idsA), Log(msgsB, idsB)) => Log(msgsA ++ msgsB, idsA ++ idsB)
    }
  }

  def runExample() {
    println("\nMonoid\n======")

    val l1 = Log(List("Start", "Hello"), List(0, 1))
    val l2 = Log(List("World", "Morning"), List(2, 3))

    println("Mappend two logs:")
    println(l1 <+> l2)
  }
}

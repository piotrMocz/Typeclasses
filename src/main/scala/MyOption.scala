import scalaz.{Cord, Show}

/**
 * Proof of concept kind of thing:
 * the definition of MyOption is (at the type level at least) the same
 * as Option from Scala stdlib, but a new one is created to make sure that
 * we aren't relying on some instance provided by scalaz or some other
 * out-of-the-box magic.
 */
sealed trait MyOption[+T]
case class MySome[+T](x: T) extends MyOption[T]
case object MyNone extends MyOption[Nothing]

object MyOption {

  // the point here: show instances have to be somewhat recursive in nature
  // i.e. when the type is compound, its members should have Show instances as well
  // second, you need to provide instances only for one of the two:
  // shows: String or show: Cord, here we choose show
  implicit def myOptionShow[A: Show]: Show[MyOption[A]] = new Show[MyOption[A]] {
    override def show(mo: MyOption[A]): Cord = mo match {
      case MySome(a) => Cord("MySome(", Show[A].show(a), ")")
      case MyNone    => Cord("MyNone")
    }
  }

}
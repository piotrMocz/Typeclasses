import scalaz._
import Scalaz._

/**
 * A type made to provide a context for the monadic operations
 * rather than serve any real purpose so please forgive any
 * clumsiness in the code.
 */

class IPAddr(val addr: (Int, Int, Int, Int)) {

  def mask(m: IPAddr): IPAddr = IPAddr(addr._1 & m.addr._1,
                                       addr._2 & m.addr._2,
                                       addr._3 & m.addr._3,
                                       addr._4 & m.addr._4)

  override def toString = addr match {
    case (a, b, c, d) => "IPAddr(" + a.toString + "." + b.toString + "." + c.toString + "." + d.toString + ")"
  }
}

object IPAddr {
  def apply(a: Int, b: Int, c: Int, d: Int) = new IPAddr((a, b, c, d))

  def fromString(s: String): Option[IPAddr] = {

    def parseSegment(s: String): Option[Int] = {
      if (s.matches("""\d+""")) {
        val n = s.toInt
        if (n >= 0 && n <= 255) Some(n) else None
      }
      else None
    }

    def segments(s: String): Option[List[Int]] = {
      val addrPat = """\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}""".r
      val matchRes: Option[String] = addrPat.findFirstIn(s)
      // sequence is an interesting function that takes a list of monadic values
      // an converts it to a list inside a monad ([m a] -> m [a]). In our case
      // this means converting List[Option[Int]] into Option[List[Int]] with a property
      // that if any of the values is None, the whole result is None.
      // [IntelliJ thinks there's a type error here -- there isn't :)]
      matchRes.flatMap(m => m.split("""\.""").toList.map(parseSegment).sequence)
    }

    def mkAddr(segs: List[Int]): Option[IPAddr] = {
      if (segs.length == 4) Some(IPAddr(segs(0), segs(1), segs(2), segs(3)))
      else None
    }

    segments(s) flatMap mkAddr

  }
}

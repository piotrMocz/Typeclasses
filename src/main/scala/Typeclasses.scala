import scalaz._
import Scalaz._


object Typeclasses {

  val ips = Map("www.google.com"     -> "216.58.209.68",
                "www.onet.pl"        -> "213.180.141.140",
                "www.scala-lang.org" -> "128.178.154.159",
                "totally-fake.co.uk" -> "432.321.212.333",
                "even-faker.se"      -> "123.123",
                "www.what.pl"        -> "ala ma kota")


  def main(args: Array[String]) {

    /*----------------------------------------------------------------
     * Usage of some monadic properties of Option.
     * Note: flatMap and ">>=" are essentially the same thing
     * They take a value "wrapped" in a monad and a function that takes
     * a "raw" value and injects the result into a monad and produce a
     * result in a monad (m a -> (a -> m b) -> m b)
     *----------------------------------------------------------------*/
    println("(Almost) real life Monad usage:")

    println("Get the right value that parses right:")
    println(ips.get("www.google.com").flatMap(IPAddr.fromString)) // (*)
    println(ips get "www.google.com" >>= IPAddr.fromString) // same as (*)

    println("Get a non-existent value:")
    println(ips get "www.youtube.com" >>= IPAddr.fromString)

    println("Get values that doesn't parse:")
    println(ips get "totally-fake.co.uk" >>= IPAddr.fromString)
    println(ips get "www.what.pl" >>= IPAddr.fromString)

    // this is the same as (*)
    val res = for {
      addrStr <- ips get "www.google.com"
      addr <- IPAddr.fromString(addrStr)
    } yield addr

    /* ----------------------------------------------------------------
     * Usage of the applicative properties of Option.
     * ---------------------------------------------------------------- */

    println("Mapping a function of 2 arguments over an Option yields a function wrapped inside an Option:")
    println(res.map((x: IPAddr) => (y: IPAddr) => x.mask(y)))

    println("Using the \"<*>\" operator we can apply such a function to another functor:")
    println(IPAddr.fromString("255.255.255.0") <*> res.map((x: IPAddr) => (y: IPAddr) => x.mask(y)))

    println("The same can be achieved using ApplicativeBuilder:")
    println((res |@| IPAddr.fromString("255.255.255.0"))(_.mask(_)))


    /*
     * Applicative builder is like applying arguments to a function instead of the other
     * way round. Of course, the arguments are functors.
     */
    def f(x: IPAddr, y: IPAddr, z: IPAddr): IPAddr = x.mask(y).mask(z)

    println("For more arguments as well:")
    println((IPAddr.fromString("192.168.10.1") |@|
      IPAddr.fromString("255.255.255.0") |@|
      IPAddr.fromString("255.255.255.128"))(f))


    println("The examples:")
    FunctorExample.runExample()
    ApplicativeExample.runExample()
    MonadExample.runExample()
    MonoidExample.runExample()
  }
}

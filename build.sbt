name := "Typeclasses"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.0"

scalacOptions += "-feature"
scalacOptions += "-language:implicitConversions"
scalacOptions += "-language:higherKinds"

initialCommands in console := "import scalaz._, Scalaz._"
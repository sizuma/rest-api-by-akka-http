name := "rest-api"

version := "0.1"

scalaVersion := "2.12.5"

val akkaVersion = "2.5.11"

val akkaHttpVersion = "10.1.1"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaVersion

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % akkaVersion

libraryDependencies += "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion

libraryDependencies += "com.typesafe.akka" %% "akka-http" % akkaHttpVersion

libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion

libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test

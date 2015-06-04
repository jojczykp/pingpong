name := "pingpong"

organization := "pl.jojczykp"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.4-M1",
	"com.typesafe.akka" %% "akka-testkit"  % "2.4-M1"% "test",
	"org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

name := "NewTP2-Scala"

version := "0.1"

scalaVersion := "2.11.12"

updateOptions := updateOptions.value.withCachedResolution(true)

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.4"
libraryDependencies += "org.apache.spark" %% "spark-graphx" % "2.4.4"
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "1.0.1"
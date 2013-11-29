name := "scala-gist-reader"

version := "1.0"

libraryDependencies in Global ++= Seq(
"org.specs2" %% "specs2" % "2.2.2" % "test",
"org.scalacheck" %% "scalacheck" % "1.10.1" % "test",
"ch.qos.logback" % "logback-classic" % "1.0.13",
"org.apache.httpcomponents" % "httpclient" % "4.3.1",
"commons-io" % "commons-io" % "2.4")


    
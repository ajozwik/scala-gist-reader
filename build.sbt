organization in Global:= "pl.jozwik"

name := "scala-gist-reader"

version := "0.1"

scalaVersion in Global := "2.11.1"

lazy val gistReader = ProjectName("gistReader").settings(
  libraryDependencies ++= Seq("org.apache.httpcomponents" % "httpclient" % "4.3.3",
    "commons-io" % "commons-io" % "2.4",
                                "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1")
)

lazy val testRunner = {
  ProjectName("testRunner").settings(
    libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "3.3.2.201404171909-r"
  ).dependsOn(gistReader)
}

lazy val web = {
  ProjectName("web").settings(
    libraryDependencies ++= Seq("org.fusesource.jansi" % "jansi" % "1.11")
  ).dependsOn(testRunner).enablePlugins(PlayScala).enablePlugins(SbtWeb)
}



libraryDependencies in Global ++= Seq(
  "org.specs2" %% "specs2" % "2.3.12" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.2")



def ProjectName(name: String): Project = (
  Project(name, file(name))
  )

organization := "pl.jozwik"

name := "scala-gist-reader"

version := "0.1"

lazy val gistReader = ProjectName("gistReader").settings(
  libraryDependencies ++= Seq("org.apache.httpcomponents" % "httpclient" % "4.3.1",
    "commons-io" % "commons-io" % "2.4")
)

lazy val scalania = {
  ProjectName("scalania").dependsOn(gistReader)
}



libraryDependencies in Global ++= Seq(
  "org.specs2" %% "specs2" % "2.2.2" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.1" % "test",
  "ch.qos.logback" % "logback-classic" % "1.0.13")



def ProjectName(name: String): Project = (
  Project(name, file(name))
  )
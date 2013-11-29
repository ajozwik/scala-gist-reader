name := "scala-gist-reader"

organization := "pl.jozwik"

version := "1.0"

libraryDependencies in Global ++= Seq(
"org.specs2" %% "specs2" % "2.2.2" % "test",
"org.scalacheck" %% "scalacheck" % "1.10.1" % "test",
"ch.qos.logback" % "logback-classic" % "1.0.13",
"org.apache.httpcomponents" % "httpclient" % "4.3.1",
"commons-io" % "commons-io" % "2.4")

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>http://github.com/ajozwik/scala-gist-reader/</url>
    <licenses>
      <license>
        <name>The Apache Software License, Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>https://github.com/ajozwik/scala-gist-reader.git</url>
      <connection>scm:git:git@github.com:ajozwik/scala-gist-reader.git</connection>
    </scm>
    <developers>
      <developer>
        <id>ajozwik</id>
        <name>Andrzej Jozwik</name>
        <url>http://stackoverflow.com/users/651140/ajozwik</url>
      </developer>
    </developers>)

    
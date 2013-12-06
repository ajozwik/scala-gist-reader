scala-gist-reader
=================

Read the files from gist - file should have only one method with requested signature.

Usage:

* Download project: git clone https://github.com/ajozwik/scalania-gist-reader.git
* Go to: cd scalania-gist-reader
* Run: sbt  "project web" "run"
* Open browser: http://localhost:9000/
* Fill the fields with values:

        Package name (pl.japila.scalania.s99)

        Object name (S99_P21)

        Method singnature, (Seq[(Any, Int, Seq[Any]) => Seq[Any]])

        Test name (P21Spec)

        Gist numbers comma separated (7680647, 7680700)

* Submit the form

In background the playframework application is run, scalania project is downloaded from github.
From requested gists (7680647, 7680700) methods are extracted and placed to "Object name" object (S99_P21).

As the last task the sbt is run in background in scalania directory with command:

sbt 'testOnly pl.japila.scalania.s99.P21Spec'

Result object and sbt output are displayed on page.


scala-gist-reader
=================

Clone the project from git, read the files from gist and store project location (original file is overridden)
The repository is cloned to tmp dir.

Next the sbt 'testOnly pl.japila.scalania.s99.P21Spec' command is run (in below example)

The main method (example of usage):

        TestRunner.uploadSolutionsAndRunTests(new URL("https://github.com/jaceklaskowski/scalania.git"),"exercises",
                                "pl.japila.scalania.s99", "S99_P21", "Seq[(Any, Int, Seq[Any]) => Seq[Any]]",
                                "P21Spec", Seq(7680647,7680700))


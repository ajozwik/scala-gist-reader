scala-gist-reader
=================

Read the files from gist.

Usage:

        val file:Either[String,(Any,Int)] = GistReader.readFileFromGist(7680909)

        val file:Seq[Either[String,(Any,Int)]] = GistReader.readFilesFromGist(Seq(7680909,7680700),"https://api.github.com/gists/")


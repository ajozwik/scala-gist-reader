package pl.jozwik.scalania

import org.specs2.mutable

class GistToFileTest extends mutable.Specification{

  "Gist to file " should {
    "extract method name " in {
      GistsToFile.extractMethodName("def p21[T](toAdd: T, position: Int, list: Seq[T])") === Some("p21")
      GistsToFile.extractMethodName("def p21(toAdd: T, position: Int, list: Seq[T])") === Some("p21")
      GistsToFile.extractMethodName("def range = (from: Int, to: Int) => ") === Some("range")
      GistsToFile.extractMethodName("def range_7680926: (Int, Int) => Seq[Int] = (from, to)") === Some("range_7680926")
    }

    "wrong file " in {
      GistsToFile.extractMethodName("ala ma kota def") === None
    }
  }
}

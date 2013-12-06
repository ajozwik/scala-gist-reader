package pl.jozwik.runner

import org.specs2.mutable

class TestRunnerTest extends mutable.Specification {
  "TestRunner " should {
    "Extract repository name from url " in {
      val expected = "repository"
      TestRunner.extractName("https://paos/posda/" + expected) === expected
      val expectedPosfixGit = expected + TestRunner.gitPostfix
      TestRunner.extractName("https://paos/posda/" + expectedPosfixGit) === expected
    }
  }

}

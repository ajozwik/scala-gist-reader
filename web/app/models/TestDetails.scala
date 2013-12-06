package models

object TestDetails{
  val url = "url"
  val subProject = "subProject"
  val packageName = "packageName"
  val objectName = "objectName"
  val signature = "signature"
  val testName = "testName"
  val numbers = "numbers"
}

case class TestDetails(url:String,subProject:String,packageName:String, objectName:String, signature:String, testName:String, numbers:String)


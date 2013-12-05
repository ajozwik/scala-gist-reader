package models

object TestDetails{
  val packageName = "packageName"
  val objectName = "objectName"
  val signature = "signature"
  val testName = "testName"
  val numbers = "numbers"
}

case class TestDetails(packageName:String, objectName:String, signature:String, testName:String, numbers:String)


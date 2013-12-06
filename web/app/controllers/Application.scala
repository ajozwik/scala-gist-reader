package controllers

import pl.jozwik.scalania.ScalaniaTest
import play.api.mvc._
import play.api.data.{Forms, Form}
import play.api.data.format.Formats._
import play.api.data.Forms._
import models.TestDetails
import models.TestDetails._
import play.api.data.validation.Constraints
import java.io.ByteArrayOutputStream

object Application extends Controller {

  val DEFAULT = TestDetails("pl.japila.scalania.s99", "S99_P21", "Seq[(Any, Int, Seq[Any]) => Seq[Any]]", "P21Spec", "")

  val delimiter = ","

  val form: Form[TestDetails] = Form(
    mapping(
      packageName -> nonEmptyText,
      objectName -> nonEmptyText,
      signature -> nonEmptyText,
      testName -> nonEmptyText,
      numbers -> Forms.of[String].verifying(Constraints.pattern(
        s"""((\\s*\\d+\\s*)${delimiter})*(\\s*\\d+\\s*)""".r,
        "constraint.numbers",
        "error.numbers"))
    )(TestDetails.apply)(TestDetails.unapply)
  )


  def index = Action {
    val f = form.fill(DEFAULT)
    Ok(views.html.index(f,"", Nil))
  }

  private def toSeq(s: String):Seq[String] = {
    Seq(s)
  }

  def checkSolution = Action {
    request =>
      val newForm = form.bindFromRequest()(request)
      newForm.fold(formWithErrors => {
        BadRequest(views.html.index(formWithErrors,"", Nil))
      }, s => {
        val numbers = s.numbers.trim.split(delimiter).map(el => el.trim.toInt)
        val (result,content) = ScalaniaTest.uploadSolutionsAndRunTests(s.packageName.trim,
          s.objectName.trim,
          s.signature.trim,
          s.testName.trim,
          numbers
        )
        Ok(views.html.index(newForm, content,toHtml(result)))
      })


  }


  private def toHtml(seq:Seq[String]):Seq[String] = {
    seq.map(str =>{
      val outputStream = new ByteArrayOutputStream()
      val html = new org.fusesource.jansi.HtmlAnsiOutputStream(outputStream)
      html.write(str.getBytes)
      outputStream.toString
    })
  }

}
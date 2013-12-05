package controllers

import pl.jozwik.scalania.ScalaniaTest
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.TestDetails
import models.TestDetails._

object Application extends Controller {

  val form:Form[TestDetails] = Form(
    mapping(
      packageName -> text,
      objectName -> text,
      signature -> text,
      testName -> text,
      numbers -> text
    )(TestDetails.apply)(TestDetails.unapply)
  )


  def index = Action {

    Ok(views.html.index(form,Nil))
  }

  def checkSolution = Action {
    request =>
    val newForm = form.bindFromRequest()(request)
      newForm.fold(formWithErrors=>{
      // binding failure, you retrieve the form containing errors:
      BadRequest(views.html.index(formWithErrors,Nil))
    },s=>{
        val numbers = s.numbers.trim.split(",").map(el => el.trim.toInt)
        val result = ScalaniaTest.uploadSolutionsAndRunTests(s.packageName.trim,
          s.objectName.trim,
        s.signature.trim,
        s.testName.trim,
        numbers:_*
        )
        Ok(views.html.index(newForm,result.toList))
    })


  }

}
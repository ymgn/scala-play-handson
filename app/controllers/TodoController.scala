package controllers

import javax.inject._
import play.api.mvc._
import service._

class TodoController @Inject()(mcc: MessagesControllerComponents)
extends MessagesAbstractController(mcc){

  def helloworld() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok("Hello World")
  }

  def list() = Action { implicit request: MessagesRequest[AnyContent] =>
    val items: Seq[Todo] = Seq(Todo("Todo1"), Todo("Todo2"), Todo("Todo3"))
    Ok(views.html.list(items))
  }
}

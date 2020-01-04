package controllers

import javax.inject._
import play.api.mvc._
import service._

class TodoController @Inject()(todoService: TodoService, mcc: MessagesControllerComponents)
extends MessagesAbstractController(mcc){

  def helloworld() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok("Hello World")
  }

  def list() = Action { implicit request: MessagesRequest[AnyContent] =>
    val items: Seq[Todo] = todoService.list()
    Ok(views.html.list(items))
  }
}

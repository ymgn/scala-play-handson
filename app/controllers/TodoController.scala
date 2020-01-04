package controllers

import javax.inject._
import play.api.mvc._

class TodoController @Inject()(mcc: MessagesControllerComponents)
extends MessagesAbstractController(mcc){

  def helloworld() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok("Hello World")
  }

}

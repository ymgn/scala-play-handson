package controllers

import javax.inject._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import service._

// @InjectのDIで注入する
class TodoController @Inject()(todoService: TodoService, mcc: MessagesControllerComponents)
extends MessagesAbstractController(mcc){

  def helloworld() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok("Hello World")
  }

  def list() = Action { implicit request: MessagesRequest[AnyContent] =>
    val items: Seq[Todo] = todoService.list()
    Ok(views.html.list(items))
  }

  // name項目が必須のFormを作成
  val todoForm: Form[String] = Form("name" -> nonEmptyText)
  // todoを新規登録する画面
  def todoNew() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.createForm(todoForm))
  }

  // 新規登録画面から叩かれるtodo追加エンドポイント
  def todoAdd() = Action { implicit  request: MessagesRequest[AnyContent] =>
    val name: String = todoForm.bindFromRequest().get
    println(name)
    Ok("Save")
  }
}

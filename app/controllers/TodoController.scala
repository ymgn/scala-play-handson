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
    todoService.insert(Todo(id = None, name))
    Redirect(routes.TodoController.list())    
  }

  // Todoの編集画面
  def todoEdit(todoId: Long) = Action { implicit request: MessagesRequest[AnyContent] =>
    // todoServiceのfindByIdから取得したデータをtodoFormにわたす
    todoService.findById(todoId).map { todo =>
      Ok(views.html.editForm(todoId, todoForm.fill(todo.name)))
    }.getOrElse(NotFound)
  }

  // 編集画面から叩かれる、todo更新エンドポイント
  def todoUpdate(todoId: Long) = Action { implicit request: MessagesRequest[AnyContent] =>
    val name: String = todoForm.bindFromRequest().get

    todoService.update(todoId, Todo(Some(todoId), name)) // Someを使って明示的に値があると示した状態でOptionalの引数に渡す
    Redirect(routes.TodoController.list())
  }
}

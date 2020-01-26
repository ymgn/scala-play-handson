package service

import javax.inject.Inject

import anorm.SqlParser._
import anorm._
import play.api.db.DBApi

import scala.language.postfixOps

case class Todo(id:Option[Long], name: String)   // caseクラスで作成されたTodoクラス

@javax.inject.Singleton
class TodoService @Inject() (dbapi: DBApi) { // @injectでplayではDIできる

  private val db = dbapi.database("default") // DB名 defaultにつなぐ

  val simple = {  // 関数オブジェクトを作成
    get[Option[Long]]("todo.id") ~
    get[String]("todo.name") map {    // 受け取った引数から"todo.id"と"todo.name"というプロパティを取り出す
      case id~name => Todo(id, name)   // 取り出した中身でTodoインスタンスを作成する
    }
  }

  def list(): Seq[Todo] = {   // Todoクラス用のListに入れて返す
    db.withConnection { implicit connection =>

      SQL(
        """
          |select * from todo
          |""".stripMargin  // stripMarginは複数文字列の時に|の左側までを除外する
      ).as(simple *)  // simple関数に実行結果を渡す？
    }
  }

  def insert(todo: Todo) = {
    db.withConnection { implicit  connection =>
      SQL(
        """
          |insert into todo values ((select next value for todo_seq), {name})
          |""".stripMargin
      ).on(
        "name" -> todo.name
      ).executeUpdate()
    }
  }

  def findById(id: Long): Option[Todo] = {
    db.withConnection { implicit connection =>
      SQL("select * from todo where id = {id}").on("id" -> id).as(simple.singleOpt) // パーサーを渡して、0件か1件かにパースする
    }
  }

  def update(id: Long, todo: Todo) = {
    db.withConnection { implicit connection =>
      SQL(
        """
          |update todo
          |set name = {name}
          |where id = {id}
          |""".stripMargin
      ).on(
        "id" -> id,
        "name" -> todo.name
      ).executeUpdate()
    }
  }

  def delete(id: Long) = {
    db.withConnection { implicit connection =>
      SQL("delete from todo where id = {id}").on("id" -> id).executeUpdate()
    }
  }
}
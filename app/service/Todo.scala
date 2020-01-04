package service

import javax.inject.Inject

import anorm.SqlParser._
import anorm._
import play.api.db.DBApi

import scala.language.postfixOps

case class Todo(name: String)   // caseクラスで作成されたTodoクラス

@javax.inject.Singleton
class TodoService @Inject() (dbapi: DBApi) { // @injectでplayではDIできる
  private val db = dbapi.database("default") // DB名 defaultにつなぐ

  val simple = {  // 関数オブジェクトを作成
    get[String]("todo.name") map {    // 受け取った引数から"todo.name"という名前で取り出す
      case name => Todo(name)   // nameが取り出せた時?にTodoインスタンスを作成する
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

}
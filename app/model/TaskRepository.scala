package model

import anorm.SqlParser.get
import anorm._
import play.api.db.DBApi
import javax.inject.Inject
import java.time.LocalDateTime
import scala.language.postfixOps

case class Task(id: Long, title: String, created_at: LocalDateTime, user_id: Long)
case class NewTask(title: String, user_id: Long)

@javax.inject.Singleton
class TaskRepository @Inject()(dbapi: DBApi)(implicit ec: DatabaseExecutionContext) {

  private val db = dbapi.database("default")

  val rowParser: RowParser[Task] = {
    get[Long]("id") ~
      get[String]("title") ~
      get[LocalDateTime]("created_at") ~
      get[Long]("user_id") map {
      case id ~ title ~ created_at ~ user_id => Task(id, title, created_at, user_id)
    }
  }

  def create(task: NewTask): Option[Long] = db.withConnection { implicit connection =>
    val query =
      SQL"""INSERT INTO tasks (title, created_at, user_id) VALUES
    (${task.title},${LocalDateTime.now()},${task.user_id})"""
    query.executeInsert()
  }

  def getAll(user_id: Long): List[Task] = db.withConnection { implicit connnection =>
    val query = SQL"select * from tasks where user_id = $user_id"
    query.as(rowParser *)
  }

  def getById(id: Long): Option[Task] = db.withConnection { implicit connnection =>
    val query = SQL"select * from tasks where id = $id"
    query.as(rowParser.singleOpt)
  }
}
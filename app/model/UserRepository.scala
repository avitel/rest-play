package model

import anorm.SqlParser.get
import anorm._
import play.api.db.DBApi
import javax.inject.Inject


case class User(id: Long, first_name: String, last_name: String, username: String, password: String)
case class NewUser(first_name: String, last_name: String, username: String, password: String)
case class UsernamePassword(username: String, password: String)

@javax.inject.Singleton
class UserRepository @Inject()(dbapi: DBApi)(implicit ec: DatabaseExecutionContext) {

  private val db = dbapi.database("default")

  val rowParser: RowParser[User] = {
    get[Long]("id") ~
      get[String]("first_name") ~
      get[String]("last_name") ~
      get[String]("username") ~
      get[String]("password") map {
      case id ~ first_name ~ last_name ~ username ~ password => User(id, first_name, last_name, username, password)
    }
  }

  //todo
  def generateToken(username: String, password:String): String = "8vZ2F0ZXdheTo4MDgwI"

  def getById(id: Long): Option[User] = db.withConnection { implicit connnection =>
    val query = SQL"select * from users where id = $id"
    query.as(rowParser.singleOpt)
  }

  def create(user: NewUser): Option[Long] = db.withConnection { implicit connection =>
    val query = SQL"""INSERT INTO users (first_name, last_name, username, password) VALUES
    (${user.first_name},${user.last_name},${user.username},${user.password})"""
    query.executeInsert()
  }
}
package controllers

import model._
import play.api.libs.json._
import play.api.mvc.{ BaseController, ControllerComponents}
import javax.inject._
import scala.concurrent._

@Singleton
class AppController @Inject()(userRepository: UserRepository, taskRepository: TaskRepository, val controllerComponents: ControllerComponents)
                             (implicit ec: ExecutionContext)  extends BaseController {

  implicit val userJson = Json.format[User]
  implicit val newUserJson = Json.format[NewUser]
  implicit val userPasswordJson = Json.format[UsernamePassword]
  implicit val taskJson = Json.format[Task]
  implicit val newTaskJson = Json.format[NewTask]

  // curl -v -d '{ "first_name": "Kirk","last_name": "Brian","username": "brian-k","password": "mypassword1"} ' -H 'Content-Type: application/json' -X POST localhost:9000/user/sign-up
  def createUser() = Action { implicit request =>
    val jsonObject = request.body.asJson
    jsonObject.flatMap(Json.fromJson[NewUser](_).asOpt) match {
      case Some(user) =>
        userRepository.create(user) match {
          case Some(id) =>  Created(Json.obj("id" -> id))
          case None => NotModified
        }
      case None => BadRequest
    }
  }

  // curl -v -d '{ "username": "brian-k","password": "mypassword1"} ' -H 'Content-Type: application/json' -X POST localhost:9000/user/token
  def getToken() = Action { implicit request =>
    val jsonObject = request.body.asJson
    jsonObject.flatMap(Json.fromJson[UsernamePassword](_).asOpt) match {
      case Some(up) =>
        val token = userRepository.generateToken(up.username, up.password)
        Ok(Json.obj("access_token" -> token))
      case None => BadRequest
    }
  }

  // curl localhost:9000/user/profile/1
  def getUserById(userId: Long) = Action {
    userRepository.getById(userId) match {
      case Some(user) => Ok(Json.toJson(user))
      case None => NotFound
    }
  }

  // curl -v -d '{"title": "Task 1", "user_id": 1} ' -H 'Content-Type: application/json' -X POST localhost:9000/tasks
  def createTask() = Action { implicit request =>
    val jsonObject = request.body.asJson
    jsonObject.flatMap(Json.fromJson[NewTask](_).asOpt) match {
      case Some(task) =>
        taskRepository.create(task) match {
          case Some(id) => Created(Json.obj("id" -> id))
          case None => NotModified
        }
      case None => BadRequest
    }
  }

  // curl localhost:9000/user/tasks/1
  def getAllTasks(userId: Long) = Action {
    val tasks = taskRepository.getAll(userId)
    Ok{
    Json.arr(tasks)
    }
  }

  // curl localhost:9000/tasks/1
  def getTaskById(taskId: Long) = Action {
    taskRepository.getById(taskId) match {
      case Some(task) => Ok(Json.toJson(task))
      case None => NotFound
    }
  }
}
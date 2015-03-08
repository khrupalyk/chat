package controllers

import javax.print.attribute.standard.JobOriginatingUserName

import akka.actor.{ActorRef, Props, Actor}
import akka.actor.Actor.Receive
import models.{Message, LeaveFromRoom, JoinToRoom}
import play.api._
import play.api.mvc.WebSocket
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.json.{JsString, JsObject, JsValue, Json}
import play.api.mvc._

/**
 * Created by root on 04.03.15.
 */
object ChatController extends Controller {

  class Channel(out: ActorRef, name: String) extends Actor {

    private val room = Akka.system.actorSelection("/user/chat_room")

    override def preStart(): Unit = {
      room ! JoinToRoom(self)
    }

    override def postStop(): Unit = {
      room ! LeaveFromRoom(self)
    }

    override def receive: Receive = {
      case massege: String =>
        room ! Message(self,
          JsObject(
            Seq(
              "name" -> JsString(name),
              "message" -> JsString(massege)
            )
          ))
      case Message(sender, message) =>
        out ! message
    }
  }

  def connect(userName: String) = WebSocket.acceptWithActor[String, JsValue] {
    request => out => Props(new Channel(out, userName))
  }
}

package models

import akka.actor.{ActorRef, Actor}
import akka.actor.Actor.Receive
import play.api.Logger
import play.api.libs.json.JsValue

import scala.collection.mutable.ListBuffer

/**
 * Created by root on 04.03.15.
 */

case class JoinToRoom(actor: ActorRef)
case class LeaveFromRoom(actor: ActorRef)
case class Message(you: ActorRef, json: JsValue)


class ChatRoom extends Actor {

  val clients = ListBuffer.empty[ActorRef]

  override def receive: Receive = {

    case Message(you, message) =>
      clients filter(_ != you) foreach( _ ! message)

    case JoinToRoom(actor) =>
      clients += actor
      Logger.debug("User connect. Clients size: " + clients.size)

    case LeaveFromRoom(actor) =>
      clients -= actor
      Logger.debug("User disconnect. Clients size: " + clients.size)
  }
}

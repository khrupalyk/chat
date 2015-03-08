import akka.actor._
import models.ChatRoom
import play.api.GlobalSettings
import play.api.Application
import play.libs.Akka

/**
 * Created by root on 04.03.15.
 */
object GlobalSettings extends GlobalSettings{
  override def onStart(app: Application): Unit = {
    Akka.system.actorOf(Props[ChatRoom], "chat_room")
  }
}

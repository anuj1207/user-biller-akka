package actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import models.{Biller, Salary}
import akka.pattern.ask
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._
import akka.util.Timeout

import scala.concurrent.duration._
import scala.collection.mutable.ListBuffer

class SalaryDepositActor(dbRef: ActorRef) extends Actor with ActorLogging{

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: Exception => Restart
    }

  implicit val timeout = Timeout(1000 seconds)
  override def receive = {
    case salary: Salary =>
      dbRef ? salary

    case billerList: ListBuffer[Biller] =>
      billerList foreach { biller =>
        dbRef ! biller
      }
  }

}

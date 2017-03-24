package actors

import akka.actor.{Actor, ActorRef}
import models.{Biller, Salary}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

import scala.collection.mutable.ListBuffer

class SalaryDepositActor(dbRef: ActorRef) extends Actor{

  implicit val timeout = Timeout(1000 seconds)
  override def receive = {
    case salary: Salary =>
      println(s"me providing saalry to ${salary.acc}")
      dbRef ? salary

    case (billerList: ListBuffer[Biller],ref: ActorRef) =>
      println("yaha aaaja aakkkkaaa")
      billerList foreach { biller =>
        println(s"Sending back to db repo $biller")
        ref ! biller
      }
  }

}

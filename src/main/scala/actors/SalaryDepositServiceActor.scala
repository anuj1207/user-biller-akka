package actors

import akka.actor.{Actor, ActorRef, Props}
import models.Salary

class SalaryDepositServiceActor(dbRef: ActorRef) extends Actor{

  override def receive = {
    case _ =>
      val depositRef = context.actorOf(Props(classOf[SalaryDepositActor],dbRef))
      println("I am here")
      for(i <- 1 to 10){
        depositRef ! Salary(i, 500)
      }
  }

}

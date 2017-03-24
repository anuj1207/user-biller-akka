package actors

import akka.actor.{Actor, ActorRef}
import models.{Biller, Customer}

class UserAccountGeneratorActor(ref: ActorRef) extends Actor{

  override def receive = {
    case (customer: Customer, billersList: List[Biller]) =>
      ref ! (customer, billersList)
  }

}

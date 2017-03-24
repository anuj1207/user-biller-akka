package actors

import akka.actor.{Actor, ActorRef}
import akka.dispatch.{BoundedMessageQueueSemantics, RequiresMessageQueue}
import models.{Biller, Customer}

class UserAccountGeneratorActor(ref: ActorRef) extends Actor
  with RequiresMessageQueue[BoundedMessageQueueSemantics]{

  override def receive = {
    case (customer: Customer, billersList: List[Biller]) =>
      ref ! (customer, billersList)
  }

}

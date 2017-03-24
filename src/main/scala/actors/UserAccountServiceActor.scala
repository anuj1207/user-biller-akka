package actors

import akka.actor.{Actor, ActorRef, Props}
import models._

class UserAccountServiceActor(dbRef: ActorRef) extends Actor{

  override def receive = {
    case _ =>
      val generatorRef = context.actorOf(Props(classOf[UserAccountGeneratorActor], dbRef))
      for(i <- 1 to 10){
        val customer = Customer(i, "user"+i, "noida"+i, "username"+i, 0)
        val billerList = List(Biller(Car, "biller"+i, i, "", 10, 0, 0, 100),
          Biller(Electricity, "biller"+i, i, "", 10, 0, 0, 100),
          Biller(Food, "biller"+i, i, "", 10, 0, 0, 100),
          Biller(Internet, "biller"+i, i, "", 10, 0, 0, 100),
          Biller(Phone, "biller"+i, i, "", 10, 0, 0, 100))
        generatorRef ! (customer, billerList)
      }
  }

}

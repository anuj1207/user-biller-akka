package actors

import akka.actor.{Actor, ActorLogging}
import models._

class DatabaseRepoActor extends Actor with ActorLogging{

  override def receive = {

    case (customer: Customer, listOfBiller: List[Biller]) =>
      DBInMemory.addCustomer(customer, listOfBiller)

    case Salary(acc, money) =>
      println(s"providing db repo salary to $acc")
      val customer = DBInMemory.listOfCustomer.filter(_.acc == acc)
      DBInMemory.listOfCustomer --= customer
      DBInMemory.listOfCustomer ++= customer.map{c =>
        c.copy(initialAmount = c.initialAmount + money)
      }
      println(s"updated list::::${DBInMemory.listOfCustomer}")
      val billersAssociated = DBInMemory.listOfBiller.filter(_.acc == acc)
      println(s"Sending to bill pay Billers Associated: customer: ${acc} billers: ${billersAssociated}:::::${(billersAssociated,self)}")
      sender ! (billersAssociated,self)

    case biller: Biller =>
      val customer = DBInMemory.listOfCustomer.filter(_.acc == biller.acc)
      DBInMemory.listOfCustomer --= customer
      DBInMemory.listOfCustomer ++= customer.map{c =>
        println(s"Paying to biller ${biller.category} from ${c.name} amount ${biller.amount}")
        c.copy(initialAmount = c.initialAmount - biller.amount)
      }
      val billers = DBInMemory.listOfBiller.filter(_ == biller)
      DBInMemory.listOfBiller --= billers
      DBInMemory.listOfBiller ++= billers.map{b =>
        println(s"Adding biller ${biller.category} from ${biller.acc} amount ${biller.paidAmount}")
        b.copy(paidAmount = b.paidAmount + biller.amount)
      }

    case Report =>
      DBInMemory.listOfCustomer.foreach{ customer =>
        val billerList = DBInMemory.listOfBiller.filter(_.acc == customer.acc)
        log.info(s"Customer: ${customer.username}; Balance: ${customer.initialAmount}")
        log.info("Billers Associated:=>")
        billerList.foreach{biller =>
          log.info(s"Category: ${biller.category}; BillerName: ${biller.name}; TotalPaid Amount: ${biller.paidAmount}; No. Of Times Paid: ${biller.executedIterations}\n")
        }
      }
  }

}

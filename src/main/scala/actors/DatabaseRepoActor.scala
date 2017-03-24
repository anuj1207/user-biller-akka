package actors

import akka.actor.{Actor, ActorLogging, Props}
import models._

class DatabaseRepoActor extends Actor with ActorLogging{

  override def receive = {

    case (customer: Customer, listOfBiller: List[Biller]) =>
      DBInMemory.addCustomer(customer, listOfBiller)

    case Salary(acc, money) =>
      val customer = DBInMemory.listOfCustomer.filter(_.acc == acc)
      if(customer.nonEmpty){
        DBInMemory.listOfCustomer --= customer
        DBInMemory.listOfCustomer ++= customer.map{c =>
          log.info(s"Crediting salary:$money for ${c.username}")
          c.copy(initialAmount = c.initialAmount + money)
        }
        val billersAssociated = DBInMemory.listOfBiller.filter(_.acc == acc)
        val salaryDepositRef = context.actorOf(Props(classOf[SalaryDepositActor],self))
        salaryDepositRef ! billersAssociated
      }
      else{
        log.error("Customer not found")
        throw new Exception("User Not Found")
      }

    case biller: Biller =>
      val customer = DBInMemory.listOfCustomer.filter(_.acc == biller.acc)
      if(customer.nonEmpty){
        DBInMemory.listOfCustomer --= customer
        DBInMemory.listOfCustomer ++= customer.map{c =>
          c.copy(initialAmount = c.initialAmount - biller.amount)
        }
        val billers = DBInMemory.listOfBiller.filter(_ == biller)
        DBInMemory.listOfBiller --= billers
        DBInMemory.listOfBiller ++= billers.map{b =>
          b.copy(paidAmount = b.paidAmount + biller.amount)
        }
      }
      else{
        log.error(s"Customer not found for biller:${biller.name} ${biller.category}")
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

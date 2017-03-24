import actors.{DatabaseRepoActor, ReportGeneratorActor, SalaryDepositServiceActor, UserAccountServiceActor}
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import models.Report

object Application extends App{

  val conf = ConfigFactory.load()
  val system = ActorSystem("CustomerBiller", conf)
  val dbProp = Props[DatabaseRepoActor]
  val dbRef = system.actorOf(dbProp)

  val reportGeneratorProp = Props(classOf[ReportGeneratorActor], dbRef, system)
  val reportGeneratorRef = system.actorOf(reportGeneratorProp)
  reportGeneratorRef ! Report

  val accServiceProp = Props(classOf[UserAccountServiceActor], dbRef)
  val accServiceRef = system.actorOf(accServiceProp)
  accServiceRef ! "add users"

  val salaryServiceProp = Props(classOf[SalaryDepositServiceActor], dbRef)
  val salaryServiceRef = system.actorOf(salaryServiceProp)
  salaryServiceRef ! "add salary"
  println("executed all")

}

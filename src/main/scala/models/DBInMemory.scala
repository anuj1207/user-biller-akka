package models

import scala.collection.mutable.ListBuffer

object DBInMemory {

  val listOfCustomer: ListBuffer[Customer] = ListBuffer()
  val listOfBiller: ListBuffer[Biller] = ListBuffer()

  def addCustomer(c: Customer,l: List[Biller]) {

    listOfCustomer+=c
    listOfBiller++=l
    println(s"adding ${c.toString} with $listOfBiller")

  }

}

package models

case class Biller(category: Category, name: String, acc: Int, transactionDate: String, amount: Int, paidAmount: Int, totalIterations: Int = 0, executedIterations: Int = 0)

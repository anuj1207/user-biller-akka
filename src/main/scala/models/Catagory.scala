package models

sealed abstract class Category

case object Phone extends Category

case object Electricity extends Category

case object Internet extends Category

case object Food extends Category

case object Car extends Category
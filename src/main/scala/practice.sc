import scala.collection.mutable.ListBuffer

val lb = ListBuffer("s")
val list = List("anuj", "saxena")
lb++=list

case class Salary(acc: Int , value: Int)

val lis = ListBuffer(Salary(1,2), Salary(2,2), Salary(3,2))

val li = lis.filter(_.acc==2)

lis--=li

lis++=li.map(_.copy(value = 10))
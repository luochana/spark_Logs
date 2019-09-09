import java.util.Date

object test {
  def main(args:Array[String]):Unit={
    println(new Date())
    println("select * from SearchCount where SearchCountId like "+"\""+new Date().toString+"%"+"\"")
  }

}

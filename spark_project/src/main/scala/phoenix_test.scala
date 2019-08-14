import org.apache.spark.SparkContext
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.phoenix.spark._
object phoenix_test {
  def main(arges: Array[String]): Unit= {
    val spark = SparkSession.builder().appName("ItemSimilarity").master("local").getOrCreate()
 //   val sqlContext = new SQLContext(sc)


   val df = spark.read
    .format("org.apache.phoenix.spark")
    .option("table", "user")
    .option("zkurl", "localhost:2181")
    .load()

/*    val df = spark.sqlContext.load(
      "org.apache.phoenix.spark",
      Map("table" -> "user", "zkUrl" -> "localhost:2181")
    )*/
    df.show()
  }
}

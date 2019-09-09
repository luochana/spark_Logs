package Streaming

import domain.CheckLog
import org.apache.log4j.{Level, Logger}
import utils.DateUtils
import org.apache.phoenix.spark._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object kafkaConsumer {
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
  def main(args:Array[String]):Unit={
    val spark = SparkSession
      .builder
      .appName("KafkaConsumer")
      .master("local")
      .getOrCreate()

    import spark.implicits._
    val messages = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "192.168.56.101:9092,192.168.56.101:9092,192.168.56.101:9092")
      .option("subscribe", "test")
      .load()


/*    val messages = spark
      .readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load()*/

    val LogInfo=messages
      .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .as[(String, String)]
      .map(_._2)
    //  .as[String]

      .map(lines=>{
      val infos=lines.split("\t")
      val ip=infos(0)
      val time=DateUtils.parseTime(infos(1))
      var courseId="0"
      val url=infos(2)
        .split(" ")(1)
      if(url.startsWith("/class")){
        val courceHTML=url.split("/")(2)
        courseId=courceHTML.substring(0,courceHTML.lastIndexOf("."))
      }
      CheckLog(ip,time,courseId,infos(3),infos(4))
    }).filter((checkLog=>(!(checkLog.courseId).equals("0"))))


    val allLogInfo=messages
      .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .as[(String, String)]
      .map(_._2)
     // .as[String]

      .map(lines=>{
      val infos=lines.split("\t")
      val ip=infos(0)
      val time=DateUtils.parseTime(infos(1))
      var courseId="0"
      val url=infos(2)
      CheckLog(ip,time,courseId,infos(3),infos(4))
    })

   // spark.sparkContext.parallelize(Seq(listLogInfo)).saveToPhoenix("LogInfo",Seq("ip","time","courseId","statusCode","searchUrl"),zkUrl = Some("localhost:2181"))


    val CourseCount= LogInfo.map(x => {
      //将CheckLog格式的数据转为20180724_courseId
      x.time.substring(0, 8) + "_" + x.courseId
    }).groupBy("value").count().toDF(Seq("CourseCountId","CCount"):_*)

   // spark.sparkContext.parallelize(Seq(listCourseCount)).saveToPhoenix("CourseCount", Seq("ID", "COUNT"), zkUrl = Some("localhost:2181"))


    val SearchCount=LogInfo.map(x=>{
      /**
        * https://www.sogou.com/web?query=
        * =>https:/www.sogou.com/web?query=
        */
      val url=x.referes.replaceAll("//","/")
      val splits=url.split("/")
      var search=""
      if(splits.length>=2){
        search=splits(1)
      }
      (x.time.substring(0,8),x.courseId,search)
    }).filter(x=>(!(x._3).equals(""))).map(info=>{
      val day_search=info._1+"_"+info._3
      day_search
    }).groupBy("value").count().toDF(Seq("SearchCountId","SCount"):_*)

   // spark.sparkContext.parallelize(Seq(listSearchCount)).saveToPhoenix("SearchCount", Seq("ID", "COUNT"), zkUrl = Some("localhost:2181"))

    val queryLogInfo=allLogInfo.writeStream
      .queryName("allLogInfo")
      .outputMode("append")
      .format("memory")
      .start()

    val queryCourseCount=CourseCount.writeStream
      .queryName("CourseCount")
      .outputMode("complete")
      .format("memory")
      .start()

    val querySearchCount=SearchCount.writeStream
      .queryName("SearchCount")
      .outputMode("complete")
      .format("memory")
      .start()
    while(true){
      spark.sql("select * from allLogInfo").show()
      spark.sql("select * from CourseCount order by CCount desc limit 20").select(col("CourseCountId"),col("CCount").cast(StringType)).show()
      spark.sql("select * from SearchCount order by SCount desc limit 20").select(col("SearchCountId"),col("SCount").cast(StringType)).show()
      spark.sql("select * from allLogInfo").saveToPhoenix("LogInfo",zkUrl = Some("localhost:2181"))
      spark.sql("select * from CourseCount order by CCount desc limit 20").select(col("CourseCountId"),col("CCount").cast(StringType)).saveToPhoenix("CourseCount",zkUrl = Some("localhost:2181"))
      spark.sql("select * from SearchCount order by SCount desc limit 20").select(col("SearchCountId"),col("SCount").cast(StringType)).saveToPhoenix("SearchCount",zkUrl = Some("localhost:2181"))
      Thread.sleep(20 * 1000)
    }
  }

}

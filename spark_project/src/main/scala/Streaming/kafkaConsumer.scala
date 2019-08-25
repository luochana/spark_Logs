package Streaming

import domain.CheckLog
import utils.DateUtils
import org.apache.phoenix.spark._
import org.apache.spark.sql.SparkSession

object kafkaConsumer {
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

    val LogInfo=messages.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .as[(String, String)].map(_._2).map(lines=>{
      val infos=lines.split("\t")
      val ip=infos(0)
      val time=DateUtils.parseTime(infos(1))
      var courseId=0
      val url=infos(2)
        .split(" ")(1)
      if(url.startsWith("/class")){
        val courceHTML=url.split("/")(2)
        courseId=courceHTML.substring(0,courceHTML.lastIndexOf(".")).toInt
      }
      CheckLog(ip,time,courseId,infos(3).toInt,infos(4))
    }).filter(checkLog=>checkLog.courseId!=0)


    val allLogInfo=messages.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .as[(String, String)].map(_._2).map(lines=>{
      val infos=lines.split("\t")
      val ip=infos(0)
      val time=DateUtils.parseTime(infos(1))
      var courseId=0
      val url=infos(2)
      CheckLog(ip,time,courseId,infos(3).toInt,infos(4))
    })

   // spark.sparkContext.parallelize(Seq(listLogInfo)).saveToPhoenix("LogInfo",Seq("ip","time","courseId","statusCode","searchUrl"),zkUrl = Some("localhost:2181"))


    val CourseCount= LogInfo.map(x => {
      //将CheckLog格式的数据转为20180724_courseId
      x.time.substring(0, 8) + "_" + x.courseId
    }).groupBy("value").count()

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
    }).filter(x=>x._3!="").map(info=>{
      val day_search_course=info._1+"_"+info._3+"_"+info._2
      day_search_course
    }).groupBy("value").count()

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
      spark.sql("select * from allLogInfo").saveToPhoenix("LogInfo",zkUrl = Some("localhost:2181"))
      spark.sql("select * from CourseCount order by count desc limit 20").saveToPhoenix("CourseCount",zkUrl = Some("localhost:2181"))
      spark.sql("select * from SearchCount order by count desc limit 20").saveToPhoenix("SearchCount",zkUrl = Some("localhost:2181"))
      Thread.sleep(20 * 1000)
    }
  }

}

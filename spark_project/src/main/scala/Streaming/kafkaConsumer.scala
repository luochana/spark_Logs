package Streaming

import java.util.Date

import domain.CheckLog
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import utils.DateUtils
import org.apache.phoenix.spark._

object kafkaConsumer {
  def main(args:Array[String]):Unit={
    val sparkConf = new SparkConf().setAppName("KafkaConsumer").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(20))
    val topicsSet = Set("test")
    val brokers = "192.168.56.101:9092,192.168.56.101:9092,192.168.56.101:9092"
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers,
      "serializer.class" -> "kafka.serializer.StringDecoder")
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, topicsSet)

    val LogInfo=messages.map(_._2).map(lines=>{
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

    LogInfo.print()


    val listLogInfo=List(messages.map(_._2).map(lines=>{
      val infos=lines.split("\t")
      val ip=infos(0)
      val time=DateUtils.parseTime(infos(1))
      var courseId=0
      val url=infos(2)
      CheckLog(ip,time,courseId,infos(3).toInt,infos(4))
    }))

    ssc.sparkContext.parallelize(Seq(listLogInfo)).saveToPhoenix("LogInfo",Seq("ip","time","courseId","statusCode","searchUrl"),zkUrl = Some("localhost:2181"))


    val currentTime=new Date()
    val listCourseCount= List(LogInfo.map(x => {
      //将CheckLog格式的数据转为20180724_courseId+currentTime
      //rowkey加上currentTime是为了防止不同批次被处理的数据不会产生覆盖的现象,因为20秒处理一次
      (x.time.substring(0, 8) + "_" + x.courseId+ "_"+currentTime, 1)
    }).reduceByKey(_ + _))

    ssc.sparkContext.parallelize(Seq(listCourseCount)).saveToPhoenix("CourseCount", Seq("ID", "COUNT"), zkUrl = Some("localhost:2181"))


    val listSearchCount=List(LogInfo.map(x=>{
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
      (day_search_course+"_"+currentTime,1)
    }).reduceByKey(_+_))

    ssc.sparkContext.parallelize(Seq(listSearchCount)).saveToPhoenix("SearchCount", Seq("ID", "COUNT"), zkUrl = Some("localhost:2181"))


    ssc.start()
    ssc.awaitTermination()
  }

}

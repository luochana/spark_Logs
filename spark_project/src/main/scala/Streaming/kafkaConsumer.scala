package Streaming

import domain.CheckLog
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import utils.DateUtils

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


    
  }

}

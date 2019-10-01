# spark_Logs
本项目是基于spark structured streaming的网站日志分析系统. 其中的网站日志采用python脚本生成. 生成的数据主要是模拟某学习网站学习视频课程的访问量（其中模拟的日志中URL以"/class"开头的表示实战课程，然后通过流水线Flume+Kafka+Structured Streaming进行实时日志的收集，存储到hbase中(采用phoenix读写)<br>

## 需求分析 <br>
 1.统计该网站实战课程的访问量。<br>
 2.统计该网站实战课程从不同搜索引擎引流过来的访问量，通过结果可为该网站的课程广告投资的方向做出更准确的决策。<br>

## 前端效果：<br>
![image](https://github.com/luochana/githubPicture/blob/master/spark_logs_front1.png) <br>

## 一.日志处理： <br>
### 开发环境： <br>
 IntelliJ IDEA + maven + git + linux <br>
### 软件架构： <br>
 zookeeper + flume + kafka +  spark  + hbase + phoenix<br>

## 二.结果可视化： <br>
 使用SpringBoot+Echarts开发可视化展示页面<br>

<br>
项目持续更新中....<br>

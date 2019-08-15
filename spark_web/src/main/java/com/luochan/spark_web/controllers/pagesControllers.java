package com.luochan.spark_web.controllers;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@Controller
@EnableAutoConfiguration
public class pagesControllers {

    //根据课程编号来获取课程名称
    private static Map<String,String> map=new HashMap<String,String>();
    //根据搜索引擎域名来映射搜索引擎名
    private static Map<String,String> searchMap=new HashMap<String,String>();
    static {
        map.put("128", "10小时入门大数据");
        map.put("112", "大数据 Spark SQL慕课网日志分析");
        map.put("145", "深度学习之神经网络核心原理与算法");
        map.put("125", "基于Spring Boot技术栈博客系统企业级前后端实战");
        map.put("130", "Web前端性能优化");
        map.put("131", "引爆潮流技术\r\n" +
                "Vue+Django REST framework打造生鲜电商项目");
        searchMap.put("cn.bing.com", "微软Bing");
        searchMap.put("www.duba.com", "毒霸网址大全");
        searchMap.put("search.yahoo.com", "雅虎");
        searchMap.put("www.baidu.com", "百度");
        searchMap.put("www.sogou.com", "搜狗");
    }


    @RequestMapping("index")
    public String test() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
            connection = DriverManager.getConnection("jdbc:phoenix:localhost:2181");
            statement = connection.createStatement();
            statement.execute("upsert into user values ('0005', 'luochan','success')");
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

       return "redirect:/index.html";
    }



}

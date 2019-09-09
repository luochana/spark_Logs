package com.luochan.spark_web.controllers;

import com.luochan.spark_web.domain.CourseClickCount;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.luochan.spark_web.dao.HbasePhoenixDao.queryCourse;
import static com.luochan.spark_web.dao.HbasePhoenixDao.querySearch;

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
        querySearch("20190807");
        queryCourse("20190807");
       return "redirect:/index1.html";
    }

    @RequestMapping("get_coursecount")
    @ResponseBody
    public List<CourseClickCount> get_coursecount() throws Exception {
        List<CourseClickCount> list=queryCourse("20190807");
        for(CourseClickCount course:list) {
            System.out.println(course.toString());
            course.setName(map.get(course.getName()));
           // System.out.println(map.get(course.getName()));
        }
        return list;
    }

    @RequestMapping("get_courseSearch")
    @ResponseBody
    public List<CourseClickCount> get_courseSearch() throws Exception {
        List<CourseClickCount> list = querySearch("20190807");
        for (CourseClickCount course : list) {
            System.out.println(course.toString());
            course.setName(searchMap.get(course.getName()));
        }
        return list;
    }
}

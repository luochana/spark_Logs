package com.luochan.spark_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.luochan.spark_web.conf")
@ComponentScan("com.luochan.spark_web.controllers")
@SpringBootApplication
public class SparkWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SparkWebApplication.class, args);
    }

}

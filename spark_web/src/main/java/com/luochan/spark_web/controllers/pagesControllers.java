package com.luochan.spark_web.controllers;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Controller
@EnableAutoConfiguration
public class pagesControllers {
    @RequestMapping("index")
    public String test() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
            connection = DriverManager.getConnection("jdbc:phoenix:localhost:2181");
            statement = connection.createStatement();
            statement.execute("upsert into test1 values (3, 'note of huhong')");
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

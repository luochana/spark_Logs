package com.luochan.spark_web.dao;

import com.luochan.spark_web.domain.CourseClickCount;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HbasePhoenixDao {
    private static String driver = "org.apache.phoenix.jdbc.PhoenixDriver";

    public List<CourseClickCount> queryCourse(String dateStr)
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<CourseClickCount> queryResult=new ArrayList<CourseClickCount>();
        try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
            connection = DriverManager.getConnection("jdbc:phoenix:localhost:2181");
            statement = connection.createStatement();
            rs=statement.executeQuery("select * from CourseCount where id like "+"");
            while (rs.next()) {

                String rowKey=rs.getString("id");
                String courseId=rowKey.split("-")[1];
                Long value=Long.parseLong(rs.getString("count"));
                CourseClickCount temp=new CourseClickCount();
                temp.setName(courseId);
                temp.setValue(value);
                queryResult.add(temp);
            }
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

        return queryResult;
    }

    public List<CourseClickCount> querySearch(String dateStr)
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<CourseClickCount> queryResult=new ArrayList<CourseClickCount>();
        try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
            connection = DriverManager.getConnection("jdbc:phoenix:localhost:2181");
            statement = connection.createStatement();
            rs=statement.executeQuery("select * from test1");
            while (rs.next()) {
                String rowKey=rs.getString("id");
                String courseId=rowKey.split("-")[1];
                Long value=Long.parseLong(rs.getString("count"));
                CourseClickCount temp=new CourseClickCount();
                temp.setName(courseId);
                temp.setValue(value);
                queryResult.add(temp);
            }
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

        return queryResult;
    }
}

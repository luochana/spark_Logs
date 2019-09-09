package com.luochan.spark_web.dao;

import com.luochan.spark_web.domain.CourseClickCount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HbasePhoenixDao {
    private static String driver = "org.apache.phoenix.jdbc.PhoenixDriver";

    public static List<CourseClickCount> queryCourse(String dateStr)
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<CourseClickCount> queryResult=new ArrayList<CourseClickCount>();
        try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
            connection = DriverManager.getConnection("jdbc:phoenix:localhost:2181");
            statement = connection.createStatement();

            PreparedStatement pstmt = connection.prepareStatement("select * from CourseCount where CourseCountId like ?");
            pstmt.setString(1,"%"+dateStr+"%");
            rs=pstmt.executeQuery();

            while (rs.next()) {
                String rowKey=rs.getString("CourseCountId");
                String courseId=rowKey.split("_")[1];
                Long value=Long.parseLong(rs.getString("CCOUNT"));

                //********88
             //   System.out.println(courseId+value);

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

    public static List<CourseClickCount> querySearch(String dateStr)
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<CourseClickCount> queryResult=new ArrayList<CourseClickCount>();
        try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
            connection = DriverManager.getConnection("jdbc:phoenix:localhost:2181");
            statement = connection.createStatement();

            PreparedStatement pstmt = connection.prepareStatement("select * from SearchCount where SearchCountId like ?");
            pstmt.setString(1,"%"+dateStr+"%");
            rs=pstmt.executeQuery();

           // rs=statement.executeQuery("select * from SearchCount where SearchCountId like "+"\""+dateStr+"%"+"\"");
            while (rs.next()) {
                String rowKey=rs.getString("SearchCountId");
                String courseId=rowKey.split("_")[1];
                Long value=Long.parseLong(rs.getString("SCOUNT"));
                //*****
                System.out.println(courseId+value);

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

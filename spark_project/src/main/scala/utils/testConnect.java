package utils;

import java.sql.*;

public class testConnect {
    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        Statement statement = null;
    try {
        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        connection=DriverManager.getConnection("jdbc:phoenix:localhost:2181","","");
        statement =connection.createStatement();
        statement.execute("upsert into user values ('0002', 'note','qwe')");
        connection.commit();
        } catch(Exception e) {
            e.printStackTrace();
            } finally {
            try {
                connection.close();
                statement.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    }
}

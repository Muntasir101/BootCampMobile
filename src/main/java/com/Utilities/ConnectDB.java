package com.Utilities;

import java.sql.*;

public class ConnectDB {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        String username = "root";
        String password = "root";
        String databaseName = "testdb";
        String query = "select * from users;";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;
       Class.forName("com.mysql.cj.jdbc.Driver");

        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();

        ResultSet table = statement.executeQuery(query);

        System.out.println("Id " + "Name " + "Email " + "Address " + "Phone");

        while (table.next()) {
            int id = table.getInt("id");
            String name = table.getString("name");
            String email = table.getString("email");
            String address = table.getString("address");
            String phone = table.getString("phone");
            System.out.println(id + "  " + name + "  " + email+ "  " + address+ "  " + phone);
        }

        statement.close();
        connection.close();
    }


}

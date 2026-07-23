package com.library;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/library";

    private static final String USER = "root";

    private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";

    public static Connection getConnection() {

        try {

            Connection connection =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("✅ Connected to MySQL Successfully");

            return connection;

        } catch (Exception e) {

            System.out.println("Connection Failed");

            e.printStackTrace();

            return null;
        }

    }
}
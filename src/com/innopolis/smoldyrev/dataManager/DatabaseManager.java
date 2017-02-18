package com.innopolis.smoldyrev.dataManager;

import java.sql.*;

/**
 * Created by smoldyrev on 16.02.17.
 */
public class DatabaseManager {

    private static Connection connection;

    public static void initDatabase(String login, String password) {

        try {
            if (connection!=null) connection.close();
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5433/LFLChat";
            connection = DriverManager.getConnection(url, login, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initDatabase() {
        initDatabase("postgres", "123456");
    }

    public Statement loadFromDB() throws SQLException {

        Statement stmt = connection.createStatement();

        return stmt;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

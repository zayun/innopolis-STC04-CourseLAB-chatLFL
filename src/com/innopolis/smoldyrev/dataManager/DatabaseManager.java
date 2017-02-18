package com.innopolis.smoldyrev.dataManager;

import java.sql.*;

/**
 * Created by smoldyrev on 16.02.17.
 */
public class DatabaseManager {

    private static Connection connection;
    private static Statement stmt;

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

    public ResultSet loadFromDB(String sqlText) throws SQLException {

        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlText);
        return rs;
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

    public static void closeStatement() {
        try {
            if (stmt != null)
                stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

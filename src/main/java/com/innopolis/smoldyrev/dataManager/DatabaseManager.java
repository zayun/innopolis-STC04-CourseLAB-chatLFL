package com.innopolis.smoldyrev.dataManager;

import org.apache.log4j.Logger;

import java.sql.*;

/**
 * Created by smoldyrev on 16.02.17.
 */
public class DatabaseManager {

    private static Logger logger = Logger.getLogger(FileManager.class);

    private static Connection connection;

    public static void initDatabase(String login, String password) {

        try {
            if (connection!=null) connection.close();
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5433/LFLChat";
            connection = DriverManager.getConnection(url, login, password);

            logger.trace("Successful connect to base: "+url);
        } catch (ClassNotFoundException e) {
            logger.error(e);
            System.out.println("Не найден драйвер");
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Ошибка подключение к БД!");
        }
    }

    public static void initDatabase() {
        initDatabase("postgres", "123456");
    }

    public Statement getStatement() throws SQLException {

        if (connection != null) {
            Statement stmt = connection.createStatement();
            return stmt;
        } else {
            logger.error("connection is null");
            throw new SQLException("Соединение не создано!");
        }

    }

    public PreparedStatement getPrepearedStatement(String sqlText) throws SQLException {
        if (connection != null) {
            PreparedStatement pstmt = connection.prepareStatement(sqlText);
            return pstmt;
        } else {
            logger.error("connection is null");
            throw new SQLException("Соединение не создано!");
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null)
                connection.close();
            logger.trace("Connection was closed");
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Невозможно закрыть соединение!");
        }
    }

}

package com.innopolis.smoldyrev.dataManager;

import org.apache.log4j.Logger;

import java.sql.*;

/**
 * Created by smoldyrev on 16.02.17.
 * Класс для работы с БД
 */
public class DatabaseManager {

    private static Logger logger = Logger.getLogger(FileManager.class);

    private static Connection connection;

    /**Инициализация подключения к БД
     * устанавливает
     * @see DatabaseManager#connection
     * @throws ClassNotFoundException - при не загруженном драйвере
     * @throws SQLException - при ошибке подключения к БД
     * */
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

    /**Инициализация подключения к БД
     * при не указанных параметрах запускает
     * @see DatabaseManager#initDatabase(String, String) с параметрами по умолчанию
     * @throws ClassNotFoundException - при не загруженном драйвере
     * @throws SQLException - при ошибке подключения к БД
     * */
    public static void initDatabase() {
        initDatabase("postgres", "123456");
    }

    /**Возвращает подключение к БД
     * @return connection
     */
    public static Connection getConnection() {
        return connection;
    }

    /**Закрывает подключение к БД
     */
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

    /**Возвращает statement текущего подключения
     * не static потому что будут конфликты в потоках
     * спросить на консультации про это
     * @return stmt
     * @throws SQLException - невозможно получить connection
     * */
    public static synchronized Statement getStatement() throws SQLException {

        if (connection != null) {
            Statement stmt = connection.createStatement();
            return stmt;
        } else {
            logger.error("connection is null");
            throw new SQLException("Соединение не создано!");
        }

    }

    /**Возвращает preparedStatement текущего подключения
     * не static потому что будут конфликты в потоках
     * спросить на консультации про это
     * @param sqlText - текст запроса
     * @return pstmt
     * @throws SQLException - невозможно получить connection
     * */
    public static synchronized PreparedStatement getPrepearedStatement(String sqlText) throws SQLException {
        if (connection != null) {
            PreparedStatement pstmt = connection.prepareStatement(sqlText);
            return pstmt;
        } else {
            logger.error("connection is null");
            throw new SQLException("Соединение не создано!");
        }
    }
}

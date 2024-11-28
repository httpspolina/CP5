package server.db;

import server.ServerConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL = ServerConfig.getInstance().getDbUrl();
    private static final String DB_USER = ServerConfig.getInstance().getDbUser();
    private static final String DB_PASSWORD = ServerConfig.getInstance().getDbPassword();

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }

}

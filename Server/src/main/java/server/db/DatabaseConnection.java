package server.db;

import server.ServerConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL = ServerConfig.INSTANCE.getDbUrl();
    private static final String DB_USER = ServerConfig.INSTANCE.getDbUser();
    private static final String DB_PASSWORD = ServerConfig.INSTANCE.getDbPassword();

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

}

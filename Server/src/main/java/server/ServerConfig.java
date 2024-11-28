package server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfig {

    private static ServerConfig INSTANCE;

    private Properties properties;

    private ServerConfig() {
        loadProperties();
    }

    public static synchronized ServerConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServerConfig();
        }
        return INSTANCE;
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("server.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public int getServerPort() {
        return Integer.parseInt(properties.getProperty("server.port"));
    }

    public String getDbUrl() {
        return properties.getProperty("db.url");
    }

    public String getDbUser() {
        return properties.getProperty("db.user");
    }

    public String getDbPassword() {
        return properties.getProperty("db.password");
    }

    public String getAdminCode() {
        return properties.getProperty("admin.code");
    }

    public String getSupervisorCode() {
        return properties.getProperty("supervisor.code");
    }
}

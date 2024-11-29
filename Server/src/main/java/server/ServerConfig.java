package server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfig {

    public static ServerConfig INSTANCE = new ServerConfig();

    private final Properties properties;

    private ServerConfig() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("server.properties")) {
            this.properties = new Properties();
            this.properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

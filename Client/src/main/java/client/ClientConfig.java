package client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientConfig {

    private static ClientConfig INSTANCE;

    private Properties properties;

    private ClientConfig() {
        loadProperties();
    }

    public static synchronized ClientConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClientConfig();
        }
        return INSTANCE;
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("client.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public String getServerHost() {
        return properties.getProperty("server.host");
    }

    public int getServerPort() {
        return Integer.parseInt(properties.getProperty("server.port"));
    }

}

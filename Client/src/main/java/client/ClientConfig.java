package client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientConfig {

    public static final ClientConfig INSTANCE = new ClientConfig();

    private final Properties properties;

    private ClientConfig() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("client.properties")) {
            this.properties = new Properties();
            this.properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getServerHost() {
        return properties.getProperty("server.host");
    }

    public int getServerPort() {
        return Integer.parseInt(properties.getProperty("server.port"));
    }

}

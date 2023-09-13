package hashcracker.config;

import java.io.FileInputStream;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream inputStream = new FileInputStream("config/config.properties")) {
            properties.load(inputStream);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}

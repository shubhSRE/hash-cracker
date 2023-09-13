package hashcracker.AMQP;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import hashcracker.config.Config;

public class AMQPConnection {
    static ConnectionFactory factory;
    final static String URI = Config.getProperty("AMQP-URI");
    public static Connection connectionAMQP() {
        Connection connection = null;
        try {
            factory = new ConnectionFactory();
            factory.setUri(URI);
            connection = factory.newConnection();
        } catch (Exception e) {
            System.out.println("Error in AMQP Connection: " + e);
        }
        return connection;
    }
}

package hashcracker.AMQP;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class AMQPChannel {
    public Channel channel;

    public Channel createChannel() throws Exception {
        Connection connection = AMQPConnection.connectionAMQP();
        channel = connection.createChannel();
        return channel;
    }
}
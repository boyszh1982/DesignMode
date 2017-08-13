package com.nameless.mq.exchange.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class ReceiveLogsDirect {

	
	private static final String EXCHANGE_NAME = "direct_logs";
	
	/**
	 * 当消息发送到该routeKeys时，consumer没有启动，则消息丢失
	 * @param args
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static void main(String[] args) throws IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.128.141");
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		String queueName = channel.queueDeclare().getQueue();
		// channel与queue绑定
		// 如果将全部 , 日志级别, 
		// 指本Consumer订阅了 info error debug warning 这几个路由的key
		String[] routingKeys = new String[] { "info", "error", "debug" , "warning" };
		for (String routingKey : routingKeys) {
			channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
		}

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");

			}

		};
		
		channel.basicConsume(queueName, consumer);

	}
}

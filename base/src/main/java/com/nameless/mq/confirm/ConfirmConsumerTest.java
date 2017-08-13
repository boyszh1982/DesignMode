package com.nameless.mq.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class ConfirmConsumerTest {

	private static final String QUEUE_NAME = "confirm_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.128.141");
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		//why ?
		channel.basicQos(1);
		
		//channel.confirmSelect();
		
		channel.basicConsume(QUEUE_NAME, false , new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				
				String message = new String(body , "UTF-8");
				System.out.println("receive message : " + message);
				
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		});
		
	}
}

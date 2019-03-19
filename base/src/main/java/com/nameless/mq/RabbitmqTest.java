package com.nameless.mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitmqTest {

	private final static String QUEUE_NAME = "hello";
	
	private static int QUEUE_MESSAGE_INDEX = 0;

	public static void main(String[] args) throws IOException, TimeoutException {

		RabbitmqTest t = new RabbitmqTest();

		for (int i = 0; i < 100000; i++) {
			t.sand();
		}
		
		//t.recv();
		
	}

	public void sand() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("10.2.36.189");
		factory.setUsername("guest");
		factory.setPassword("guest");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String message = "["+(QUEUE_MESSAGE_INDEX++)+"] Hello World !";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println("Send a message [" + message + "]");

		channel.close();
		connection.close();
	}
	
	public void recv() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.128.141");
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
				
		channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				
				String message = new String(body,"UTF-8");
				
				System.out.println("Received a message ["+ message +"]");
				
			}
		});
		// here is blocking
	}
}

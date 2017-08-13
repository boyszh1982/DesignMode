package com.nameless.mq.exchange.fanout;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLog {

	private static final String EXCHANGE_NAME = "logs";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.128.141");
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		/**
		 * The fanout exchange is very simple. 
		 * As you can probably guess from the name, 
		 * it just broadcasts all the messages it receives to all the queues it knows. 
		 * And that's exactly what we need for our logger.
		 */
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		
		String message = "";
		String routingKey = ""; //原来的 hello or task_queue
		for(int i=0;i<10;i++){
			message = new String("测试消息 :"+ i);
			channel.basicPublish(EXCHANGE_NAME, routingKey , null , message.getBytes());
		}
		
		
		channel.close();
		connection.close();
	}
}

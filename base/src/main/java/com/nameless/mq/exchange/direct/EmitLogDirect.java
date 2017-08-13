package com.nameless.mq.exchange.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogDirect {

	private static final String EXCHANGE_NAME = "direct_logs";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.128.141");
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		
		String routingKey = "info";
		String message = "this is a info log message !";
		channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
		
		send("debug","this is a debug log message !", channel);
		send("error","this is a error log message !", channel);
		send("warning","this is a warning log message !", channel);
		
		channel.close();
		connection.close();
		
		
	}
	
	public static void send(String routingKey, String message, Channel channel) throws IOException{
		
		channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
		
	}
}

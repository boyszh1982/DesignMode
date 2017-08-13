package com.nameless.mq.rpc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class RPCClient {

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		RPCClient client = null;
		try {
			//blocking test
			client = new RPCClient();
			String response = client.call("30");
			System.out.println(" [.] Got '" + response + "'");
		} finally {
			if(client != null)
				client.close();
		}
	}
	
	private Connection connection;
	private Channel channel;
	private String requestQueueName = "rpc_queue";
	private String replyQueueName;

	public RPCClient() throws IOException, TimeoutException {
		// TODO Auto-generated constructor stub
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.128.141");
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq");
		connection = factory.newConnection();
		channel = connection.createChannel();

		replyQueueName = channel.queueDeclare().getQueue();
	}

	public String call(String message) throws UnsupportedEncodingException, IOException, InterruptedException {
		String corrId = UUID.randomUUID().toString();
		AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName)
				.build();

		String routingKey = replyQueueName;
		channel.basicPublish("", routingKey, props, message.getBytes("UTF-8"));

		final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

		channel.basicConsume(routingKey, true, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {

				if (properties.getCorrelationId().equals(corrId)) {
					response.offer(new String(body, "UTF-8"));
				}

			}

		});
		
		return response.take();
	}
	
	public void close() throws IOException {
		connection.close();
	}
}

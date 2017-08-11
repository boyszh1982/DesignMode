package com.nameless.mq;

import com.rabbitmq.client.*;
import java.io.IOException;

/**
 * 消息消费者
 * 
 * @author hushuang
 * 
 */
public class C {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws Exception {
		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 设置RabbitMQ地址
		factory.setHost("192.168.128.141");
		// 创建一个新的连接
		Connection connection = factory.newConnection();
		// 创建一个频道
		final Channel channel = connection.createChannel();
		// 声明要关注的队列 --
		/*
		 * 在RabbitMQ中，队列声明是幂等性的（一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同），
		 * 也就是说，如果不存在，就创建，
		 * 如果存在，不会对已经存在的队列产生任何影响。
		 * autoDelete = true 关闭连接则删除
		 */
		boolean autoDelete = false ;
		channel.queueDeclare(QUEUE_NAME, false, false, autoDelete, null);
		System.out.println("C [*] Waiting for messages. To exit press CTRL+C");
		/*
		 * DefaultConsumer类实现了Consumer接口，通过传入一个频道，
		 * 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
		 */
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, 
					Envelope envelope, 
					AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				
				System.out.println("consumerTag:"+consumerTag);
				String routingKey = envelope.getRoutingKey();
				String contentType = properties.getContentType();
				long deliveryTag = envelope.getDeliveryTag();
				System.out.println("routingKey:"+routingKey);
				System.out.println("contentType:"+contentType);
				System.out.println("deliveryTag:"+deliveryTag);
				
				String message = new String(body, "UTF-8");
				System.out.println("C [x] Received '" + message + "'");
				
				//手动消费消息确认
				channel.basicAck(deliveryTag, false);
				
				// ???? 
				//channel.basicCancel(consumerTag);
			}
		};
		/*
		 * 自动回复队列应答 -- RabbitMQ中的消息确认机制，后面章节会详细讲解
		 * Here, since we specified autoAck = false, 
		 * it is necessary to acknowledge messages delivered to the Consumer, 
		 * most conveniently done in the handleDelivery method, as illustrated.
		 */
		boolean autoAck = false;
		channel.basicConsume(QUEUE_NAME, autoAck, consumer );
	}
}


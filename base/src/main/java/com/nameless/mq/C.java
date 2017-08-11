package com.nameless.mq;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 消息消费者
 * 
 * @author hushuang
 * 
 */
public class C {

	private final static String CONSUMER_NAME = "C3";
	private final static String QUEUE_NAME = "hello";

	/**
	 * 消息的事物？
	 * @param argv
	 * @throws Exception
	 */
	public static void main(String[] argv) throws Exception {
		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 设置RabbitMQ地址
		factory.setHost("192.168.128.141");
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq");
		factory.setConnectionTimeout(5000*1000);
		factory.setHandshakeTimeout(500000);
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
		/*
		 * First, we need to make sure that RabbitMQ will never lose our queue. 
		 * In order to do so, we need to declare it as durable:
		 * 
		 * At this point we're sure that the task_queue queue won't be lost even if RabbitMQ 
		 * restarts. Now we need to mark our messages as persistent - by setting 
		 * MessageProperties (which implements BasicProperties) 
		 * to the value PERSISTENT_TEXT_PLAIN.
		 */
		boolean durable = true;
		channel.queueDeclare(QUEUE_NAME, durable, false, autoDelete, null);
		System.out.println(CONSUMER_NAME+" [*] Waiting for messages. To exit press CTRL+C");

		// what's this
		channel.basicQos(1);
		
		/*
		 * DefaultConsumer类实现了Consumer接口，通过传入一个频道，
		 * 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
		 */
		Consumer consumer = new DefaultConsumer(channel) {
			/*
			 * TODO 用DEBUGGER跟中是谁调用了 handleDelivery() 传入的 envelope.deliveryTag 究竟是什么？
			 * (non-Javadoc)
			 * @see com.rabbitmq.client.DefaultConsumer#handleDelivery(java.lang.String, com.rabbitmq.client.Envelope, com.rabbitmq.client.AMQP.BasicProperties, byte[])
			 */
			@Override
			public void handleDelivery(String consumerTag, 
					Envelope envelope, 
					AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				
				
				String routingKey = envelope.getRoutingKey();
				String contentType = properties.getContentType();
				long deliveryTag = envelope.getDeliveryTag();
				
				System.out.print("consumerTag:"+consumerTag);
				System.out.print(",routingKey:"+routingKey);
				System.out.print(",contentType:"+contentType);
				System.out.println(",deliveryTag:"+deliveryTag);
				
				String message = new String(body, "UTF-8");
				System.out.println(CONSUMER_NAME+" [x] Received '" + message + "'");
				
				//手动消费消息确认
				//根据时间结尾字符判断是Ack消息还是NAck消息，NAck时requeue=true,启动多个consumer去执行。
				//看看消息失败时是否转移到其他consumer上了。
				SimpleDateFormat sdf = new SimpleDateFormat("ss");
				String ss = sdf.format(new java.util.Date());
				//30秒以上时Ack
				if(Integer.valueOf(ss) > 30 ) {
					channel.basicAck(deliveryTag, false);
				}
				else{
					//Nack并且重新入队列。
					channel.basicNack(deliveryTag, false, false);
					
					
				}
				//System.out.println(channel.getClass());
				// ???? 
				//channel.basicCancel(consumerTag);
				try {
					Thread.sleep(1*1L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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


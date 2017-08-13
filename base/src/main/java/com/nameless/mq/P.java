package com.nameless.mq;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.ReturnListener;

/**
 * 消息生产者
 * 
 * @author hushuang
 * 
 */
public class P {

	private final static String QUEUE_NAME = "hello";
		
	public static void main(String[] argv) throws Exception {
		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 设置RabbitMQ地址
		factory.setHost("192.168.128.141");
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq");
		//factory.setPort(5672);
		// 创建一个新的连接
		Connection connection = factory.newConnection();
		// 创建一个频道
		Channel channel = connection.createChannel();
		System.out.println(channel.getClass());
		// 声明一个队列 --
		/*
		 * 在RabbitMQ中，队列声明是幂等性的（一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同），
		 * 也就是说，如果不存在，就创建，如果存在，不会对已经存在的队列产生任何影响。
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
		
		/*
		 * confirm 主要是用来判断消息是否有正确到达交换机，如果有，
		 * 那么就 ack 就返回 true；如果没有，则是 false。
		 * 没有测试出效果 , 需要设置confirm模式 channel.confirmSelect();
		 */
		channel.addConfirmListener(new ConfirmListener(){
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("handleAck.deliveryTag:"+deliveryTag);
				System.out.println("handleAck.multiple:"+multiple);
			}

			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("handleNack.deliveryTag:"+deliveryTag);
				System.out.println("handleNack.multiple:"+multiple);
			}
		});
		/*
		 * return 则表示如果你的消息已经正确到达交换机，但是后续处理出错了，那么就会回调 return，
		 * 并且把信息送回给你（前提是需要设置了 Mandatory，不设置那么就丢弃）；
		 * 如果消息没有到达交换机，那么不会调用 return 的东西。
		 * 没有测试出效果
		 */
		channel.addReturnListener(new ReturnListener(){
			public void handleReturn(int arg0, String arg1, String arg2, String arg3, BasicProperties arg4, byte[] arg5)
					throws IOException {
				System.out.println("handleReturn.arg0:"+arg0);
				System.out.println("handleReturn.arg1:"+arg1);
				System.out.println("handleReturn.arg2:"+arg2);
				System.out.println("handleReturn.arg3:"+arg3);
				System.out.println("handleReturn.arg4:"+arg4);
				System.out.println("handleReturn.arg5:"+arg5);
			}
		});
		
		StringBuffer message = new StringBuffer();
		for(int i=0;i<100;i++){
			message.append("%s#Hello World! "+Math.random());
			// 发送消息到队列中
			channel.basicPublish("", QUEUE_NAME, true ,
					MessageProperties.PERSISTENT_TEXT_PLAIN,
					String.format(message.toString(), i).getBytes("UTF-8"));
			//System.out.println("P [x] Sent '" + message + "'");
			message.delete(0, message.length());
			Thread.sleep(10*1000L);
		}
		
		//Thread.sleep(100*1000L);
		// 关闭频道和连接
		channel.close();
		connection.close();
	}
}
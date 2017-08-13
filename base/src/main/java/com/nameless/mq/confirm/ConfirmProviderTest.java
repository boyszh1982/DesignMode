package com.nameless.mq.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.Confirm;

public class ConfirmProviderTest {

	private static final String QUEUE_NAME = "confirm_queue";

	public static void main(String[] args) throws IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.128.141");
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// set channel to confirm
		// 只Provider设计即可。
		Confirm.SelectOk selectOk = channel.confirmSelect();
		
		// set confirm listener
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
		
		try {
			String message = "要发送的消息,运算结果 =%s";
			message = String.format(message, 1 / 1 + "");
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
			if(!channel.waitForConfirms()) {
				System.out.println("Send message failed !");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		channel.close();
		connection.close();

	}

}

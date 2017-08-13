package com.nameless.mq.transaction;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.Tx;

public class TransactionProviderTest {

	private static final String QUEUE_NAME = "transaction_queue";

	public static void main(String[] args) throws IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.128.141");
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// set channel to transaction
		Tx.SelectOk selectOk = channel.txSelect();
		try {
			String message = "要发送的消息,运算结果 =%s";
			message = String.format(message, 1 / 0 + "");
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
			// commit
			Tx.CommitOk commitOk = channel.txCommit();
		} catch (Exception e) {
			// rollback
			Tx.RollbackOk rollbackOk = channel.txRollback();
		}

		channel.close();
		connection.close();

	}

}

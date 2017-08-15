package com.nameless.base.nio.mytest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NioChatServer {
	
	//queue的大小怎么设置？
	private static final ConcurrentLinkedQueue<SocketChannel> queue = 
			new ConcurrentLinkedQueue<>();

	/**
	 * Chat summary 1、broadcast 2、point to point
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args)
			throws IOException, InterruptedException {

		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(7001));
		serverSocketChannel.configureBlocking(false);

		while (true) {
			SocketChannel socketChannel = serverSocketChannel.accept();
			if (socketChannel != null && socketChannel.finishConnect()) {
				System.out.println(
						"a connector ! " + socketChannel.getRemoteAddress());
				queue.offer(socketChannel);
			} else {
				System.out.println("wait a connector !");
				Thread.sleep(1000L);
			}
		}
	}
	
	//
}

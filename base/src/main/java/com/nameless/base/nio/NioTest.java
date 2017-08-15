package com.nameless.base.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by Administrator on 2017-08-08.
 */
public class NioTest {

    /*
	    服务端：ServerSocketChannel
	    客户端：SocketChannel
	    open(); //建立通道
	    channel.regist(selector , "唯一标识") //注册使用标识
	    Selector 选择器
	    open(); 开始营业
	    selectKeys(); //获取当前已经注册的所有使用标识
	
	    Buffer
	    put() //往缓冲区写
	    get() //从缓冲区读
	    flip() //切换读写模式
	    clear() //清空缓冲区
	
	    selectionKey
	    isAcceptable() //是否可接受客户请求
	    isConnectionable() //是否已经连接
	    isReadable() //是否可读
	    isWriteable() //是否可写

     */

    public static void main(String[] args) throws IOException {

        int n = 1;
        n = 1 << 0; // 1 * 2的0次方
        System.out.println(Integer.toBinaryString(n) + "," +n);
        n = 1 << 2; // 1 * 2的2次方
        System.out.println(Integer.toBinaryString(n) + "," +n);
        n = 1 << 3; // 1 * 2的3次方
        System.out.println(Integer.toBinaryString(n) + "," +n);
        n = 1 << 4; // 1 * 2的4次方
        System.out.println(Integer.toBinaryString(n) + "," +n);

        SocketAddress address = new InetSocketAddress(7001);
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(address);

    }

}

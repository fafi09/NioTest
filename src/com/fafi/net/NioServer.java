package com.fafi.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

	public static void main(String[] args) throws Exception {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		
		/*ServerSocket ss = ssc.socket();
		ss.bind(new InetSocketAddress(8888));*/
		ssc.bind(new InetSocketAddress(8888));
		Selector selector = Selector.open();
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		
		handleMsg(selector);
		handleMsg(selector);
		handleMsg(selector);
		
	}
	
	public static void handleMsg(Selector selector) throws Exception {
		int num = selector.select();
		if(num > 0) {
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = selectedKeys.iterator();
			while(it.hasNext()) {
				SelectionKey sk = it.next();
				if(sk.isAcceptable()) {
					ServerSocketChannel serverSocketChannel = (ServerSocketChannel) sk.channel();
					SocketChannel socketChannel = serverSocketChannel.accept();
					socketChannel.configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_READ);
					
				} else if(sk.isReadable()){
					SocketChannel socketChannel = (SocketChannel) sk.channel();
					ByteBuffer receiveBuff = ByteBuffer.allocate(1024);
					int count = socketChannel.read(receiveBuff);
					if(count > 0) {
						System.out.println("服务器端从客户端接受到数据："+ new String(receiveBuff.array(),0,count));
						socketChannel.register(selector, SelectionKey.OP_WRITE);
					}
				} else if(sk.isWritable()) {
					SocketChannel socketChannel = (SocketChannel) sk.channel();
					ByteBuffer sendBuff = ByteBuffer.allocate(1024);
					String sendStr = "hello";
					sendBuff.put(sendStr.getBytes());
					sendBuff.flip();
					socketChannel.write(sendBuff);
				}
				it.remove();
			}
		}
	}

}

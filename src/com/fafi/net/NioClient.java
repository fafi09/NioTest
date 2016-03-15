package com.fafi.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
/**
 * 可以用com.fafi.test.NIOServer进行配合测试，server一直
 * 处于监听状态，客户端拿到想要的信息后关闭通道
 * @author fi
 *
 */
public class NioClient {
	public static void main(String[] args) throws Exception {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		
		Selector selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
		socketChannel.connect(new InetSocketAddress("localhost", 8888));
		
		handleMsg(selector);
		handleMsg(selector);
		
		socketChannel.close();
 	}
	
	public static void handleMsg(Selector selector) throws Exception {
		int num = selector.select();
		if(num > 0) {
			Set<SelectionKey> selectKey = selector.selectedKeys();
			Iterator<SelectionKey> it = selectKey.iterator();
			while(it.hasNext()) {
				SelectionKey key = it.next();
				
				if(key.isConnectable()) {
					SocketChannel sc = (SocketChannel) key.channel();
					if(sc.isConnectionPending()) {
						sc.finishConnect();
						System.out.println("完成链接");
						ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
						sendBuffer.put("hello,server".getBytes());
						sendBuffer.flip();
						sc.write(sendBuffer);
						sc.register(selector, SelectionKey.OP_READ);
					}
				} else if(key.isReadable()) {
					SocketChannel sc = (SocketChannel) key.channel();
					ByteBuffer receiveBuff = ByteBuffer.allocate(1024);
					int count = sc.read(receiveBuff);
					if(count > 0) {
						System.out.println("客户端从服务器端接受到数据："+ new String(receiveBuff.array(),0,count));
						//sc.register(selector, SelectionKey.OP_WRITE);
					}
				} else if(key.isWritable()) {
					/*SocketChannel sc = (SocketChannel) key.channel();
					ByteBuffer sendBuff = ByteBuffer.allocate(1024);
					String sendStr = "hello";
					sendBuff.put(sendStr.getBytes());
					sendBuff.flip();
					sc.write(sendBuff);*/
				}
				
				it.remove();
			}
		}
	}
}

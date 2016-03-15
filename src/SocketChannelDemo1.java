import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelDemo1 {
	public static void main(String[] args) throws Exception {
		SocketChannel sc = SocketChannel.open();
		sc.connect(new InetSocketAddress("localhost", 10001));
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.put("hello world".getBytes());
		buf.flip();
		sc.write(buf);
		 while (true) {  
			 buf.clear();  
             int readBytes = sc.read(buf);  
             if (readBytes > 0) {  
            	 buf.flip();  
            	 sc.close();  

                 System.out.println(new String(buf.array(),buf.position(),buf.limit()));
                 break;  
             }  
             
         }  
		

	}
}

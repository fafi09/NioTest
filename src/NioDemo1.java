import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class NioDemo1 {

	public static void main(String[] args) throws Exception {
		File file = new File("abc.txt");
		FileInputStream fis = new FileInputStream(file);
		FileChannel fc = fis.getChannel();
		ByteBuffer bb = ByteBuffer.allocate(4);
		int bytesRead = fc.read(bb);
		while(bytesRead != -1) {
			System.out.println("buffer is reading");
			bb.flip();
			/*while(bb.hasRemaining()) {
				System.out.println((char)bb.get());
			}*/
			System.out.println(bb.position());
			System.out.println(bb.limit());
			System.out.println(bb.capacity());
			System.out.println(new String(bb.array(),0,bb.limit()));
			System.out.println(bb.position());
			System.out.println(bb.limit());
			System.out.println(bb.capacity());
			bb.rewind();
			System.out.println(bb.position());
			System.out.println(bb.limit());
			System.out.println(bb.capacity());
			bb.position(2);
			bb.mark();
			bb.reset();
			System.out.println(bb.position());
			System.out.println(bb.limit());
			System.out.println(bb.capacity());
			bb.clear();
			System.out.println(new String(bb.array(),0,bb.limit()));
			bytesRead = fc.read(bb);
		}
		fc.close();
		fis.close();
		
	}
	
	public static void readBufferToFile() throws Exception {
		File file = new File("abc.txt");
		FileOutputStream fos = new FileOutputStream(file);
		FileChannel fc = fos.getChannel();
		String srcStr = "hello world";
		ByteBuffer bb = ByteBuffer.allocate(48);
		bb.put(srcStr.getBytes());
		bb.flip();
		fc.write(bb);
		fc.close();
		fos.close();

	}

	public static void readFileToBuffer() {
		
	}
}

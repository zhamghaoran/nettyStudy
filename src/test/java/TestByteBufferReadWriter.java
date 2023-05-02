import java.nio.ByteBuffer;

public class TestByteBufferReadWriter {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put((byte) 0x61);
        ByteBufferUtil.debugAll(byteBuffer);
        byteBuffer.put(new byte[] {0x62,0x63,0x64});
        ByteBufferUtil.debugAll(byteBuffer);  // 写入bcd
        byteBuffer.flip();  // 切换成读模式
        System.out.println( (char) byteBuffer.get());
        ByteBufferUtil.debugAll(byteBuffer);
        byteBuffer.compact();
        ByteBufferUtil.debugAll(byteBuffer);
        byteBuffer.put((byte) 0x65);
        byteBuffer.put((byte) 0x66);
        ByteBufferUtil.debugAll(byteBuffer);
    }
}

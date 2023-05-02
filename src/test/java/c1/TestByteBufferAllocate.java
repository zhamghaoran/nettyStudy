package c1;

import java.nio.ByteBuffer;

public class TestByteBufferAllocate {
    public static void main(String[] args) {
//        System.out.println(ByteBuffer.allocate(10).getClass());
//        System.out.println(ByteBuffer.allocateDirect(10).getClass());
        /*
        class java.nio.HeapByteBuffer    使用的java的堆内存, 读写效率较低,收到GC的影响
        class java.nio.DirectByteBuffer  使用的是直接内存, 读写效率较高(少一次数据的拷贝), 不会收到GC的影响, 内存分配的效率低一些, 可能会导致内存泄漏
         */
        // 直接调用get(int i) 就可以直接获取到第i个所以得byte,并且它不会移动指针
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put((byte) 0x61);
        byteBuffer.put((byte) 0x62);
        byteBuffer.rewind();  // 会让position的位置归0
        byteBuffer.flip();
        byte b = byteBuffer.get(); // get会让pos的位置后移一位
        System.out.println((char) b);
        byteBuffer.put((byte) 0x63); // 所以索引为2的位置的值被改变了
        ByteBufferUtil.debugAll(byteBuffer);

        // mark和rest
        /*
        mark是在读取的时候做一个标记，那么当我们调用reset方法的时候指针就会回到mark的位置
         */
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(10);
        byteBuffer1.put(new byte[] {0x61,0x62,0x63,0x64});
        byteBuffer1.flip();
        byte b1 = byteBuffer1.get();
        byteBuffer1.mark();
        byte b2 = byteBuffer1.get();
        System.out.println((char) b1 + " " + (char) b2);
        byteBuffer1.reset();
        ByteBufferUtil.debugAll(byteBuffer1);
        System.out.println((char) byteBuffer1.get());

    }
}

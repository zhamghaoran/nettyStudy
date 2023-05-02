package c1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) throws FileNotFoundException {
        //FileChannel
        // 1.输入输出流 2. RandomAccessFile
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            // 准备缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(10); // 划分一个10字节的缓冲区
            while (true) {
                // 从channel中读取数据,向buffer写入数据
                int read = channel.read(byteBuffer);
                log.debug("读取到的字节数{}", read);
                if (read == -1)  // 没有内容了就退出
                    break;
                // 打印buffer的内容
                byteBuffer.flip(); //  切换成读模式
                while (byteBuffer.hasRemaining()) {
                    byte b = byteBuffer.get();
                    log.debug("读取到的实际字节{}", (char) b);
                }
                // 切换成写模式
                byteBuffer.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

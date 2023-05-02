import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class TestGarthingWrite {
    public static void main(String[] args) throws IOException {
        ByteBuffer b1 = StandardCharsets.UTF_8.encode("hello");
        ByteBuffer b2 = StandardCharsets.UTF_8.encode("world");
        ByteBuffer b3 = StandardCharsets.UTF_8.encode("你好");
        FileChannel rw = new RandomAccessFile("words2.txt", "rw").getChannel();
        rw.write(new ByteBuffer[]{b1, b2, b3});

    }
}

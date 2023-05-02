package c1;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TestFileChannel {
    public static void main(String[] args) {
        ByteBuffer encode = StandardCharsets.UTF_8.encode("123456");

        while (encode.hasRemaining()) {

        }
    }
}

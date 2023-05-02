import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringByteBuffer {
    public static void main(String[] args) {
        ByteBuffer encode = StandardCharsets.UTF_8.encode("123456");
        ByteBufferUtil.debugAll(encode);
        ByteBuffer encode1 = Charset.forName("utf-8").encode("123456");
        ByteBufferUtil.debugAll(encode1);
        CharBuffer decode = StandardCharsets.UTF_8.decode(encode);
        System.out.println(decode.getClass());
        System.out.println(decode.toString());
    }
}

package c1;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringByteBuffer {
    public static void main(String[] args) {
        // 字符串转成buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.put("hello".getBytes());
        ByteBufferUtil.debugAll(byteBuffer);
        // 2.charSet  它会自动切换的读模式，并且limit和字符串长度相同
        ByteBuffer encode = StandardCharsets.UTF_8.encode("hello");
        ByteBufferUtil.debugAll(encode);
        // wrap  和上面的同理
        ByteBuffer wrap = ByteBuffer.wrap("hello".getBytes());
        ByteBufferUtil.debugAll(wrap);

        // 转为字符串
        String str = StandardCharsets.UTF_8.decode(encode).toString();
        System.out.println(str);

        // 使用第一种方式需要切换到读模式才能正常的输出
        byteBuffer.flip();
        String string = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        System.out.println(string);

    }
}

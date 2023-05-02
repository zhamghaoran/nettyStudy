import java.nio.ByteBuffer;

public class TestBufferExam {
    /**
     * 网络上有多条数据发送给服务端，数据之间使用 \n 进行分隔
     * 但由于某种原因这些数据在接收时，被进行了重新组合，例如原始数据有3条为
     * Hello,world\n
     * I'm zhangsan\n
     * How are you?\n
     * 变成了下面的两个 byteBuffer
     * Hello,world\nI'm zhangsan\nHo
     * w are you?\n
     * 现在要求你编写程序，将错乱的数据恢复成原始的按 \n 分隔的数据
     */
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\nhaha!\n".getBytes());
        split(source);
    }

    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            // 找到一条完整消息
            if (source.get(i) == '\n') {
                int len = i + 1 - source.position();
                // 把这条完整消息存到新的bytebuffer
                ByteBuffer tar = ByteBuffer.allocate(len);
                // 从source里面读
                for (int j = 0; j < len; j++) {
                    tar.put(source.get());
                }
                ByteBufferUtil.debugAll(tar);
            }
        }
        source.compact();
    }
}

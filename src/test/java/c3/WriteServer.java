package c3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

public class WriteServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector selector = Selector.open();
        SelectionKey scKey = ssc.register(selector, SelectionKey.OP_ACCEPT, null);
        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    // 向客户端发送大量数据
                    StringBuilder sb = new StringBuilder();
                    sb.append("a".repeat(5000000));
                    ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                    // 返回值代表实际写入的字节数
                    int write = sc.write(buffer);
                    System.out.println(write);
                    // 判断是否有剩余内容
                    if (buffer.hasRemaining()) {
                        // 关注一个可写事件
                        scKey.interestOps(scKey.interestOps() + SelectionKey.OP_WRITE);
                        // 把未写完的数据挂到sckey上面
                        scKey.attach(buffer);
                    }
                } else if (key.isWritable()) {
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel sc = (SocketChannel) key.channel();
                    int write = sc.write(buffer);
                    System.out.println(write);
                    // 6 .清理
                    if (!buffer.hasRemaining()) {
                        // 需要清除buffer
                        key.attach(null);
                        // 不需要再关注可写事件
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                    }
                }
            }
        }
    }
}

package c2;

import c1.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        //1. 创建selector, 管理多个Channel
        Selector selector = Selector.open();

        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        // 2. 建立selector 和 Channel 的联系 (注册)
        // SelectionKey 事件发生后，通过它可以得到这个是什么事件，可以知道是哪个Channel发生的事件
        SelectionKey sscKey = ssc.register(selector, 0, null);

        sscKey.interestOps(SelectionKey.OP_ACCEPT); // key只关注accept事件
        log.debug("register key:{}", sscKey);
        ssc.bind(new InetSocketAddress(8080));
        List<SocketChannel> channelList = new ArrayList<>();
        while (true) {
            // 3.有没有发生事件, 没有事件就阻塞，线程阻塞，有事发生线程才会恢复运行
            // 在有事件被处理时，不会阻塞, 事件发生之后，要不处理，要不取消，不能不管
            selector.select();
            // 处理事件，SelectedKeys 内部包含了所有发生的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();  // accept,read,
            selectionKeys.forEach(i -> {
                log.debug("key: {}", i);
                // 5.区分事件类型
                if (i.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) i.channel();
                    try {
                        SocketChannel sc = channel.accept();
                        sc.configureBlocking(false);
                        SelectionKey scKey = sc.register(selector, 0, null);
                        scKey.interestOps(SelectionKey.OP_READ);
                        log.debug("{}", sc);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (i.isReadable()) {
                    // 读取数据的处理
                    SocketChannel channel = (SocketChannel) i.channel(); // 拿到触发事件的Channel
                    try {
                        channel.read(byteBuffer);
                        byteBuffer.flip();
                        ByteBufferUtil.debugAll(byteBuffer);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}

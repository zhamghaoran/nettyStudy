package c2;

import c1.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
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
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey i = iterator.next();
                // 处理key的时候，处理完了之后要及时删除
                iterator.remove();
                log.debug("key: {}", i);
                // 5.区分事件类型
                if (i.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) i.channel();
                    try {
                        SocketChannel sc = channel.accept();
                        sc.configureBlocking(false);
                        ByteBuffer buffer = ByteBuffer.allocate(16);
                        // 将byteBuffer作为附件关联到selectionKey上面
                        SelectionKey scKey = sc.register(selector, 0, buffer);
                        scKey.interestOps(SelectionKey.OP_READ);
                        log.debug("{}", sc);
                    } catch (IOException e) {
                        e.printStackTrace();
                        i.cancel();
                    }
                } else if (i.isReadable()) {
                    // 读取数据的处理
                    SocketChannel channel = (SocketChannel) i.channel(); // 拿到触发事件的Channel
                    try {
                        // 获取selectionKey上面的附件
                        ByteBuffer buffer = (ByteBuffer) i.attachment();  // 获取
                        int read = channel.read(buffer); // 如果是正常断开read返回值是-1
                        if (read <= 1) {
                            i.cancel();
                        } else {
                            split(buffer);
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                i.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        // 因为客户端断开了，因此需要把这个key取消（从selector的key集合中真正删除）
                        e.printStackTrace();
                        i.cancel();
                    }
                }
            }
        }
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

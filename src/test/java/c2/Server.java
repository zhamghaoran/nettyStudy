package c2;

import c1.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

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
        sscKey.interestOps();
        ssc.bind(new InetSocketAddress(8080));
        List<SocketChannel> channelList = new ArrayList<>();
        while (true) {
            SocketChannel accept = ssc.accept();
            if (accept != null) {
                log.debug("connected..{}", accept);
                ssc.configureBlocking(false);
                channelList.add(accept);
            }
            channelList.forEach(i -> {
                try {
                    int read = i.read(byteBuffer);
                    if (read > 0) {
                        byteBuffer.flip();
                        ByteBufferUtil.debugAll(byteBuffer);
                        byteBuffer.clear();
                        log.debug("afterRead...{}", accept);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}

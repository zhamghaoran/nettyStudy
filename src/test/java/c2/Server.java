package c2;

import c1.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        // 使用nio 来理解阻塞模式 , 单线程来处理
        // 0.ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        // 1.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false); //  切换成非阻塞模式
        // 2.绑定一个监听端口
        ssc.bind(new InetSocketAddress(8080));
        // 3.连接的集合
        List<SocketChannel> channelList = new ArrayList<>();
        while (true) {
            // 4.建立与客户端的连接,socketChannel 用来与客户端通信
//            log.debug("connecting...");
            SocketChannel accept = ssc.accept(); // 非阻塞方法,线程停止运行了,如果没有连接建立，返回一个null值
            if (accept != null) {
                log.debug("connected..{}", accept);
                ssc.configureBlocking(false); // 非阻塞模式
                channelList.add(accept);
            }
            // 接受客户端发送的数据
            channelList.forEach(i -> {
                try {
//                    log.debug("beforeRead...{}", accept);
                    int read = i.read(byteBuffer);  // 非阻塞方法,线程继续运行, 如果没有读到数据，read会返回0
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

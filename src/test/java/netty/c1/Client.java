package netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        // 创建启动器类
        Channel channel = new Bootstrap()
                // 添加组件EventLoop
                .group(new NioEventLoopGroup())
                // 选择客户端ServerSocketChannel
                .channel(NioSocketChannel.class)
                // 添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    // 在连接建立之后被调用
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 异步非阻塞的方法 main线程发起调用， 由nio的线程来执行连接
                .connect(new InetSocketAddress("localhost", 8080)) // 1s后才能把连接建立好
                .sync()
                // 无阻塞向下获取 channel
                .channel();
        System.out.println(channel);
        System.out.println("");
    }
}

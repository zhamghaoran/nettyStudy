package netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        // 创建启动器类
        // 带有future 和promise 名字的类型都是要和异步方法配套使用的, 用来正确处理结果
        ChannelFuture channelFuture = new Bootstrap()
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
                .connect(new InetSocketAddress("localhost", 8080)); // 1s后才能把连接建立好;
        // 1.使用 sync方法同步处理结果
        channelFuture.sync(); // 阻塞住,当前线程，直到NIO线程连接建立完毕
        Channel channel = channelFuture.channel();
        // 2.使用addListener (回调对象)方法异步处理结果
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            // 在NIO连接建立好了以后会调用operationComplete
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel1 = future.channel();

            }
        });
        System.out.println(channel);
        System.out.println("");
    }
}

package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class Server {
    public static void main(String[] args) {
        // 0.启动器, 负责组装netty组件，协调工作
        ChannelFuture channelFuture = new ServerBootstrap()
                .group(new NioEventLoopGroup()) // 1  (selector,thread) = eventLoop, group 组
                .channel(NioServerSocketChannel.class) // 2. 选择服务器的serverSocketChannel的实现
                // boss负责处理连接，child负责处理读写 决定了worker(child) 能执行哪些操作(handler)
                .childHandler(
                        // channel代表和客户端进行数据读写的通道,Initializer 初始oo化，负责添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                            protected void initChannel(NioSocketChannel ch) {
                                // 添加具体的解码器
                                ch.pipeline().addLast(new StringDecoder()); // 5 将ByteBuf类型转化成字符串
                                ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() { // 6我们自己的业务处理类
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
                                        // 打印上一步转化好的字符串
                                        System.out.println(msg);
                                    }
                                });
                            }
                        })
                .bind(8080);// 绑定端口
    }
}

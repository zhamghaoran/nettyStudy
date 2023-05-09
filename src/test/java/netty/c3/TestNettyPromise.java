package netty.c3;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class TestNettyPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1. 准备EventLoop对象
        EventLoop next = new NioEventLoopGroup().next();
        // 2.可以主动创建这个promise对象, 结果容器
        DefaultPromise<Integer> promise = new DefaultPromise<>(next);

        new Thread(() -> {
            // 任意一个线程执行计算
            log.debug(" 开始计算");
            try {
                Thread.sleep(1000);
                // 执行完毕向promise 填充结果
                promise.setSuccess(80);
            } catch (InterruptedException e) {
                promise.setFailure(e);
                throw new RuntimeException(e);
            }

        }).start();
        // 设置一个接受结果的线程
        log.debug("等待结果。。。");
        log.debug("结果是:{}", promise.get());
    }
}

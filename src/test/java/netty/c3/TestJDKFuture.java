package netty.c3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class TestJDKFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. 关联jdk中的线程池使用
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // 提交任务
        Future<Integer> future = executorService.submit((Callable<Integer>) () -> {
            log.debug("执行计算");
            Thread.sleep(1000);
            return 50;
        });
        // 主线程就可以通过future来获得结果 但是get是阻塞方法
        log.debug("等待结果");
        Integer integer = future.get();
        log.debug("结果是{}", integer);

    }
}

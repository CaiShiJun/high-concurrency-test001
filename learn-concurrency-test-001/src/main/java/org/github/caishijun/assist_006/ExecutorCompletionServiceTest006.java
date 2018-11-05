package org.github.caishijun.assist_006;

import java.util.concurrent.*;

/**
 * ExecutorCompletionService分析及使用
 * 当我们通过Executor提交一组并发执行的任务，并且希望在每一个任务完成后能立即得到结果，有两种方式可以采取：
 *
 * 方式二：
 *
 * 第一种方式显得比较繁琐，通过使用ExecutorCompletionService，则可以达到代码最简化的效果。
 *
 * 来源网址：https://www.cnblogs.com/shamo89/p/6769017.html
 */
public class ExecutorCompletionServiceTest006 {

    static class Task implements Callable<String> {
        private int i;

        public Task(int i) {
            this.i = i;
        }

        @Override
        public String call() throws Exception {
            Thread.sleep(10000);
            return Thread.currentThread().getName() + "执行完任务：" + i;
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        testExecutorCompletionService();
    }

    private static void testExecutorCompletionService() throws InterruptedException, ExecutionException{
        int numThread = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numThread);
        CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);
        for (int i = 0; i < numThread; i++) {
            completionService.submit(new ExecutorCompletionServiceTest006.Task(i));
        }
        for (int i = 0; i < numThread; i++) {
            System.out.println(completionService.take().get());
        }
        executorService.shutdown();
    }
}
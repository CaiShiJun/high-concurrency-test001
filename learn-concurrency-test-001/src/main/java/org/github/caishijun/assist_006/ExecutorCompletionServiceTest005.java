package org.github.caishijun.assist_006;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * ExecutorCompletionService分析及使用
 * 当我们通过Executor提交一组并发执行的任务，并且希望在每一个任务完成后能立即得到结果，有两种方式可以采取：
 *
 * 方式一：
 *
 * 通过一个list来保存一组future，然后在循环中轮训这组future,直到每个future都已完成。如果我们不希望出现因为排在前面的任务阻塞导致后面先完成的任务的结果没有及时获取的情况，那么在调用get方式时，需要将超时时间设置为0
 *
 * 来源网址：https://www.cnblogs.com/shamo89/p/6769017.html
 */
public class ExecutorCompletionServiceTest005 {

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

    public static void main(String[] args) {
        testUseFuture();
    }

    private static void testUseFuture() {
        int numThread = 5;
        ExecutorService executor = Executors.newFixedThreadPool(numThread);

        List<Future<String>> futureList = new ArrayList<Future<String>>();
        for (int i = 0; i < numThread; i++) {
            Future<String> future = executor
                    .submit(new ExecutorCompletionServiceTest005.Task(i));
            futureList.add(future);
        }

        while (numThread > 0) {
            for (Future<String> future : futureList) {
                String result = null;
                try {
                    result = future.get(0, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    // 超时异常直接忽略
                }
                if (null != result) {
                    futureList.remove(future);
                    numThread--;
                    System.out.println(result);
                    // 此处必须break，否则会抛出并发修改异常。（也可以通过将futureList声明为CopyOnWriteArrayList类型解决）
                    break;
                }
            }
        }
    }
}

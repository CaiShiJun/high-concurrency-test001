package org.github.caishijun.deadlock_001.threadpooldeadlock_c;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 从 FixedThreadPool 线程池获取的 task 中，不要再次使用该 FixedThreadPool 线程池中的线程。再次尝试调用 FixedThreadPool 中的线程会因线程池中的线程用尽导致 task 中的调用 FixedThreadPool 线程的代码因无法获取线程池 FixedThreadPool 中的线程而导致死锁。
 */
public class FixedThreadPoolDeadLockTest001 {

    //创建一个最大线程数为 3 的 FixedThreadPool 线程池
    static ExecutorService executor = Executors.newFixedThreadPool(3);

    static volatile int num = 0;

    public static void main(final String[] arguments) throws InterruptedException {

        executor.submit(new Task());
        System.out.println("public static void main 向线程池提交第" + ++num + "个线程");
        executor.submit(new Task());
        System.out.println("public static void main 向线程池提交第" + ++num + "个线程");
        executor.submit(new Task());
        System.out.println("public static void main 向线程池提交第" + ++num + "个线程");

        //executor.shutdown();
    }

    static class Task implements Runnable {

        public void run() {

            executor.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("进入new Task().run()方法中的 executor.submit(new Runnable() 线程");
                }
            });


            try {
                TimeUnit.DAYS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


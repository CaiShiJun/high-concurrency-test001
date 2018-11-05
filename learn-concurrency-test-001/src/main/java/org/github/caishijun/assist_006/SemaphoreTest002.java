package org.github.caishijun.assist_006;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphore 一个计数信号量。从概念上讲，信号量维护了一个许可集。如有必要，在许可可用前会阻塞每一个acquire()，然后再获取该许可。每个release() 添加一个许可，从而可能释放一个正在阻塞的获取者。但是，不使用实际的许可对象，Semaphore 只对可用许可的号码进行计数，并采取相应的行动。
 *
 * Semaphore通常用于限制可以访问某些资源（物理或逻辑的）的线程数目。
 *
 * 来源网址：https://www.cnblogs.com/shamo89/p/6710103.html
 */
public class SemaphoreTest002 {

    public static void main(String[] args) throws IOException {

        ExecutorService executorService = Executors.newCachedThreadPool();
        // 配置同时只能有两个线程访问
        Semaphore semaphore = new Semaphore(2);
        // 开启10个线程
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Test002TaskRun(semaphore));
        }
        // 关闭线程池
        executorService.shutdown();
        while (true) {
            System.out.println(Thread.currentThread().getName()+"semaphore这个信号量中当前可用的许可数："+semaphore.availablePermits());
            System.out.println(Thread.currentThread().getName()+"正在等待获取的线程的估计数目："+semaphore.getQueueLength());
            System.out.println(Thread.currentThread().getName()+"查询是否有线程正在等待获取："+semaphore.hasQueuedThreads());
            if (semaphore.getQueueLength() == 0) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Test002TaskRun implements Runnable{

    private Semaphore semaphore;

    public Test002TaskRun(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName()+"begin...");
            semaphore.acquire();// 从此信号量获取一个许可，在提供一个许可前一直将线程阻塞，否则线程被中断
            Thread.sleep(5000);// 模拟业务耗时
            semaphore.release();// 释放一个许可，将其返回给信号量
            System.out.println(Thread.currentThread().getName()+"end...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
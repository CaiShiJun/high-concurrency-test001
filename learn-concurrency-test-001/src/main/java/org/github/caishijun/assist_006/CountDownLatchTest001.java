package org.github.caishijun.assist_006;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountDownlatch 是一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
 * CountDownlatch 本身而言是Java并发包中非常有用的一个类，它可以让某些任务完成以后再继续运行下面的内容，每个任务本身执行完毕后让计数器减一，直到计数器清零后，以下的内容才可以继续运行，否则将阻塞等待。
 *
 * CountDownLatch有以下基本方法：
 *
 * 1）await(),阻塞等待，直到计数器清零
 *
 * 2）await(int timeout, TimeUnit unit),使线程阻塞，除非被中断或者超过等待的最大时间。如果达到计数器清零，则await返回true，如果等待超过了最大的等待时间，则返回false。
 *
 * 3）countDown(),计数器减一，当计数器清零时，await的线程被唤醒，线程继续执行
 *
 * 4）getCount()，获取当前计数器的大小
 *
 * 来源网址：https://www.cnblogs.com/shamo89/p/6709548.html
 */
public class CountDownLatchTest001 {

    public static void main(String[] args) throws IOException {
        // 模拟数据库资源
        CopyOnWriteArrayList<Integer> ziyuan = new CopyOnWriteArrayList<Integer>();
        for (int i = 1; i < 10; i++) {
            ziyuan.add(i);
        }

        ExecutorService executorService = Executors.newCachedThreadPool();

        // 标记一共有ziyuan.size()/3个线程需要等待
        CountDownLatch cLatch = new CountDownLatch(ziyuan.size()/3);

        // 开启三个线程
        for (int i = 0; i < ziyuan.size()/3; i++) {
            executorService.execute(new Test001TaskRun(ziyuan,cLatch, 3*i));
        }

        // 关闭线程池
        executorService.shutdown();

        // 主线程等待
        try {
            cLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("我是主线程要操作的内容，必须确保我在其它三个线程执行完后最后打印。。。");
    }
}

class Test001TaskRun implements Runnable{

    private CopyOnWriteArrayList<Integer> ziyuan;
    private CountDownLatch cLatch;
    private int c;

    public Test001TaskRun(CopyOnWriteArrayList<Integer> ziyuan, CountDownLatch cLatch, int c) {
        this.ziyuan = ziyuan;
        this.cLatch = cLatch;
        this.c = c;
    }

    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName()+"begin...");
        for (int i = c; i < ziyuan.size()/3+c; i++) {
            System.out.println(Thread.currentThread().getName()+"//"+ziyuan.get(i)+"//CountDownLatch:"+cLatch.getCount());
            try {
                Thread.sleep(new Random().nextInt(4)*1000);// 模拟业务耗时
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName()+"end...");
        // 每个业务线程执行完后会减去1
        cLatch.countDown();
    }

}

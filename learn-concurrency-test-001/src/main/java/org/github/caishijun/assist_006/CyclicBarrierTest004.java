package org.github.caishijun.assist_006;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CyclicBarrier 一个同步辅助类，它允许一组线程互相等待，直到到达某个公共屏障点 (common barrier point)。在涉及一组固定大小的线程的程序中，这些线程必须不时地互相等待，此时 CyclicBarrier 很有用。因为该 barrier 在释放等待线程后可以重用，所以称它为循环的barrier。
 *
 * CyclicBarrier 支持一个可选的Runnable命令，在一组线程中的最后一个线程到达之后（但在释放所有线程之前），该命令只在每个屏障点运行一次。若在继续所有参与线程之前更新共享状态，此屏障操作很有用。
 *
 * 来源网址：https://www.cnblogs.com/shamo89/p/6713275.html
 */
public class CyclicBarrierTest004 {

    public static void main(String[] args) {
        int threadNum = 3;
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        CyclicBarrier barrier = new CyclicBarrier(threadNum);
        for (int i = 0; i < threadNum; i++) {
            executor.submit(new Thread(new Test004TaskRun(barrier)));
        }
        executor.shutdown();
        while (true) {
            System.out.println(Thread.currentThread().getName()+" 当前在屏障处等待的参与者数目:"+barrier.getNumberWaiting());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Test004TaskRun implements Runnable{

    private CyclicBarrier cyclicBarrier;

    public Test004TaskRun(CyclicBarrier cyclicBarrier) {
        super();
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        try {
            int m = 1000 * (new Random()).nextInt(8);
            Thread.sleep(m);// 模拟准备业务耗时
            System.out.println(Thread.currentThread().getName() + " 准备好了,准备耗时："+m+"毫秒");
            // barrier的await方法，在所有参与者都已经在此 barrier 上调用 await 方法之前，将一直等待。
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " 开始处理核心业务，当前时间"+System.currentTimeMillis());
    }

}
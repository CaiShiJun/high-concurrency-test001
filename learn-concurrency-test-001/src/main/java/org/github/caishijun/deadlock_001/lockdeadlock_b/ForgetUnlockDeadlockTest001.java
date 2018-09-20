package org.github.caishijun.deadlock_001.lockdeadlock_b;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 参考资料：https://www.yiibai.com/java_concurrency/concurrency_lock.html
 */
public class ForgetUnlockDeadlockTest001 {

    public static void main(String args[]) {
        PrintDemo PD = new PrintDemo();

        ThreadDemo t1 = new ThreadDemo( "Thread - 1 ", PD );
        ThreadDemo t2 = new ThreadDemo( "Thread - 2 ", PD );
        ThreadDemo t3 = new ThreadDemo( "Thread - 3 ", PD );
        ThreadDemo t4 = new ThreadDemo( "Thread - 4 ", PD );

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

}

class PrintDemo {

    private final Lock queueLock = new ReentrantLock();

    public void print() {
        queueLock.lock();
        try {
            Long duration = (long) (Math.random() * 10000);
            System.out.println(Thread.currentThread().getName()
                + "  Time Taken " + (duration / 1000) + " seconds.");
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.printf("%s printed the document successfully.\n", Thread.currentThread().getName());

            /**
             * 注意这里没有执行 queueLock.unlock() 方法，导致锁一直没有被释放，所以只有一个线程可以进入该方法。因为 Lock 接口的 lock() 和 unlock() 可以不在同一个方法中（这种情况有可能会导致 unlock() 没有被执行，导致死锁），所以必须检查 unlock 一定会被执行。
             */
            //queueLock.unlock();
        }
    }
}

class ThreadDemo extends Thread {

    PrintDemo  printDemo;

    ThreadDemo( String name,  PrintDemo printDemo) {
        super(name);
        this.printDemo = printDemo;
    }

    @Override
    public void run() {
        System.out.printf("%s starts printing a document\n", Thread.currentThread().getName());
        printDemo.print();
    }
}
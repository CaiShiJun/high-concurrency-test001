package org.github.caishijun.deadlock_001.synchronizeddeadlock_a;

/**
 * 参考资料：https://blog.csdn.net/ysyswywl2/article/details/50801536
 */
public class SynchronizedDeadlockTest001 {

    /**
     * A锁
     */
    private static String A = "A";

    /**
     * B锁
     */
    private static String B = "B";

    public void deadLock() {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (A) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (B) {
                        System.out.println("thread1...");
                    }
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (B) {
                    synchronized (A) {
                        System.out.println("thread2...");
                    }
                }
            }
        });

        t1.start();
        t2.start();
    }


    public static void main(String[] args) {
        new SynchronizedDeadlockTest001().deadLock();
    }

}

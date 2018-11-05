package org.github.caishijun.atomic_003;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 参考：https://www.yiibai.com/java_concurrency/concurrency_atomiclong.html
 */
public class AtomicLongTest001 {

    static class Counter {
        private AtomicLong c = new AtomicLong(0);

        public void increment() {
            c.getAndIncrement();
        }

        public long value() {
            return c.get();
        }
    }
    public static void main(final String[] arguments) throws InterruptedException {
        final Counter counter = new Counter();
        //1000 threads
        for(int i = 0; i < 1000 ; i++) {
            new Thread(new Runnable() {
                public void run() {
                    counter.increment();
                }
            }).start();
        }
        Thread.sleep(6000);

        System.out.println("Final number (should be 1000): " + counter.value());
    }
}
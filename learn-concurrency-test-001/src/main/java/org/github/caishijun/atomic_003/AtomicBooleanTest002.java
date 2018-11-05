package org.github.caishijun.atomic_003;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 参考：https://www.yiibai.com/java_concurrency/concurrency_atomicboolean.html
 */
public class AtomicBooleanTest002 {

    public static void main(final String[] arguments) throws InterruptedException {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        new Thread("Thread 1") {
            public void run() {
                while(true){
                    System.out.println(Thread.currentThread().getName()
                        +" Waiting for Thread 2 to set Atomic variable to true. Current value is "
                        + atomicBoolean.get());

                    //线程 Thread 2 可能会在该位置执行 atomicBoolean.set(true); 导致上方的 atomicBoolean.get() 获取到的值为 false ， 下方代码 atomicBoolean.compareAndSet( 获取到的值为 true ），出现数据不一致的情况。

                    if(atomicBoolean.compareAndSet(true, false)) {
                        System.out.println("Done!");
                        break;
                    }
                }};
        }.start();

        new Thread("Thread 2") {
            public void run() {
                System.out.println(Thread.currentThread().getName() + ", Atomic Variable: " +atomicBoolean.get());
                System.out.println(Thread.currentThread().getName() +" is setting the variable to true ");
                atomicBoolean.set(true);
                System.out.println(Thread.currentThread().getName() + ", Atomic Variable: " +atomicBoolean.get());
            };
        }.start();
    }
}

package org.github.caishijun.queue_004;

/**
 * SynchronousQueue是这样一种阻塞队列，其中每个put必须等待一个take，反之亦然。同步队列没有任何内部容量，甚至连一个队列的容量都没有。不能在同步队列上进行peek，因为仅在试图要取得元素时，该元素才存在，除非另一个线程试图移除某个元素，否则也不能（使用任何方法）添加元素，也不能迭代队列，因为其中没有元素可用于迭代。队列的头是尝试添加到队列中的首个已排队线程元素，如果没有已排队线程，则不添加元素并且头为 null。
 * 对于其他Collection方法（例如 contains），SynchronousQueue作为一个空集合，此队列不允许 null 元素。
 * 同步队列类似于CSP和Ada中使用的rendezvous信道。它非常适合于传递性设计，在这种设计中，在一个线程中运行的对象要将某些信息、事件或任务传递给在另一个线程中运行的对象，它就必须与该对象同步。
 * 对于正在等待的生产者和使用者线程而言，此类支持可选的公平排序策略，默认情况下不保证这种排序。
 * 但是，使用公平设置为true所构造的队列可保证线程以FIFO的顺序进行访问。公平通常会降低吞吐量，但是可以减小可变性并避免得不到服务。
 *
 * 来源网址：https://www.cnblogs.com/shamo89/p/7363339.html
 */

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * SynchronousQueue 的使用场景 ==== 线程间共享元素
 * 假设有两个线程，一个生产者和一个消费者，当生产者设置一个共享变量的值时，我们希望向消费者线程
 * 发出这个信号，然后消费者线程将从共享变量取值。
 *
 * 来源网址：https://www.cnblogs.com/shamo89/p/7363339.html
 */
public class SynchronousQueueTest006 {

    /**
     * 利用AtomicInteger+CountDownLatch实现
     */
    @Test
    public void doingByCountDownLatch(){
        ExecutorService executor = Executors.newFixedThreadPool(2);
        AtomicInteger sharedState = new AtomicInteger();// 共享变量值
        //  协调这两个线程，以防止情况当消费者访问共享变量值
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // 生产商将设置一个随机整数到sharedstate变量，并countdown()方法，
        // 信号给消费者，它可以从sharedstate取一个值
        Runnable producer = () -> {     // 这是java8的匿名内部类的新写法
            Integer producedElement = ThreadLocalRandom.current().nextInt();
            sharedState.set(producedElement);
            System.out.println("生产者给变量设值："+producedElement);
            countDownLatch.countDown();
        };

        // 消费者会等待countdownlatch执行到await()方法，获取许可后，再从生产者里获取变量sharedstate值
        Runnable consumer = () -> {
            try {
                countDownLatch.await();
                Integer consumedElement = sharedState.get();
                System.out.println("消费者获取到变量："+consumedElement);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        executor.execute(producer);
        executor.execute(consumer);
        try {
            executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        assertEquals(countDownLatch.getCount(), 0);
    }

    /**
     * 仅使用SynchronousQueue就可以实现
     */
    @Test
    public void doingBySynchronousQueue(){
        ExecutorService executor = Executors.newFixedThreadPool(2);
        SynchronousQueue<Integer> queue = new SynchronousQueue<>();

        // 生产者
        Runnable producer = () -> {
            Integer producedElement = ThreadLocalRandom.current().nextInt();
            try {
                queue.put(producedElement);
                System.out.println("生产者设值："+producedElement);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        // 消费者
        Runnable consumer = () -> {
            try {
                Integer consumedElement = queue.take();
                System.out.println("消费者取值："+consumedElement);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        executor.execute(producer);
        executor.execute(consumer);
        try {
            executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        assertEquals(queue.size(), 0);
    }
}

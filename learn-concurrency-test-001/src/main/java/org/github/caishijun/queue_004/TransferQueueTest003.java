package org.github.caishijun.queue_004;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
 * TransferQueue也具有SynchronousQueue的所有功能，但是TransferQueue的功能更强大。
 * TransferQueue同时也是一个阻塞队列，它具备阻塞队列的所有特性。
 *
 * 1.transfer(E e)若当前存在一个正在等待获取的消费者线程，即立刻将e移交之；否则将元素e插入到队列尾部，并且当前线程进入阻塞状态，直到有消费者线程取走该元素。
 * 2.tryTransfer(E e)若当前存在一个正在等待获取的消费者线程，则该方法会即刻转移e，并返回true;若不存在则返回false，但是并不会将e插入到队列中。这个方法不会阻塞当前线程，要么快速返回true，要么快速返回false。
 * 3.hasWaitingConsumer()和getWaitingConsumerCount()用来判断当前正在等待消费的消费者线程个数。
 * 4.tryTransfer(E e, long timeout, TimeUnit unit) 若当前存在一个正在等待获取的消费者线程，会立即传输给它; 否则将元素e插入到队列尾部，并且等待被消费者线程获取消费掉。若在指定的时间内元素e无法被消费者线程获取，则返回false，同时该元素从队列中移除。
 *
 * 来源网址：https://www.cnblogs.com/wangzhongqiu/p/6441703.html
 */
public class TransferQueueTest003 {

    private static TransferQueue<String> queue = new LinkedTransferQueue<String>();

    public static void main(String[] args) throws Exception {

        new Productor(1).start();

        Thread.sleep(100);

        System.out.println("over.size=" + queue.size());

        //可以看到生产者线程会阻塞，因为调用transfer()的时候并没有消费者在等待获取数据。队列长度变成了1，说明元素e没有移交成功的时候，会被插入到阻塞队列的尾部。
    }

    static class Productor extends Thread {
        private int id;

        public Productor(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                String result = "id=" + this.id;
                System.out.println("begin to produce." + result);
                queue.transfer(result);
                System.out.println("success to produce." + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

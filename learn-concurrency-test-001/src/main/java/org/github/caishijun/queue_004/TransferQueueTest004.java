package org.github.caishijun.queue_004;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

/**
 * 来源网址：https://www.cnblogs.com/wangzhongqiu/p/6441703.html
 */
public class TransferQueueTest004 {

    private static TransferQueue<String> queue = new LinkedTransferQueue<String>();

    public static void main(String[] args) throws Exception {

        new Productor(1).start();

        Thread.sleep(100);

        System.out.println("over.size=" + queue.size());//1

        Thread.sleep(1500);

        System.out.println("over.size=" + queue.size());//0

        //第一次还没到指定的时间，元素被插入到队列中了，所有队列长度是1；第二次指定的时间片耗尽，元素从队列中移除了，所以队列长度是0。
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
                queue.tryTransfer(result, 1, TimeUnit.SECONDS);
                System.out.println("success to produce." + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

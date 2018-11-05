package org.github.caishijun.assist_006;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exchanger可以在两个线程之间交换数据，只能是2个线程，他不支持更多的线程之间互换数据。当线程A调用Exchange对象的exchange()方法后，他会陷入阻塞状态，直到线程B也调用了exchange()方法，然后以线程安全的方式交换数据，之后线程A和B继续运行。
 *
 * 来源网址：https://www.cnblogs.com/shamo89/p/6718286.html
 */
public class ExchangerTest003 {

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        String data = "";
        executorService.execute(new Test003Procuder(data, exchanger));
        executorService.execute(new Test003Consumer(data, exchanger));
    }

}

class Test003Procuder implements Runnable{

    private String data;
    private Exchanger<String> exchanger;
    public Test003Procuder(String data,Exchanger<String> exchanger) {
        this.data = data;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {

        try {
            for (int i = 0; i < 5; i++) {
                data = new Random().nextInt(1000)+"";
                System.out.println("producer"+i+" "+data);
                Thread.sleep(new Random().nextInt(5)*1000);
                exchanger.exchange(data);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Test003Consumer implements Runnable{

    private String data;
    private Exchanger<String> exchanger;
    public Test003Consumer(String data,Exchanger<String> exchanger) {
        this.data = data;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {

        try {
            for (int i = 0; i < 5; i++) {
                data = exchanger.exchange(data);
                Thread.sleep(new Random().nextInt(5)*1000);
                System.out.println("Consumer"+i+" "+data);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
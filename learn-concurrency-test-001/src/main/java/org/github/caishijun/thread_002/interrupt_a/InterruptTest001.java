package org.github.caishijun.thread_002.interrupt_a;

import java.util.concurrent.TimeUnit;

/**
 * java interrupt()方法只是设置线程的中断标记，当对处于阻塞状态的线程调用interrupt方法时（处于阻塞状态的线程是调用sleep, wait, join 的线程)，会抛出InterruptException异常，而这个异常会清除中断标记。因此，根据这两个思路，不同的run()方法设计，会导致不同的结果，下面是示例，并说明了运行了结果和原因。
 */
public class InterruptTest001 {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(run4);
        t.start();
        TimeUnit.SECONDS.sleep(2);
        t.interrupt();
    }

    /** 不同的run方法设计 */

    private static Runnable run1 = new Runnable() {
        //这种设计比较好,当调用阻塞操作时,会因为抛出异常退出,当不调用阻塞操作时,会因为检查中断状态而退出
        @Override
        public void run() {
            try{
                while(!Thread.interrupted()){
                    // System.out.println("sleep 5s");
                    //Thread.sleep(5000);接收到中断信号时,由于while循环判断不成立退出,不抛出异常
                }
                System.out.println("Exit normal");
            }catch(Exception e){
                System.out.println("interrupted");
            }
        }
    };

    private static Runnable run2 = new Runnable() {
        @Override
        public void run() {
            try{
                while(!Thread.interrupted()){
                    // System.out.println("sleep 5s");
                    Thread.sleep(5000);//接收到中断信号时，由于抛出异常退出，模拟耗时操作
                }
                System.out.println("Exit normal");
            }catch(Exception e){
                System.out.println("interrupted and exit");
            }
        }
    };

    private static Runnable run3 = new Runnable() {
        @Override
        public void run() {
            while(!Thread.interrupted()){
                //接收到中断信号时,由于while循环判断不成立退出
            }
            System.out.println("interrupt normal and exit 2");
        }
    };


    private static Runnable run4 = new Runnable() {
        //此种设计不好
        @Override
        public void run() {
            while(!Thread.interrupted()){
                try{
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("running-"+System.currentTimeMillis());
                    //接收到中断信号，捕获异常并清除中断状态，所以不退出,所以这种不是良好的设计方式,如果想要退出,需要在catch语句中Thread.currentThread().interrupt();
                }catch(Exception e){
                    System.out.println("Interrupte and clear interrupt status, so run continue");
                }
            }
            System.out.println("exit normal 3");
        }
    };

    private static Runnable run5 = new Runnable() {
        @Override
        public void run() {
            double d = 1;
            while(!Thread.interrupted()){
                while(d<3000){
                    d = d + (Math.PI+Math.E)/d;
                    System.out.println(d+ " running");
                    //接收到中断信号时,不会中断正在运行的操作,只有当操作完成后,检查中断状态时会退出
                }
            }
            System.out.println("Exit "+d);
        }
    };

}

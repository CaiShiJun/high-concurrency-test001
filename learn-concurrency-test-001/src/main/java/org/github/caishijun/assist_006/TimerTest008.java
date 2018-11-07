package org.github.caishijun.assist_006;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Timer类的主要作用是设置计划任务，但封装任务的类却是TimerTask类。执行计划任务的代码要放入TimerTask的子类中，因为TimerTask是一个抽象类。
 *
 * 来源网址：https://www.cnblogs.com/shamo89/p/7280142.html
 */

/**
 * 测试类
 */
public class TimerTest008 {
    public static void main(String[] args) {
        Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 3);
        System.out.println("1 "+new Date().toString());
        timer.schedule(new MyTask(), calendar.getTime());
    }
}

/**
 * 任务类
 */
class MyTask extends TimerTask {
    @Override
    public void run() {
        System.out.println("get last info..."+new Date().toString());
    }
}
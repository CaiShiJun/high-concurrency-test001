package org.github.caishijun.assist_006;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * FutureTask 是一种可以取消的异步的计算任务。它的计算是通过Callable实现的，它等价于可以携带结果的Runnable，并且有三个状态：等待、运行和完成。完成包括所有计算以任意的方式结束，包括正常结束、取消和异常。
 * FutureTask 包装器是一种非常便利的机制，同时实现了Future和Runnable接口。
 *
 * 1. FutureTask执行多任务计算的使用场景
 * 利用FutureTask和ExecutorService，可以用多线程的方式提交计算任务，主线程继续执行其他任务，当主线程需要子线程的计算结果时，在异步获取子线程的执行结果。
 *
 * 来源网址：https://www.cnblogs.com/shamo89/p/6773877.html
 */
public class FutureTaskTest007 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 创建任务集合
        List<FutureTask<Integer>> taskList = new ArrayList<FutureTask<Integer>>();
        // 创建线程池
        ExecutorService exec = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            FutureTask<Integer> ft = new FutureTask<>(new Test007ComputeTask(i, ""+i));
            taskList.add(ft);
            // 提交给线程池执行任务，也可以通过exec.invokeAll(taskList)一次性提交所有任务;
            exec.submit(ft);
        }

        System.out.println("所有计算任务提交完毕, 主线程接着干其他事情！");

        // 开始统计各计算线程计算结果
        Integer totalResult = 0;
        for (FutureTask<Integer> ft : taskList) {
            totalResult += ft.get();
        }

        // 关闭线程池
        exec.shutdown();
        System.out.println("多任务计算后的总结果是:" + totalResult);
    }

}

class Test007ComputeTask implements Callable<Integer> {

    private Integer result = 0;
    private String taskName = "";

    public String getTaskName(){
        return this.taskName;
    }

    public Test007ComputeTask(Integer iniResult, String taskName){
        result = iniResult;
        this.taskName = taskName;
        System.out.println("生成子线程计算任务: "+taskName);
    }

    @Override
    public Integer call() throws Exception {
        for (int i = 0; i < 100; i++) {
            result =+ i;
        }
        // 休眠5秒钟，观察主线程行为，预期的结果是主线程会继续执行，到要取得FutureTask的结果是等待直至完成。
        Thread.sleep(5000);
        System.out.println("子线程计算任务: "+taskName+" 执行完成!");
        return result;
    }

}
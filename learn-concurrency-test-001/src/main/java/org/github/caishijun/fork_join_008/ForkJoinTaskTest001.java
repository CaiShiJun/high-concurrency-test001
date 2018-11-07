package org.github.caishijun.fork_join_008;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * 1、ForkJoinTask：使用该框架，需要创建一个ForkJoin任务，它提供在任务中执行fork和join操作的机制。一般情况下，我们并不需要直接继承ForkJoinTask类，只需要继承它的子类，它的子类有两个：
 *
 * a、RecursiveAction:用于没有返回结果的任务。
 *
 * b、RecursiveTask:用于有返回结果的任务。
 *
 * 2、ForkJoinPool：任务ForkJoinTask需要通过ForkJoinPool来执行。
 *
 * 来源网址：https://www.cnblogs.com/shamo89/p/9772358.html
 */
public class ForkJoinTaskTest001 {
}

class CountTask extends RecursiveTask<Integer> {
    private static final long serialVersionUID = 1L;
    // 阈值
    private static final int THRESHOLD = 2;
    private int start;
    private int end;

    public CountTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        // 判断任务是否足够小
        boolean canCompute = (end - start) <= THRESHOLD;
        if (canCompute) {
            // 如果小于阈值，就进行运算
            for (int i = start; i <= end; i++) {
                sum += i;
            }
            System.out.println(Thread.currentThread().getName()+" A sum:"+sum);
        } else {
            // 如果大于阈值，就再进行任务拆分
            int middle = (start + end) / 2;
            System.out.println(Thread.currentThread().getName()+" start:"+start+",middle:"+middle+",end:"+end);
            CountTask leftTask = new CountTask(start, middle);
            CountTask rightTask = new CountTask(middle + 1, end);
            // 执行子任务
            leftTask.fork();
            rightTask.fork();
            // 等待子任务执行完，并得到执行结果
            int leftResult = leftTask.join();
            int rightResult = rightTask.join();
            // 合并子任务
            sum = leftResult + rightResult;
            System.out.println(Thread.currentThread().getName()+" B sum:"+sum);
        }
        return sum;
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();// 这边也可以指定一个最大线程数
        CountTask task = new CountTask(1, 10);
        // 执行一个任务
        Future<Integer> result = forkJoinPool.submit(task);
        try {
            System.out.println(result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
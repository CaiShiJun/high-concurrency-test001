package org.github.caishijun.fork_join_008;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 在ForkJoin框架中声明的compute()方法不允许在运行是抛出异常，因为这个方法的实现没有包含任何throws申明。因此，必须包含必须的代码来处理相关的异常。
 *
 * 来源网址：https://www.cnblogs.com/shamo89/p/9774810.html
 */
public class ForkJoinTaskTest003 {

    public static void main(String[] args) {
        int array[] = new int[100];
        Test003Task task = new Test003Task(array,1,100);
        ForkJoinPool pool = new ForkJoinPool();
        pool.execute(task);
        pool.shutdown();
        try {
            pool.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(task.isCompletedAbnormally()){
            System.out.print("Main:An exception has occured\n");
            System.out.printf("Main:%s\n",task.getException());

        }
    }
}

class Test003Task extends RecursiveTask<Integer> {

    private static final long serialVersionUID = 1L;
    private int array[];
    private int start,end;
    public Test003Task(int array[],int start,int end){
        this.array = array;
        this.start = start;
        this.end = end;
    }
    protected Integer compute() {
        System.out.printf("Task:Start from %d to %d\n",start,end);
        if(end-start<10){
            if((start<3)&&(3<end)){
                throw new RuntimeException("This task throws an"+"Exception:Task from "+start+"to "+end);
            }
            else{
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            int mid  = (end+start)/2;
            Test003Task task1 = new Test003Task(array,start,mid);
            Test003Task task2 = new Test003Task(array,start,mid);
            invokeAll(task1,task2);
        }
        System.out.printf("Task:End form %d to %d \n",start,end);
        return 0;
    }
}
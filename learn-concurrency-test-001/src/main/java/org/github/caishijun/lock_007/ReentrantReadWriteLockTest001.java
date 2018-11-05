package org.github.caishijun.lock_007;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 利用ReentrantReadWriteLock模拟数据的读写分离
 *
 * 来源网址：https://www.cnblogs.com/shamo89/p/7244521.html
 */
public class ReentrantReadWriteLockTest001 {

    public static void main(String[] args) {
        List<String> dataList = new ArrayList<>();
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
//        DataQueue dataQueue = ;
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            exec.execute(new DataQueue(new Random().nextInt(10), dataList, rwLock));
        }
        exec.shutdown();
    }
}

/**
 * 模拟数据库表 读数据 写数据
 */
class DataQueue implements Runnable {

    private int randomNum;// 随机数
    private List<String> dataList;// 存放数据的集合
    private ReentrantReadWriteLock rwLock;// 读写锁

    public DataQueue(int randomNum, List<String> dataList, ReentrantReadWriteLock rwLock) {
        super();
        this.randomNum = randomNum;
        this.dataList = dataList;
        this.rwLock = rwLock;
    }

    public void getData(){
        rwLock.readLock().lock();// 开启读锁  只能允许读的线程访问
        System.out.println("read thread "+Thread.currentThread().getName()+" begin read data");
        StringBuffer sb = new StringBuffer();
        for (String data : dataList) {
            sb.append(data+" ");
        }
        System.out.println("read thread "+Thread.currentThread().getName()+" read data:"+sb.toString());
        System.out.println("read thread "+Thread.currentThread().getName()+" end read data");
        rwLock.readLock().unlock();// 释放读锁
    }

    public void setData(){
        rwLock.writeLock().lock();// 开启写锁  其它线程不管是读还是写都不能访问
        System.out.println("write thread "+Thread.currentThread().getName()+" begin write data");
        String data = UUID.randomUUID().toString();
        dataList.add(data);
        System.out.println("write thread "+Thread.currentThread().getName()+" write data:"+data);
        System.out.println("write thread "+Thread.currentThread().getName()+" end write data");
        rwLock.writeLock().unlock();// 释放读锁
    }

    @Override
    public void run() {
        if (randomNum%2 == 0) {
            getData();
        } else {
            setData();
        }
    }
}
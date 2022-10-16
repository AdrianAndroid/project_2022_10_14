package com.example.project_2022_10_14;

import java.util.Date;
import java.util.concurrent.Semaphore;

/**
 * https://www.runoob.com/java/thread-deadlock.html
 * <p>产生死锁的四个必要条件:
 * <p>1、互斥使用，即当资源被一个线程使用(占有)时，别的线程不能使用</p>
 * <p>2、不可抢占，资源请求者不能强制从资源占有者手中夺取资源，资源只能由资源占有者主动释放。</p>
 * <p>3、请求和保持，即当资源请求者在请求其他的资源的同时保持对原有资源的占有。</p>
 * <p>4、循环等待，即存在一个等待队列：P1占有P2的资源，P2占有P3的资源，P3占有P1的资源。这样就形成了一个等待环路。</p>
 *
 * <p>运行结果</p>
 * <p>Fri Oct 14 20:42:12 CST 2022 LockA 开始执行</p>
 * <p>Fri Oct 14 20:42:12 CST 2022 LockB 开始执行</p>
 * <p>Fri Oct 14 20:42:12 CST 2022 LockB 锁住obj2</p>
 * <p>Fri Oct 14 20:42:12 CST 2022 LockA 锁住obj1</p>
 * <p>|</p>
 */
public class LockTest {
    public static String obj1 = "obj1";
    public static final Semaphore a1 = new Semaphore(1);
    public static String obj2 = "obj2";
    public static final Semaphore a2 = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        LockA lockA = new LockA();
        new Thread(lockA).start();
        LockB lockB = new LockB();
        new Thread(lockB).start();
    }

    static class LockA implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println(new Date() + " LockA 开始执行");
                while (true) {
                    synchronized (obj1) {
                        System.out.println(new Date() + " LockA 锁住obj1 开始等待3秒");
                        Thread.sleep(3000); // 此处等待是给B能锁住机会
                        synchronized (obj2) {
                            System.out.println(new Date() + " LockA 锁住obj2");
                            Thread.sleep(60 * 1000); //为测试,占用了就不放
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class LockB implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println(new Date() + " LockB 开始执行");
                while (true) {
                    synchronized (obj2) {
                        System.out.println(new Date() + " LockB 锁住obj2, 开始等待3秒");
                        Thread.sleep(3000); // 此处等待是给A能锁住的机会
                        synchronized (obj1) {
                            System.out.println(new Date() + " LockB锁住obj1");
                            Thread.sleep(60 * 1000); // 为测试,占用了就不放
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

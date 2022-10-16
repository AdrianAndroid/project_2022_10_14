package com.example.project_2022_10_14;

import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class UnLockTest {
    public static final Semaphore a1 = new Semaphore(1);
    public static final Semaphore a2 = new Semaphore(1);
    public static String obj1 = "obj1";
    public static String obj2 = "obj2";

    public static void main(String[] args) {
        LockA lockA = new LockA();
        new Thread(lockA).start();
        LockB lockB = new LockB();
        new Thread(lockB).start();
    }
}

class LockA implements Runnable {

    @Override
    public void run() {
        try {
            System.out.println(new Date() + " LockA 开始执行");
            while (true) {
                if (UnLockTest.a1.tryAcquire(1, TimeUnit.SECONDS)) {
                    System.out.println(new Date() + " LockA 锁住 obj1");
                    if (UnLockTest.a2.tryAcquire(1, TimeUnit.SECONDS)) {
                        System.out.println(new Date() + " LockA 锁住 obj2");
                        Thread.sleep(10 * 1000); // do something
                    } else {
                        System.out.println(new Date() + "LockA 锁 obj2 失败");
                    }
                } else {
                    System.out.println(new Date() + "LockA 锁 obj1 失败");
                }
                UnLockTest.a1.release(); // 释放
                UnLockTest.a2.release();
                Thread.sleep(1000); // 马上进行尝试，现实情况下do something是不确定的
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class LockB implements Runnable {

    @Override
    public void run() {
        try {
            System.out.println(new Date() + " LockB 开始执行");
            while (true) {
                if (UnLockTest.a2.tryAcquire(1, TimeUnit.SECONDS)) {
                    System.out.println(new Date() + " LockB 锁住 obj2");
                    if (UnLockTest.a1.tryAcquire(1, TimeUnit.SECONDS)) {
                        System.out.println(new Date() + " LockB 锁住 obj1");
                        Thread.sleep(10 * 1000); // do something
                    } else {
                        System.out.println(new Date() + "LockB 锁 obj1 失败");
                    }
                } else {
                    System.out.println(new Date() + "LockB 锁 obj2 失败");
                }
                UnLockTest.a1.release(); // 释放
                UnLockTest.a2.release();
                Thread.sleep(10 * 1000); // 这里只是为了演示，所以tryAcquire只用1秒，而且B要给A让出能执行的时间，否则两个永远是死锁
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


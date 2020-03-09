package com.jwj.c01;

/**
 * 死锁测试
 */
public class DeadLockDemo {
    private static String a="A";
    private static String b="B";

    public static void main(String[] args) {
        new DeadLockDemo().deadLock();
    }

    /**
     * t1获取了a锁；
     * t2获取了b锁；
     * t1尝试获得b锁失败，等待t2释放；
     * t2尝试获得a锁失败，等待t1释放；
     * 产生死锁。
     */
    private void deadLock(){
        Thread t1=new Thread(() -> {
            synchronized (a){
                System.out.println("t1 obtained a and t1 want b");
                try {
                    Thread.currentThread().sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (b){
                    System.out.println("t1 obtained a and b");
                }
            }
        });

        Thread t2=new Thread(() -> {
            synchronized (b){
                System.out.println("t2 obtained b and t2 want a");
                synchronized (a){
                    System.out.println("t2 obtained a and b");
                }
            }
        });

        t1.start();
        t2.start();
    }
}

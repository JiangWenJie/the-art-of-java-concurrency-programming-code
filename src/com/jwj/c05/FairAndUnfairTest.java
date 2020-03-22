package com.jwj.c05;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 书上的改良写法，为了防止Junit退出JVM，使用了CountDownLatch end，当所有线程结束后才会继续Test线程
 */
public class FairAndUnfairTest {

    private static Lock fairLock=new ReentrantLock2(true);

    private static Lock unfairLock=new ReentrantLock2(false);

    private static CountDownLatch start;

    private static CountDownLatch end;

    @Test
    public void unfair(){
        testLock(unfairLock);
    }

    @Test
    public void fair(){
        testLock(fairLock);
    }

    private void testLock(Lock lock){
        int countThread=5;
        start = new CountDownLatch(1);
        end=new CountDownLatch(countThread);
        for (int i = 0; i < countThread; i++) {
            Thread thread = new Job(lock);
            thread.setName("" + i);
            thread.start();
        }
        start.countDown();
        try {
            end.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class Job extends Thread{
        private Lock lock;

        public Job(Lock lock) {
            this.lock = lock;
        }

        public void run() {
            try {
                start.await();
            } catch (InterruptedException e) {
            }
            for (int i = 0; i < 2; i++) {
                lock.lock();
                try {
                    //这里睡眠一会，防止加锁解锁操作太快不产生竞争，使用junit会有这种问题，使用psvm则不会
                    TimeUnit.MILLISECONDS.sleep(20);
                    System.out.println("Lock by [" + getName() + "], Waiting by " + ((ReentrantLock2) lock).getQueuedThreads());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            end.countDown();
        }

        public String toString() {
            return getName();
        }
    }

    private static class ReentrantLock2 extends ReentrantLock{
        public ReentrantLock2() {
        }

        public ReentrantLock2(boolean fair) {
            super(fair);
        }

        public Collection<Thread> getQueuedThreads(){
            List<Thread> arrayList=new ArrayList<>(super.getQueuedThreads());
            Collections.reverse(arrayList);
            return arrayList;
        }

    }
}

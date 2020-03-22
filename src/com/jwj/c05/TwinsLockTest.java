package com.jwj.c05;

import com.jwj.c04.SleepUtils;

import java.util.concurrent.locks.Lock;

public class TwinsLockTest {
    public static void main(String[] args) {
        final Lock lock=new TwinsLock();
        class Worker extends Thread{
            public Worker(String name) {
                super(name);
            }

            @Override
            public void run() {
                while (true){
                    lock.lock();
                    try {
                        SleepUtils.second(1);
                        System.out.println(Thread.currentThread().getName());
                        SleepUtils.second(1);
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            Worker w=new Worker("Thread-"+i);
            w.setDaemon(true);
            w.start();
        }

        for (int i = 0; i < 10; i++) {
            SleepUtils.second(1);
            System.out.println();
        }
    }
}

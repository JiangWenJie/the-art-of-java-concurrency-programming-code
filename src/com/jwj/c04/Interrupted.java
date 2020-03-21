package com.jwj.c04;

import java.util.concurrent.TimeUnit;

/**
 * 当线程抛出InterruptedException时会重置中断位为false。此时调用interrupt()并不会把中断位重置
 */
public class Interrupted {
    public static void main(String[] args) throws InterruptedException {
        Thread sleepThread=new Thread(new SleepRunner(),"sleepThread");
        sleepThread.setDaemon(true);
        Thread busyThread=new Thread(new BusyRunner(),"busyThread");
        busyThread.setDaemon(true);
        sleepThread.start();
        busyThread.start();
        TimeUnit.SECONDS.sleep(5);
        sleepThread.interrupt();
        busyThread.interrupt();
        System.out.println("SleepThread interrupted is "+sleepThread.isInterrupted());
        System.out.println("BusyThread interrupted is "+busyThread.isInterrupted());
        SleepUtils.second(2);
    }

    static class SleepRunner implements Runnable{

        @Override
        public void run() {
            while (true){
                SleepUtils.second(10);
            }
        }
    }
    static class BusyRunner implements Runnable{

        @Override
        public void run() {
            while (true){

            }
        }
    }
}

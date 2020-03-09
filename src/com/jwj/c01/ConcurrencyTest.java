package com.jwj.c01;

/**
 * 串行与并行测试
 */
public class ConcurrencyTest {

    private static final long count=100000001;

    public static void main(String[] args) throws InterruptedException {
        concurrency();
        serial();
    }

    /**
     * 并行执行两个次数为count的循环
     * 注意：原书代码存在问题
     * thread.join();
     * 应在
     * long time=System.currentTimeMillis()-start;
     * 前，表示“耗时计算必须在thread线程完全执行完后”，
     * 否则很可能导致耗时计算已经完成，而thread线程还在执行循环！
     * @throws InterruptedException
     */
    public static void concurrency() throws InterruptedException {
        long start=System.currentTimeMillis();
        Thread thread=new Thread(() -> {
            int a=0;
            for (long i = 0; i < count; i++) {
                a+=5;
            }
        });
        thread.start();
        int b=0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        thread.join();
        long time=System.currentTimeMillis()-start;
        System.out.println("并行："+time+"ms,b="+b);
    }

    /**
     * 串行执行两个次数为count的循环
     */
    public static void serial(){
        long start=System.currentTimeMillis();
        int a=0;
        for (long i = 0; i < count; i++) {
            a+=5;
        }
        int b=0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        long time=System.currentTimeMillis()-start;
        System.out.println("串行："+time+"ms,b="+b);
    }
}

package com.jwj.c04;


import java.util.concurrent.TimeUnit;

/**
 * ThreadLocal实例
 */
public class Profiler {
    public static void main(String[] args) throws InterruptedException {
        Profiler.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost: "+Profiler.end()+" mills");
    }

    private static final ThreadLocal<Long> TIME_THTRADLOCAL=new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    public static final void begin(){
        TIME_THTRADLOCAL.set(System.currentTimeMillis());
    }

    public static final long end(){
        return System.currentTimeMillis()- TIME_THTRADLOCAL.get();
    }
}

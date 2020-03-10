package com.jwj.c04;


/**
 * Daemon线程在JVM不存在非Daemon线程时会自动退出，用于程序中后台调度和支持性工作，比如GC
 * 在MultiThread代码中提到的Monitor Ctrl-Break 线程也是守护线程，finally无法保证执行
 */
public class Daemon {
    public static void main(String[] args) {
        Thread thread=new Thread(new DaemonRunner(),"DaemonRunner");
        thread.setDaemon(true);
        thread.start();
        //分割线，以上时原书代码，以下是笔者为了测试其他线程的Daemon属性
        printlnAllThreadInfo();
    }

    static class DaemonRunner implements Runnable{

        @Override
        public void run() {
            try {
                SleepUtils.second(10);
            } finally {
                System.out.println("DaemonThread finally run.");
            }
        }
    }

    /**
     * 打印所有线程的信息，包括id，name，是否是守护线程，groupName
     * 这里读者可能会疑惑为什么DaemonThread的线程id是11而不是7，因为从Thread源码来看，应该连续，笔者Debug之后发现
     * 存在C2 CompilerThread{num}线程，其中num的数量为cpu数，笔者电脑为4核，所以存在C2 CompilerThread7，
     * C2 CompilerThread8，C2 CompilerThread9，C2 CompilerThread10线程，该线程负责字节码的编译工作，
     * 所以笔者在统计线程时该线程就已经Terminated了，笔者暂时也没有好的办法来解决这个问题
     */
    static void printlnAllThreadInfo(){
        ThreadGroup threadGroup=Thread.currentThread().getThreadGroup();
        ThreadGroup rootThreadGroup=getRootThreadGroup(threadGroup);
        Thread[] rootThreads=new Thread[rootThreadGroup.activeCount()];
        rootThreadGroup.enumerate(rootThreads,true);
        for (Thread thread:rootThreads) {
            System.out.println("["+thread.getId()+"]"+" name "+thread.getName()+" isDaemon "+thread.isDaemon()+" group "+thread.getThreadGroup().getName());
        }
    }

    /**
     * 获取根线程组
     * @param currentThreadGroup 当前线程组
     * @return
     */
    static ThreadGroup getRootThreadGroup(ThreadGroup currentThreadGroup){
        ThreadGroup rootThreadGroup=currentThreadGroup;
        while (rootThreadGroup.getParent()!=null){
            rootThreadGroup=rootThreadGroup.getParent();
        }
        return rootThreadGroup;
    }
}

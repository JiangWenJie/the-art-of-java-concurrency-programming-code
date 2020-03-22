package com.jwj.c06;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class CountTask extends RecursiveTask<Integer> {
    //阈值
    private static final int THRESHOLD=2;

    private int start;

    private int end;

    public CountTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum=0;
        //如果任务足够小就计算任务
        boolean canCompute=(end-start)<=THRESHOLD;
        if (canCompute){
            for (int i = start ; i <= end; i++) {
                sum+=i;
            }
        }else {
            //如果任务大于阈值，就分裂成2个子任务计算
            int middle=(start+end)/2;
            CountTask leftTask=new CountTask(start,middle);
            CountTask rightTask=new CountTask(middle+1,end);
            //执行子任务
            leftTask.fork();
            rightTask.fork();
            //等子任务执行完成，得到结果
            int leftResult=leftTask.join();
            int rightResult=rightTask.join();
            //合并子任务
            sum=leftResult+rightResult;
        }
        return sum;
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool=new ForkJoinPool();
        //生成一个计算任务
        CountTask task=new CountTask(1,7);
        //执行一个计算任务
        Future<Integer> result=forkJoinPool.submit(task);
        if (task.isCompletedAbnormally()){
            System.out.println(task.getException());
        }
        try {
            System.out.println(result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

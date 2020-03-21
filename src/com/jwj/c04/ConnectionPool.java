package com.jwj.c04;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * 模拟连接池
 */
public class ConnectionPool {
    private LinkedList<Connection> pool=new LinkedList<>();

    public ConnectionPool(int initialSize) {
        if (initialSize>0){
            for (int i = 0; i < initialSize; i++) {
                pool.add(ConnectionDriver.createConnection());
            }
        }
    }

    public void releaseConnection(Connection connection){
        if (null!=connection){
            synchronized (pool){
                //连接释放后需要进行通知，这样其他消费者能够感知到连接池中已经归还了一个连接
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }

    /**
     * 超时获取一个连接
     * @param mills
     * @return
     * @throws InterruptedException
     */
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool){
            //不设置超时时间
            if (mills<=0){
                //当线程池资源不足时，等待
                while (pool.isEmpty()){
                    pool.wait();
                }
                return pool.removeFirst();
            }else {
                //设置超时时间
                long future=System.currentTimeMillis()+mills;
                long remaining=mills;
                //超时的具体实现
                while (pool.isEmpty()&&remaining>0){
                    pool.wait(remaining);
                    remaining=future-System.currentTimeMillis();
                }
                Connection result=null;
                if (!pool.isEmpty()){
                    result=pool.removeFirst();
                }
                return result;
            }
        }
    }
}

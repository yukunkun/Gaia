package com.gaiamount.util.network;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 管理线程池
 *
 * @author itcast
 *
 */
public class ThreadManager {
    private  final String TAG =this.getClass().getSimpleName() ;

    private ThreadManager() {

    }

    private static ThreadManager instance = new ThreadManager();
    private ThreadPoolProxy longPool;
    private ThreadPoolProxy shortPool;

    public static ThreadManager getInstance() {
        return instance;
    }

    // 联网比较耗时
    // cpu的核数*2+1
    public synchronized ThreadPoolProxy createLongPool() {
        if (longPool == null) {
            longPool = new ThreadPoolProxy(5, 2*getNumCores()+1, 5000L);
        }
        return longPool;
    }
    // 操作本地文件
    public synchronized ThreadPoolProxy createShortPool() {
        if(shortPool==null){
            shortPool = new ThreadPoolProxy(3, 2*getNumCores()+1, 5000L);
        }
        return shortPool;
    }

    public class ThreadPoolProxy {
        private ThreadPoolExecutor pool;
        private int corePoolSize;
        private int maximumPoolSize;
        private long time;

        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long time) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.time = time;

        }
        /**
         * 执行任务
         * @param runnable
         */
        public void execute(Runnable runnable) {
            if (pool == null) {
                // 创建线程池
				/*
				 * 1. 线程池里面管理多少个线程2. 如果排队满了, 额外的开的线程数3. 如果线程池没有要执行的任务 存活多久4.
				 * 时间的单位 5 如果 线程池里管理的线程都已经用了,剩下的任务 临时存到LinkedBlockingQueue对象中 排队
				 */
                pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                        time, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(10));
            }
            pool.execute(runnable); // 调用线程池 执行异步任务
        }
        /**
         * 取消任务
         * @param runnable
         */
        public void cancel(Runnable runnable) {
            if (pool != null && !pool.isShutdown() && !pool.isTerminated()) {
                pool.remove(runnable); // 取消异步任务
            }
        }
    }

    //获取CPU个数
    private int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if(Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }

        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            Log.d(TAG, "CPU Count: " + files.length);
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch(Exception e) {
            //Print exception
            Log.d(TAG, "CPU Count: Failed.");
            e.printStackTrace();
            //Default to return 1 core
            return 1;
        }
    }
}
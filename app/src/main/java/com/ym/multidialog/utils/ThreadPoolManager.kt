package com.ym.multidialog.utils

import java.io.Serializable
import java.util.concurrent.*
/**
 * @author : lxm
 * @package_name ：com.ym.multidialog.utils
 * @date :2021年5月21日17:05:23
 * @description ：线程池工具
 */

class ThreadPoolManager : Serializable {

    companion object {
        @JvmStatic
        fun getInstance(): ThreadPoolManager {
            return SingletonHolder.mInstance
        }

        fun getSingleThreadPool(): ExecutorService {
            return ThreadPoolExecutor(
                1, 1,
                0L, TimeUnit.MILLISECONDS,
                LinkedBlockingQueue<Runnable>())
        }

        fun getCachedThreadPool(): ExecutorService {
            return ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                SynchronousQueue<Runnable>())
        }

        fun getFixThreadPool(): ExecutorService {
            return ThreadPoolExecutor(
                4, 4,
                0L, TimeUnit.MILLISECONDS,
                LinkedBlockingQueue<Runnable>()
            )
        }

        fun getSingleScheduledExecutor(): ScheduledExecutorService{
            return ScheduledThreadPoolExecutor(1,
                ThreadPoolExecutor(
                    1,
                    4, 0L,
                    TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>()
                ).threadFactory)
        }

    }

    private object SingletonHolder {
        val mInstance: ThreadPoolManager = ThreadPoolManager()
    }

    private fun readResolve(): Any {//防止单例对象在反序列化时重新生成对象
        return SingletonHolder.mInstance
    }


}
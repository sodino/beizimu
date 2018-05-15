package bei.zi.mu.thread

import android.os.Handler
import android.os.HandlerThread
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
 * Created by sodino on 2018/2/27.
 */

@Deprecated("线索池操作请使用RxJava相关的Schedulers替代。",
ReplaceWith("""
    * 如UI操作： <br/>
    * AndroidSchedulers.mainThread().scheduleDirect()
    * 工作线索操作：
    * Schedulers.newThread().scheduleDirect()
        """),
        level = DeprecationLevel.WARNING)
class ThreadPool {
    companion object {
        /**线程池中保留长驻线程数*/
        val CORE_POOL_SIZE          = 5
        /**线程池中最大的工作线程数*/
        val MAX_POOL_SIZE           = 128
        /**非长驻线程工作结束后，保留在线程池内不terminate的时间长度。单位TimeUnit.SECONDS。*/
        val KEEP_ALIVE_TIME         = 10L

        var index                   = 0

        val lock                    by lazy { ReentrantLock() }
        val instance                by lazy { ThreadPool() }
        val threadsExecutor by lazy { instance.threadsExecutor }
        val UIHandler               by lazy { Handler() }
        val workHandler             by lazy {
            val thread = HandlerThread("app.handler.work")
            thread.start()
            Handler(thread.looper)
        }
    }

    var blockingQueue      : BlockingQueue<Runnable>
    var threadsExecutor    : ThreadsExecutor

    val threadFactory = ThreadFactory { runnable ->
        index ++
        val threadName = "bzm.$index"
        val thread = Thread(runnable)
        thread.name = threadName
        thread
    }

    init {
        blockingQueue = LinkedBlockingDeque<Runnable>()
        threadsExecutor = ThreadsExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, blockingQueue, threadFactory)
    }
}
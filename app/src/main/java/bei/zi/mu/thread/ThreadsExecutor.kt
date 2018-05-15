package bei.zi.mu.thread

import java.util.concurrent.BlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by sodino on 2018/2/27.
 */

@Deprecated("See ThreadPool")
class ThreadsExecutor(corePoolSize: Int,
                      maximumPoolSize : Int,
                      keepAliveTime : Long,
                      unit : TimeUnit,
                      workQueue : BlockingQueue<Runnable>,
                      threadFactory : ThreadFactory)
    : ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory) {

    override fun beforeExecute(t: Thread?, r: Runnable?) {
        super.beforeExecute(t, r)
    }

    override fun afterExecute(r: Runnable?, t: Throwable?) {
        super.afterExecute(r, t)
    }
}
package sodino.log

/**
 * Created by sodino on 2018/2/26.
 */
interface Catty{
    fun i(log: String)
    fun d(log: String, throwable : Throwable? = null)
    fun e(log: String, throwable: Throwable? = null)
//    fun f(log: String, throwable: Throwable? = null)
}
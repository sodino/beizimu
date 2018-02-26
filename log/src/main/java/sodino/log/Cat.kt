package sodino.log

import android.util.Log

/**
 * Created by sodino on 2018/2/26.
 */
class Cat constructor(tag : String) : Catty{
    val tag = tag

    companion object {
        fun getCurrentMethod(): String {
            val defIndex = 7;
            val elements = Thread.currentThread().stackTrace;
            if (elements == null || elements.size <= defIndex) {
                return "";
            }

            var element = elements[defIndex];

            var clazz : String
            if (Cat::class.java.simpleName.length < "Cat".length) {
                clazz = element.className
            } else {
                clazz = element.className.substring(element.className.lastIndexOf('.') + 1)
            }

            val result = clazz + "#" + element.methodName
            return result;
        }

        fun println(priority: Int, tag: String, log : String, throwable: Throwable? = null) {
            var sb = StringBuilder("[${getCurrentMethod()}] $log")
            if (throwable != null) {
                sb.append("\n").append(Log.getStackTraceString(throwable))
            }
            Log.println(priority, tag, sb.toString())
        }
    }

    override fun i(log: String) {
        Companion.println(Log.INFO, tag, log)
    }

    override fun d(log: String, throwable : Throwable ?) {
        Companion.println(Log.DEBUG, tag, log, throwable)
    }


    override fun e(log: String, throwable : Throwable ?) {
        Companion.println(Log.ERROR, tag, log, throwable)
    }

//    override fun f(log: String, throwable : Throwable ?) {
//        Log.d(tag, log);
//        if (throwable != null) {
//            var stacktrace = Log.getStackTraceString(throwable)
//            Log.d(tag, stacktrace)
//        }
//    }


}


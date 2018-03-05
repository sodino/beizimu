package bei.zi.mu

import sodino.log.Cat
import sodino.log.Catty

/**
 * Created by sodino on 2018/2/26.
 */
class LogCat{
    companion object : Catty{
        private val cat : Cat = Cat("bzm")

        override fun i(log : String) {
            cat.i(log)
        }

        override fun d(log: String, throwable: Throwable?) {
            cat.d(log, throwable)
        }

        override fun e(log: String, throwable: Throwable?) {
            cat.e(log, throwable)
        }

//        override fun f(log: String, throwable: Throwable?) {
//            cat.f(log, throwable)
//        }

    }
}
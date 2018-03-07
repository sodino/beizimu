package bei.zi.mu.http.bean

import bei.zi.mu.LogCat

/**
 * Created by sodino on 2018/3/4.
 */
abstract class Bean : BeanInterface {
    var code: Int = -1
    var message: String = ""

    abstract fun parse(response : String)
}
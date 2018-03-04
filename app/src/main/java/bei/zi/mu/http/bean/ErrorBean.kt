package bei.zi.mu.http.bean

/**
 * Created by sodino on 2018/3/4.
 */
class ErrorBean : Bean() {
    override val isFilled: Boolean
        get() = true
    var code: Int = 0
    var message: String = ""
}
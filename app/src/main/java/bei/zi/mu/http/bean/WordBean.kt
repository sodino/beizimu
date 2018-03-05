package bei.zi.mu.http.bean

import bei.zi.mu.LogCat

/**
 * Created by sodino on 2018/3/4.
 */
class WordBean : Bean() {
    override val isFilled: Boolean
        get() = true

    override fun parse(response: String) {
        LogCat.d("responseJson=$response")
    }
}
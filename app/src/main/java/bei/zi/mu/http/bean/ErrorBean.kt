package bei.zi.mu.http.bean

/**
 * Created by sodino on 2018/3/4.
 */
class ErrorBean : Bean() {

    override fun isFilled(): Boolean {
        return code != -1
    }


    override fun parse(response: String) {
    }

}
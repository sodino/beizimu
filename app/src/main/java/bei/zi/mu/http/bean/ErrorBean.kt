package bei.zi.mu.http.bean

import io.objectbox.Property

/**
 * Created by sodino on 2018/3/4.
 */
class ErrorBean : Bean<Nothing, Nothing>() {
    override fun updateDbBean(oldBean: Nothing) : Nothing{
        throw kotlin.UnsupportedOperationException("Not supported for ErrorBean")
    }

    override fun primaryValue(): Nothing {
        throw kotlin.UnsupportedOperationException("Not supported for ErrorBean")
    }

    override fun primaryKey(): Property {
        return Bean.None
    }

    override fun isFilled(): Boolean {
        return code != -1
    }


    override fun parse(response: String) {
    }

}
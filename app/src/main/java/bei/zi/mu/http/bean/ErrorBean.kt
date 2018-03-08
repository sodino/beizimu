package bei.zi.mu.http.bean

import io.objectbox.Property

/**
 * Created by sodino on 2018/3/4.
 */
class ErrorBean : Bean<Nothing>() {
    override fun updateOldBean(oldBean: Nothing) : Nothing{
        throw kotlin.UnsupportedOperationException("Not supported for ErrorBean")
    }

    override fun primaryStringValue(): String {
        return ""
    }

    override fun primaryStringKey(): Property {
        return Bean.None
    }

    override fun isFilled(): Boolean {
        return code != -1
    }


    override fun parse(response: String) {
    }

}
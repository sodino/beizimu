package bei.zi.mu.http.bean

import android.text.TextUtils
import android.util.AndroidRuntimeException

import bei.zi.mu.App
import io.objectbox.Property

/**
 * Created by sodino on 2018/3/8.
 */

abstract class Bean<T> {

    var code = -1
    var message = ""

    open fun parse(response: String){}

    abstract fun primaryStringKey(): Property
    abstract fun primaryStringValue() : String
    abstract fun updateOldBean(oldBean : T) : T

    /** true:真正取到数据，各种list或bean不会为空。false:具体的list.size==0或bean为null.  */
    abstract fun isFilled() : Boolean

    companion object {
        val None = Property(0, 0, String::class.java, "none")
    }


    fun <T> findByPrimary(value: String = ""): T? {
        if (TextUtils.isEmpty(value)) {
            return null
        }
        val primaryKey = primaryStringKey()
        if (primaryKey === None) {
            return null
        }

        val box = App.myApp.boxStore.boxFor(javaClass)
        val bean = box.query().equal(primaryKey, value).build().findFirst()
        return bean as T?
    }

    fun save(b : Bean<T>? = null ) {
        val box = App.myApp.boxStore.boxFor(javaClass)
        if (b == null) {
            box.put(this)
        } else {
            box.put(b)
        }
    }

    fun insertOrUpdate() {
        val bean = findByPrimary<T>(primaryStringValue())
        if (bean == null) {
            save()
        } else {
            val updateBean = updateOldBean(bean as T)
            if (bean !== updateBean) {
                throw AndroidRuntimeException("please update and return oldBean, NOT other bean!")
            }
            save(updateBean as Bean<T>)
        }
    }
}

package bei.zi.mu.http.bean

import android.util.AndroidRuntimeException
import android.util.Log

import bei.zi.mu.App
import io.objectbox.Property

/**
 *
 * TODO : proguard混淆配置
 * 1. getId()
 * 1. primaryKey()
 * Created by sodino on 2018/3/8.
 */

abstract class Bean<T, PrimaryType>{
    var _id : Long
        get() {
            return javaClass.getMethod("getId").invoke(this) as Long
        }
        private set(id : Long) {}

    var code = -1
    var message = ""

    open fun parse(response: String){}

    abstract fun primaryKey(): Property

    abstract fun primaryValue() : PrimaryType
    abstract fun updateDbBean(dbBean : T) : T

    /** true:真正取到数据，各种list或bean不会为空。false:具体的list.size==0或bean为null.  */
    abstract fun isFilled() : Boolean

    companion object {
        public val None = Property(0, 0, String::class.java, "none")

        public fun <BeanType, ValueType>findFirstByPrimaryKey(clazz : Class<*>, key : Property, value : ValueType) : BeanType?{
            if (value == null) {
                return null
            }

            val tValue : Any = value
            val box = App.myApp.boxStore.boxFor(clazz)
            val queryBuilder = box.query()
            when(tValue) {
                is Long     -> {queryBuilder.equal(key, tValue)}
                is Boolean  -> {queryBuilder.equal(key, tValue)}
                is String   -> {queryBuilder.equal(key, tValue)}
                else        -> {
                    throw AndroidRuntimeException("Primary Type not support ${tValue::class.java}")
                }
            }


            val result = queryBuilder.build().findFirst()

            return if (result != null) (result as BeanType) else null
        }
    }


    fun <T> findByPrimary(value: PrimaryType): T? {
        val primaryKey = primaryKey()
        if (primaryKey === None) {
            return null
        }

        val box = App.myApp.boxStore.boxFor(javaClass)
        var result : Any? = null
        when (value) {
            is String -> {result = box.query().equal(primaryKey, value).build().findFirst() }
            is Long ->  {result = box.query().equal(primaryKey, value).build().findFirst() }
            is Boolean ->  {result = box.query().equal(primaryKey, value).build().findFirst() }
        }

        var bean : T? = null
        if (result != null) {
            bean = result as T
        }
        return bean?:null
    }

    private fun save(b : Bean<T, PrimaryType>? = null ) {
        val box = App.myApp.boxStore.boxFor(javaClass)
        if (b == null) {
            box.put(this)
        } else {
            box.put(b)
        }
    }

    public fun insertOrUpdate() {
        // fixed 主键id为0时，为新对象；不为0时，为数据库中实例化的或assignable=true的情况。
        // id = 0 时需要findByPrimary()查找一下； id不为0时可以直接save()
        // save()的情况要考虑该class中的ToMany 或 ToOne 有可能也是刚new出来而不是从数据库取出来的，仍然会造成重复
        // 看是不是要java反射遍历ToMany或ToOne中的实例链
        if (_id == 0L) {
            save()
        } else {
            val bean = findByPrimary<T>(primaryValue())
            if (bean == null) {
                save()
            } else {
                val _idDB = (bean as Bean<*, *>)._id
                val updateBean = updateDbBean(bean as T)
//                if (bean !== updateBean) {
//                    throw AndroidRuntimeException("please update and return oldBean, NOT other bean!")
//                }
                if (_idDB != (updateBean as Bean<*, *>)._id) {
                    throw AndroidRuntimeException("updateDBBean._id != oldDBBean._id")
                }
                save(updateBean as Bean<T, PrimaryType>)
            }
        }
    }



    public fun <ValueType>findFirstByPrimaryKey(value : ValueType) : T? {
        if (value == null) {
            return null
        }


        val tValue : Any = value
        val box = App.myApp.boxStore.boxFor(javaClass)
        val queryBuilder = box.query()
        val key = primaryKey()
        when(value) {
            is Long     -> {queryBuilder.equal(key, value)}
            is Boolean  -> {queryBuilder.equal(key, value)}
            is String   -> {queryBuilder.equal(key, value)}
            else        -> {
                throw AndroidRuntimeException("Primary Type not support ${tValue::class.java}")
            }
        }


        val result = queryBuilder.build().findFirst()

        return if (result != null) (result as T) else null
    }
}

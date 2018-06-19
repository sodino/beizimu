package bei.zi.mu.http.bean

import android.util.AndroidRuntimeException

import bei.zi.mu.App
import io.objectbox.Property

/**
 * Created by sodino on 2018/3/8.
 */

abstract class Bean<T>{
    var _id : Long
        get() {
            return javaClass.getMethod("getId").invoke(this) as Long
        }
        private set(id : Long) {}

    var code = -1
    var message = ""

    open fun parse(response: String){}

    abstract fun primaryKeys(): Array<Property>

    abstract fun primaryValues() : Array<*>
    abstract fun updateDbBean(dbBean : T) : T

    /** true:真正取到数据，各种list或bean不会为空。false:具体的list.size==0或bean为null.  */
    abstract fun isFilled() : Boolean

    companion object {
        public val None = Property(0, 0, String::class.java, "none")

        public fun <BeanType>findFirstByPrimaryKey(clazz : Class<*>, primaryKeys : Array<Property>, values : Array<*>) : BeanType?{
            if (primaryKeys.isEmpty()) {
                throw AndroidRuntimeException("primaryKeys is empty.")
            }
            if (values.isEmpty()) {
                throw AndroidRuntimeException("values is empty.")
            }
            if (values.size != primaryKeys.size) {
                throw AndroidRuntimeException("values.size(${values.size}) != primaryKeys.size(${primaryKeys.size})")
            }

            val size = values.size

            val box = App.myApp.boxStore.boxFor(clazz)
            val queryBuilder = box.query()


            for (i in 0 until size) {
                val key = primaryKeys[i]
                val value = values[i]
                when(value) {
                    is Long     -> {queryBuilder.equal(key, value)}
                    is Boolean  -> {queryBuilder.equal(key, value)}
                    is String   -> {queryBuilder.equal(key, value)}
                    else        -> {
                        throw AndroidRuntimeException("Primary Type not support ${value?.javaClass}")
                    }
                }
            }


            val result = queryBuilder.build().findFirst()

            return if (result != null) (result as BeanType) else null
        }
    }


    fun <T> findByPrimary(values: Array<*>): T? {
        return findFirstByPrimaryKey<T>(javaClass, primaryKeys(), values)
    }

    private fun save(b : Bean<T>? = null ) {
        val box = App.myApp.boxStore.boxFor(javaClass)
        if (b == null) {
            box.put(this)
        } else {
            box.put(b)
        }
    }


    public fun remove() : Long {
        var removeId = 0L
        val box = App.myApp.boxStore.boxFor(javaClass)
        if (_id != 0L) {
            // 避免 ： IllegalArgumentException: Illegal key value: 0
            box.remove(_id)
            removeId = _id
        } else {
            // 已经生成了，也存储了，但仍然使用new对象的，而不是从数据库读取出来的，导致_ID仍是0
            val bean = findByPrimary<T>(primaryValues())
            if (bean is Bean<*> && bean._id != 0L) {
                // 真正去删除，再检查一遍_ID !=0L 避免死循环
                bean.remove()

                removeId = bean._id
            }
        }

        return removeId
    }

    public fun insertOrUpdate() {
        // fixed 主键id为0时，为新对象；不为0时，为数据库中实例化的或assignable=true的情况。
        // 存储前需要findByPrimary()查找一下，根据自定义的primary进行排重。
        // save()的情况要考虑该class中的ToMany 或 ToOne 有可能也是刚new出来而不是从数据库取出来的，仍然会造成重复
        // 看是不是要java反射遍历ToMany或ToOne中的实例链
        val bean = findByPrimary<T>(primaryValues())
        if (bean == null) {
            save()
        } else {
            val _idDB = (bean as Bean<*>)._id
            val updateBean = updateDbBean(bean as T)
//                if (bean !== updateBean) {
//                    throw AndroidRuntimeException("please update and return oldBean, NOT other bean!")
//                }
            if (_idDB != (updateBean as Bean<*>)._id) {
                throw AndroidRuntimeException("updateDBBean._id != oldDBBean._id")
            }
            save(updateBean as Bean<T>)
        }
    }

}

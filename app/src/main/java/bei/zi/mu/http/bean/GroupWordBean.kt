package bei.zi.mu.http.bean

import bei.zi.mu.App
import io.objectbox.Property
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
/**
 * 分组与生词的结对存储
 * */
@Entity
data class GroupWordBean (
        @Id
        var id                      : Long                      = 0,
        var group                   : String                    = "",
        var word                    : String                    = ""
    ) : Bean<GroupWordBean>(){
    companion object {
        fun findFirstByPrimaryKey(group : String, word : String) : GroupWordBean? {
            val clazz = GroupWordBean::class.java
            val propertys = arrayOf(GroupWordBean_.group, GroupWordBean_.word)
            val values = arrayOf(group, word)
            return findFirstByPrimaryKey<GroupWordBean>(clazz, propertys, values)
        }

        fun all(): List<GroupWordBean> {
            val box = App.myApp.boxStore.boxFor(GroupWordBean::class.java)
            return box.all
        }
    }

    override fun primaryKeys(): Array<Property> {
        return arrayOf(GroupWordBean_.group, GroupWordBean_.word)
    }

    override fun primaryValues(): Array<*> {
        return arrayOf(group, word)
    }

    override fun updateDbBean(dbBean: GroupWordBean): GroupWordBean {
        val newBean = dbBean.copy(group = group, word = word)

        return newBean
    }

    override fun isFilled(): Boolean {
        val bool = group.isNotEmpty() && word.isNotEmpty()
        return bool
    }

}
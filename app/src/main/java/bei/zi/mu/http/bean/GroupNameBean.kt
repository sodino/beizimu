package bei.zi.mu.http.bean

import bei.zi.mu.App
import io.objectbox.Property
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
/**
 * 分组名称
 * */
@Entity
data class GroupNameBean (
        @Id
        var id                      : Long                      = 0,
        var name                    : String                    = ""
    ) : Bean<GroupNameBean>(){
    companion object {
        public fun findFirstByPrimaryKey(value : String) : GroupNameBean? {
            val clazz = GroupNameBean::class.java
            val property = GroupNameBean_.name
            return findFirstByPrimaryKey<GroupNameBean>(clazz, arrayOf(property), arrayOf(value))
        }

        fun all(): List<GroupNameBean> {
            val box = App.myApp.boxStore.boxFor(GroupNameBean::class.java)
            return box.all
        }
    }

    override fun primaryKeys(): Array<Property> {
        return arrayOf(GroupNameBean_.name)
    }

    override fun primaryValues(): Array<*> {
        return arrayOf(name)
    }

    override fun updateDbBean(dbBean: GroupNameBean): GroupNameBean {
        val newBean = dbBean.copy(name = name)

        return newBean
    }

    override fun isFilled(): Boolean {
        val bool = name.isNotEmpty()
        return bool
    }

}
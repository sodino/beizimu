package bei.zi.mu.http.bean

import bei.zi.mu.App
import io.objectbox.Property
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.query.OrderFlags

/**
 * 分组与生词的结对存储
 * */
@Entity
data class GroupWordBean (
        @Id
        var id                      : Long                      = 0,
        var groupId                 : Long                      = 0,
        var word                    : String                    = ""
    ) : Bean<GroupWordBean>(){
    companion object {
        fun findFirstByPrimaryKey(groupId : Long, word : String) : GroupWordBean? {
            val clazz = GroupWordBean::class.java
            val propertys = arrayOf(GroupWordBean_.groupId, GroupWordBean_.word)
            val values = arrayOf(groupId, word)
            return findFirstByPrimaryKey<GroupWordBean>(clazz, propertys, values)
        }

        fun all(): List<GroupWordBean> {
            val box = App.myApp.boxStore.boxFor(GroupWordBean::class.java)
            val list = box.all
            return list
        }

        fun findByGroupId(groupId : Long, idDecreasing : Boolean = true): List<WordBean>? {
            val flag = if (idDecreasing) { OrderFlags.DESCENDING } else { 0 }

            val box = App.myApp.boxStore.boxFor(GroupWordBean::class.java)
            val listGroupWord = box.query()
                    .equal(GroupWordBean_.groupId, groupId)
                    .order(GroupWordBean_.id, flag)
                    .build()
                    .find()
            val listWord = mutableListOf<WordBean>()
            listGroupWord.forEach { val wordName = it.word
                val wordBean = WordBean.findFirstByPrimaryKey(wordName)
                if (wordBean != null) {
                    listWord.add(wordBean)
                }
            }

            return listWord
        }
    }

    override fun primaryKeys(): Array<Property> {
        return arrayOf(GroupWordBean_.groupId, GroupWordBean_.word)
    }

    override fun primaryValues(): Array<*> {
        return arrayOf(groupId, word)
    }

    override fun updateDbBean(dbBean: GroupWordBean): GroupWordBean {
        val newBean = dbBean.copy(groupId = groupId, word = word)

        return newBean
    }

    override fun isFilled(): Boolean {
        val bool = groupId != 0L && word.isNotEmpty()
        return bool
    }

}
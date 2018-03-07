package bei.zi.mu.http.bean

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

/**
 * Created by sodino on 2018/3/7.
 */
@Entity
data class MemoryBean(
        @Id
        var id               : Long              = 0,
        var tCreate          : Long              = 0,            // 第一次查询该单词的时间
        var tLastReview      : Long              = 0,            // 上一次复习的时间
        var reviewNum        : Long              = 0,            // 复习次数
        var okPhonetic       : Boolean           = false,        // true: 记得音标
        var okMean           : Boolean           = false,        // true: 记得该单词的意思
        var word             : ToOne<WordBean>?  = null
) : BeanInterface{

    override fun isFilled(): Boolean {
        return tCreate > 0
    }

}
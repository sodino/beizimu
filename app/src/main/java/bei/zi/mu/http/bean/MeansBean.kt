package bei.zi.mu.http.bean

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

/**
 * Created by sodino on 2018/3/6.
 */

@Entity
data class MeansBean(
        @Id
        var id          : Long                  = 0,
        var part        : String                = "",   // "n."  "vt."
        var mean        : String                = "",
        var word        : ToOne<WordBean>?      = null
        ) : BeanInterface {

    override fun isFilled(): Boolean {
        return true
    }

}
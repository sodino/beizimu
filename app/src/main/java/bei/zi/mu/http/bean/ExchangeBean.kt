package bei.zi.mu.http.bean

import bei.zi.mu.Const
import io.objectbox.Property
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by sodino on 2018/3/6.
 */


@Entity
data class ExchangeBean(
       @Id
       var id           : Long                  = 0,
       /**@see Const.exchange */
       var type         : Int                   = 0,
       var exchange     : String                = "",
       var word         : ToOne<WordBean>?      = null
) : Bean<ExchangeBean>() {
    companion object {
        fun parse(jsonExchanges: JSONObject) : List<ExchangeBean> {
            val list = mutableListOf<ExchangeBean>()

            for ((type, name) in Const.exchange.map) {
                val tmpArr = jsonExchanges.optJSONArray("word_${name}")
                if (tmpArr == null) {
                    continue
                }
                val plArr = tmpArr as JSONArray
                if (plArr.length() > 0) {
                    list.add(ExchangeBean(type = type, exchange = plArr.getString(0)))
                }
            }

            return list
        }
    }

    override fun primaryKeys(): Array<Property> {
        return arrayOf(ExchangeBean_.id)
    }

    override fun primaryValues(): Array<Long> {
        return arrayOf(id)
    }

    override fun updateDbBean(dbBean: ExchangeBean) : ExchangeBean {
        return dbBean
    }

    override fun isFilled(): Boolean {
        return type != 0 && exchange.isNotEmpty()
    }

}
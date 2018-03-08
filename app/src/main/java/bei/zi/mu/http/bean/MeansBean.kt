package bei.zi.mu.http.bean

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
data class MeansBean(
        @Id
        var id          : Long                  = 0,
        var part        : String                = "",   // "n."  "vt."
        var mean        : String                = "",
        var word        : ToOne<WordBean>?      = null
        ) :Bean<MeansBean> () {
    override fun primaryStringKey(): Property {
        return None
    }

    override fun primaryStringValue(): String {
        return ""
    }

    override fun updateOldBean(oldBean: MeansBean) : MeansBean {
        return oldBean
    }

    companion object {
        fun parse(jsonMeans: JSONArray) : List<MeansBean> {
            val listMeans = mutableListOf<MeansBean>()

            val length = jsonMeans.length()
            for (i in 0 until length) {
                val json = jsonMeans.get(i) as JSONObject
                val part = json.optString("part")
                val jsonPartMeans = json.optJSONArray("means") as JSONArray
                val lPartMeans = jsonPartMeans.length()
                var strMeans = ""
                for (j in 0 until lPartMeans) {
                    if (strMeans.length > 0) {
                        strMeans += "\n"
                    }
                    strMeans += jsonPartMeans.get(j).toString()
                }

                val bean = MeansBean(part = part, mean = strMeans)
                listMeans.add(bean)
            }

            return listMeans
        }
    }


    override fun isFilled(): Boolean {
        return part.isNotEmpty() && mean.isNotEmpty()
    }

}
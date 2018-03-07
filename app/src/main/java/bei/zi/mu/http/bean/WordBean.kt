package bei.zi.mu.http.bean

import bei.zi.mu.Const
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by sodino on 2018/3/4.
 */

@Entity
data class WordBean(
        @Id
        var id                      : Long                      = 0,
        var name                    : String?                   = null,
        var frequence               : Int                       = 0,    // 星级
        var phoneticSymbol          : ToOne<PhoneticSymbol>?    = null, // 音标 与 发音mp3文件
        @Backlink(to = "word")
        var means                   : ToMany<MeansBean>?        = null, // 单词解释
        @Backlink(to = "word")
        var exchanges               : ToMany<ExchangeBean>?     = null, // 复数、过去式、完成时等单词变形
        var tag                     : String?                   = null  // cet4,cet6,tofel,kaoyan等...
        ) : Bean() {
    override fun isFilled(): Boolean {
        return true
    }


    override fun parse(response: String) {
        var jsonObj = JSONObject(response)
        var baseInfo = jsonObj.optJSONObject("baseInfo")
        name = baseInfo.optString("word_name")
        frequence = baseInfo.optInt("frequence")
        tag = parseTag(baseInfo.optJSONArray("word_tag"))
        val word = WordBean(name = name, frequence = frequence, tag = tag)

        val jsonExchanges = baseInfo.optJSONObject("exchange")
        val listExchange = parseExchanges(jsonExchanges)
        word.exchanges?.addAll(listExchange)


        var symbols = baseInfo.optJSONArray("symbols")[0] as JSONObject
        var phonetic : PhoneticSymbol? = null
        if (symbols != null) {
            phonetic = PhoneticSymbol.parse(symbols)
            word.phoneticSymbol?.target = phonetic
        }
        val jsonMeans = symbols.optJSONArray("parts")
        if (jsonMeans != null) {
            val listMeans = parseMeans(jsonMeans)
            word.means?.addAll(listMeans)
        }
    }

    private fun parseMeans(jsonMeans: JSONArray) : List<MeansBean> {
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
                    strMeans += "; "
                }
                strMeans += jsonPartMeans.get(j).toString()
            }

            val bean = MeansBean(part = part, mean = strMeans)
            listMeans.add(bean)
        }

        return listMeans
    }

    private fun parseTag(jsonArr: JSONArray?): String? {
        var result = ""


        jsonArr?.let {
            val size = it.length()
            for (i in 0 until size) {
                val tag = it.optInt(i)
                if (tag != null) {
                    if (result.length > 0) {
                        result += ","
                    }
                    result += tag.toString()
                }
            }
        }

        return result
    }

    private fun parseExchanges(jsonExchanges: JSONObject) : List<ExchangeBean> {
        val list = mutableListOf<ExchangeBean>()


        for ((type, name) in Const.exchange.map) {
            val plArr = jsonExchanges.optJSONArray("word_${name}") as JSONArray
            if (plArr.length() > 0) {
                list.add(ExchangeBean(type = type, exchange = plArr.getString(0)))
            }
        }

        return list
    }
}
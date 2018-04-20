package bei.zi.mu.http.bean

import android.text.TextUtils
import bei.zi.mu.LogCat
import io.objectbox.Property
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import org.json.JSONArray
import org.json.JSONObject

/**
 * ToMany ToOne的类似，会被objectbox的gradle在预编译时直接改造
 * 见反编译的结果：http://wx2.sinaimg.cn/large/e3dc9ceagy1fp4c51sb4sj21ag0w8dmr.jpg
 * Created by sodino on 2018/3/4.
 */

@Entity
data class WordBean(
        @Id
        var id                      : Long                      = 0,
        var name                    : String?                   = null,
        var frequence               : Int                       = 0,    // 星级
        @Backlink(to = "word")
        var phoneticSymbol          : ToMany<PhoneticSymbol>?   = null, // 音标 与 发音mp3文件
        // 以下为反编译后的实现
//        var phoneticSymbol          : ToMany<PhoneticSymbol>    = ToMany(this@WordBean, WordBean_.phoneticSymbol), // 音标 与 发音mp3文件
        @Backlink(to = "word")
        var means                   : ToMany<MeansBean>?        = null, // 单词解释
        @Backlink(to = "word")
        var exchanges               : ToMany<ExchangeBean>?     = null, // 复数、过去式、完成时等单词变形
        @Backlink(to = "word")
        var memory                  : ToMany<MemoryBean>?       = null, // 记忆、复习次数、时间等信息
        var tag                     : String?                   = null  // cet4,cet6,tofel,kaoyan等...
        ) : Bean<WordBean, String>() {

    override fun isFilled(): Boolean {
//        val bName = !TextUtils.isEmpty(name)
//        val bPhonetic = phoneticSymbol?.target?.isFilled()
//        val bMeans = means?.isNotEmpty()
//        // java的写法：
//        // 这里bPhonetic和bMeans会出错：  Required: kotlin.Boolean. Found: kotlin.Boolean?
//        return bName && bPhonetic && bMeans


        // kotlin的两种写法
        // 第一种
//        val bName = !TextUtils.isEmpty(name)
//        val bPhonetic = phoneticSymbol?.target?.isFilled() ?: false
//        val bMeans = means?.isNotEmpty() ?: false
//        return bName && bPhonetic && bMeans
        // 第二种
        val bName = !TextUtils.isEmpty(name)
        val bPhonetic = phoneticSymbol?.isNotEmpty()
        val bMeans = means?.isNotEmpty()
        return bName && (bPhonetic?: false) && (bMeans?: false)
    }


    override fun parse(response: String) {
        var jsonObj = JSONObject(response)
        var baseInfo = jsonObj.optJSONObject("baesInfo")    // iciba的数据写错了，竟然写成 baes ！！！
        name = baseInfo.optString("word_name")
        frequence = baseInfo.optInt("frequence")
        tag = parseTag(baseInfo.optJSONArray("word_tag"))

        val jsonExchanges = baseInfo.optJSONObject("exchange")
        if (jsonExchanges != null) {
            val listExchange = ExchangeBean.parse(jsonExchanges)
            if (listExchange != null && listExchange.isNotEmpty()) {
                exchanges?.addAll(listExchange)
            }
        }

        var symbols = baseInfo.optJSONArray("symbols")[0] as JSONObject
        var phonetic : PhoneticSymbol? = null
        if (symbols != null) {
            phonetic = PhoneticSymbol.parse(symbols)
            phoneticSymbol?.add(phonetic)
        }
        val jsonMeans = symbols.optJSONArray("parts")
        if (jsonMeans != null) {
            val listMeans = MeansBean.parse(jsonMeans)
            if (listMeans != null && listMeans.isNotEmpty()) {
                means?.addAll(listMeans)
            }
        }
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

    /**
     * @param updateMemory : true:之前有存储过该单词的情况下，允许更新记忆数据
     * */
    fun save(updateMemory : Boolean = false,
             okPhonetic : Boolean = false,
             okMean : Boolean = false) {
        val oldBean = findByPrimary<WordBean>(name?:"")
        val now = System.currentTimeMillis()
        if (oldBean == null) {
            // 当不存在oldBean时写入db
            val memBean = MemoryBean(tCreate = now,
                    tLastReview = now,
                    reviewNum = 1,
                    okPhonetic = okPhonetic,
                    okMean = okMean)
            memory?.add(memBean)
//            val box = App.myApp.boxStore.boxFor(WordBean::class.java)
            insertOrUpdate()
            LogCat.d("save $name")
        } else {
            if (updateMemory) {
                var memBean = oldBean.memory?.get(0)
                if (memBean == null) {
                    memBean = MemoryBean(tCreate = now,
                            tLastReview = now,
                            reviewNum = 1,
                            okPhonetic = okPhonetic,
                            okMean = okMean)
                } else {
                    memBean = memBean.copy(tLastReview = now,
                            reviewNum = memBean.reviewNum + 1,
                            okPhonetic = okPhonetic,
                            okMean = okMean)
                }

                memory?.clear()
                memory?.add(memBean)
                insertOrUpdate()
            }
            LogCat.d("already exist $name, update memory.")
        }
    }

    override fun primaryKey(): Property {
        return WordBean_.name
    }

    override fun primaryValue(): String {
        return name ?: ""
    }

    override fun updateDbBean(dbBean: WordBean) : WordBean {
        // 第一种：自己复制
//        val myMemory = memory
//        val oldMemory = dbBean.memory
//        if (oldMemory != null && myMemory?.isNotEmpty() == true) {
//            oldMemory.addAll(myMemory.toList())
//        }

        // 第二种：直接copy
        val newBean = dbBean.copy(name = name, frequence = frequence,
                phoneticSymbol = phoneticSymbol, means = means,
                exchanges = exchanges, memory = memory,
                tag = tag)

        return newBean
    }

    fun initMemoryBean() {
        if (findByPrimary<WordBean>(name?:"") != null) {
            // 已经有存储过了，就不必再初始化MemoryBean了
            return
        }
        val now = System.currentTimeMillis()
        // 当不存在oldBean时写入db
        val memBean = MemoryBean(tCreate = now,
                tLastReview = now,
                reviewNum = 1,
                okPhonetic = false,
                okMean = false)
        memory?.add(memBean)
    }
}
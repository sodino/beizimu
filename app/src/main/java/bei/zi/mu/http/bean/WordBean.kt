package bei.zi.mu.http.bean

import android.text.TextUtils
import bei.zi.mu.App
import bei.zi.mu.LogCat
import bei.zi.mu.ext.d
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
        var name                    : String                    = "",
        var frequence               : Int                       = 0,    // 星级
        var tCreate                 : Long                      = 0,    // 创建时间，即第一次查询该单词的时间
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
        ) : Bean<WordBean>() {

    companion object {
        public fun findFirstByPrimaryKey(value : String) : WordBean?{
            val clazz = WordBean::class.java
            val property = WordBean_.name
            return findFirstByPrimaryKey<WordBean>(clazz, arrayOf(property), arrayOf(value))
        }

        /** 寻找最久远的未复习的单词 */
        public fun find4WordCard() : WordBean? {
            val box = App.myApp.boxStore.boxFor(MemoryBean::class.java)
            val result = box.query()
                .order(MemoryBean_.tLastReview)
                .build()
                .findFirst()

            return result?.word?.target ?: null
        }

        fun findRecent100(): List<WordBean>? {
            val box = App.myApp.boxStore.boxFor(WordBean::class.java)
            val list = box.query()
                        .notEqual(WordBean_.tCreate, 0) // 时间值为0的话为自动导入，不是用户主动查询的。
                        .orderDesc(WordBean_.tCreate)
                    .build()
                    .find(0, 100)
            return list
        }

        fun findByWordTag(type: Int): List<WordBean>? {

            return null
        }
    }

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
        val errno = jsonObj.optInt("errno")
        if (errno != 0) {
            "parse failed. [$response]".d()
            return
        }


        var baseInfo = jsonObj.optJSONObject("baesInfo")    // iciba的数据写错了，竟然写成 baes ！！！
        if (baseInfo == null) {
            "cann't find baseInfo. [$response]".d()
            return
        }

        name = baseInfo.optString("word_name")
        frequence = baseInfo.optInt("frequence")
        tag = parseTag(baseInfo.optJSONArray("word_tag"))

        tCreate = System.currentTimeMillis()

        val jsonExchanges = baseInfo.optJSONObject("exchange")
        if (jsonExchanges != null) {
            val listExchange = ExchangeBean.parse(jsonExchanges)
            if (listExchange != null && listExchange.isNotEmpty()) {
                exchanges?.addAll(listExchange)
            }
        }

        var tmpArr = baseInfo.optJSONArray("symbols")

        var symbols : JSONObject? = null
        if (tmpArr is JSONArray) {
            symbols = tmpArr[0] as JSONObject
        }
        var phonetic : PhoneticSymbol? = null
        if (symbols != null) {
            phonetic = PhoneticSymbol.parse(symbols)
            phoneticSymbol?.add(phonetic)
        }
        val jsonMeans = symbols?.optJSONArray("parts")
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


    override fun primaryKeys(): Array<Property> {
        return arrayOf(WordBean_.name)
    }

    override fun primaryValues(): Array<String> {
        return arrayOf(name ?: "")
    }

    override fun updateDbBean(dbBean: WordBean) : WordBean {
        // 第一种：自己复制
//        val myMemory = memor
//        val oldMemory = dbBean.memory
//        if (oldMemory != null && myMemory?.isNotEmpty() == true) {
//            oldMemory.addAll(myMemory.toList())
//        }

        // 第二种：直接copy，但不复制id。
        val newBean = dbBean.copy(name = name,
                frequence = frequence,
                tCreate = tCreate,
                phoneticSymbol = phoneticSymbol,
                means = means,
                exchanges = exchanges,
                memory = memory,
                tag = tag)

        return newBean
    }

    fun initMemoryBean() {
        if (findByPrimary<WordBean>(arrayOf(name?:"")) != null) {
            // 已经有存储过了，就不必再初始化MemoryBean了
            return
        }
        val now = System.currentTimeMillis()
        // 当不存在oldBean时写入db
        val memBean = MemoryBean(tLastReview = now,
                reviewNum = 1,
                okPhonetic = false,
                okMean = false)
        memory?.add(memBean)
    }

    fun updateMemory(okPhonetic : Boolean = false, okMean : Boolean = false) {
        val memoryBean = memory?.get(0)
        if (memoryBean != null) {
            memoryBean.tLastReview = System.currentTimeMillis()
            memoryBean.reviewNum ++
            memoryBean.okPhonetic = okPhonetic
            memoryBean.okMean = okMean

            // 更新memoryBean
            // insertOrUpdate()无效，不会更新到memoryBean
//            insertOrUpdate()
            memoryBean.insertOrUpdate()
        }
    }
}
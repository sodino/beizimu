package bei.zi.mu

import android.util.AndroidRuntimeException
import bei.zi.mu.http.bean.PhoneticSymbol

/**
 * Created by sodino on 2018/3/4.
 */
class Const {
    object URL{
        const val ICIBA                    = "http://www.iciba.com/"
        const val GITHUB_USER_CONTENT      = "https://raw.githubusercontent.com/"
    }

    object Param {
        const val I_TYPE                   = "type"
        const val S_GROUP                  = "group"
    }

    object SDCard {
        const val APP                      = "/sdcard/beizimu/"
        const val mp3                      = "${APP}/mp3/"
        const val tmpFolder                = "$APP/.tmpFolder/"

        fun mp3Path(word : String, type : Int) : String {
            if (word.isEmpty()) {
                throw AndroidRuntimeException("word.length must > 1")
            }
            return "$mp3${word[0]}/${word}_${if(type==PhoneticSymbol.AM) "am" else "en"}.mp3"
        }

        fun newTmpPath(): String {
            return "${Const.SDCard.tmpFolder}${System.currentTimeMillis()}.tmp"
        }
    }

    object WordTag{
        // 0~5 为iciba的分类
        const val KAO_YAN                  = 0
        const val CET_6                    = 1
        const val CET_4                    = 2
        const val GRE                      = 3
        const val TOEFL                    = 4
        const val IELTS                    = 5
        // 以下为自定义
        const val RECENT_100               = 100
        val map : Map<Int, String>         = mapOf(
                KAO_YAN     to App.myApp.getString(R.string.kaoYan),
                CET_4       to App.myApp.getString(R.string.CET4),
                CET_6       to App.myApp.getString(R.string.CET6),
                GRE         to App.myApp.getString(R.string.GRE),
                TOEFL       to App.myApp.getString(R.string.TOEFL),
                IELTS       to App.myApp.getString(R.string.IELTS),

                RECENT_100  to App.myApp.getString(R.string.recent100)
        )
    }

    object exchange{
        const val plural                   = 1     // 复数
        const val third                    = 2     // 第三人称单数
        const val past                     = 3     // 过去式
        const val done                     = 4     // 过去分词
        const val ing                      = 5     // 现在分词
        const val er                       = 6     // 比较级
        const val est                      = 7     // 最高级
        const val prep                     = 8     // 介词 preposition
        const val adv                      = 9     // 副词 adverb
        const val verb                     = 10    // 动词 verb
        const val noun                     = 11    // 名词
        const val adj                      = 12    // 形容词 adjective
        const val conn                     = 13    // 连接词 Conjunction   if , so that , because , as , since 等..

        val map : Map<Int, String>   = mapOf(
                plural  to "pl",
                third   to "third",
                past    to "past",
                done    to "done",
                ing     to "ing",
                er      to "er",
                est     to "est",
                prep    to "prep",
                adv     to "adv",
                verb    to "verb",
                noun    to "noun",
                adj     to "adj",
                conn    to "conn"
        )
    }

}
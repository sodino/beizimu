package bei.zi.mu

/**
 * Created by sodino on 2018/3/4.
 */
class Const {
    object URL{
        const val ICIBA                    = "http://www.iciba.com/"
        const val GITHUB_USER_CONTENT      = "https://raw.githubusercontent.com/"
    }

    object WordTag{
        const val KAO_YAN                  = 0
        const val CET_6                    = 1
        const val CET_4                    = 2
        const val TOEFL                    = 4
        const val IELTS                    = 5
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
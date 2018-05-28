package bei.zi.mu.http.bean

import io.objectbox.Property
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import org.json.JSONObject

/**
 * Created by sodino on 2018/3/6.
 */
@Entity
data class PhoneticSymbol(
        @Id
        var id                  : Long                  = 0,
        var en                  : String?               = null, // phonetic symbol english
        var en_mp3              : String?               = null,
        var am                  : String?               = null, // phonetic symbol american
        var am_mp3              : String?               = null,
        var tts_mp3             : String?               = null,
        var word                : ToOne<WordBean>?      = null
        ) : Bean<PhoneticSymbol>() {


    override fun primaryKeys(): Array<Property> {
        return arrayOf(PhoneticSymbol_.id)
    }

    override fun primaryValues(): Array<Long> {
        return arrayOf(id)
    }

    override fun updateDbBean(dbBean: PhoneticSymbol) : PhoneticSymbol{
        return dbBean
    }

    override fun isFilled(): Boolean {
            val result = en?.isNotEmpty()?:false
            || en_mp3?.isNotEmpty()?:false
            || am?.isNotEmpty()?:false
            || am_mp3?.isNotEmpty()?:false
            || tts_mp3?.isNotEmpty()?:false

            return result
        }

    companion object {
        const val EN        = 0
        const val AM        = 1

        fun parse(json : JSONObject) : PhoneticSymbol {
            val ph_en = json.optString("ph_en")
            val ph_en_mp3 = json.optString("ph_en_mp3")
            val ph_am = json.optString("ph_am")
            val ph_am_mp3 = json.optString("ph_am_mp3")
            val ph_tts_mp3 = json.optString("ph_tts_mp3")

            return PhoneticSymbol(en = ph_en, en_mp3 = ph_en_mp3,
                    am = ph_am, am_mp3 = ph_am_mp3,
                    tts_mp3 = ph_tts_mp3)
        }
    }
}
package bei.zi.mu.http.bean

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import org.json.JSONObject

/**
 * Created by sodino on 2018/3/6.
 */
data class PhoneticSymbol(
        @Id
        var id                  : Long                  = 0,
        var en                  : String?               = null, // phonetic symbol english
        var en_mp3              : String?               = null,
        var am                  : String?               = null, // phonetic symbol american
        var am_map3             : String?               = null,
        var tts_mp3             : String?               = null,
        var word                : ToOne<WordBean>?      = null
        ) : BeanInterface {
        override fun isFilled(): Boolean {
                return true
        }

        companion object {
            fun parse(json : JSONObject) : PhoneticSymbol {
                val ph_en = json.optString("ph_en")
                val ph_en_mp3 = json.optString("ph_en_mp3")
                val ph_am = json.optString("ph_am")
                val ph_am_mp3 = json.optString("ph_am_mp3")
                val ph_tts_mp3 = json.optString("ph_tts_mp3")

                return PhoneticSymbol(en = ph_en, en_mp3 = ph_en_mp3,
                        am = ph_am, am_map3 = ph_am_mp3,
                        tts_mp3 = ph_tts_mp3)
            }
        }
}
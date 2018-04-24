package bei.zi.mu.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import bei.zi.mu.BaseActivity
import bei.zi.mu.Const
import bei.zi.mu.R
import bei.zi.mu.TitlebarActivity

/**
 * Created by sodino on 2018/4/24.
 */
public class WordListActivity : TitlebarActivity(), View.OnClickListener {
    var type : Int              = R.id.txtRecent100


    companion object {
        val mapType2Title by lazy { mapOf(
                R.id.txtRecent100   to R.string.recent100,
                R.id.txtCET4        to R.string.CET4,
                R.id.txtCET6        to R.string.CET6,
                R.id.txtToefl       to R.string.TOEFL,
                R.id.txtGRE         to R.string.GRE,
                R.id.txtKaoYan      to R.string.kaoYan
        ) }
        public fun launch(context : Context, id : Int) {
            val intent = Intent(context, WordListActivity::class.java)
            intent.putExtra(Const.Param.I_TYPE, id)
            context.startActivity(intent)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.word_list_activity)

        type = intent.getIntExtra(Const.Param.I_TYPE, R.id.txtRecent100)
        setTitle(mapType2Title.get(type)?:R.string.recent100)

    }

    override fun onClick(v: View) {
    }

}
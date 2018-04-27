package bei.zi.mu.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import bei.zi.mu.Const
import bei.zi.mu.R
import bei.zi.mu.TitlebarActivity
import bei.zi.mu.http.bean.PhoneticSymbol
import bei.zi.mu.http.bean.WordBean
import bei.zi.mu.mvp.SearchActivity.Presenter
import bei.zi.mu.util.playMp3
import bei.zi.mu.util.showToast
import kotlinx.android.synthetic.main.search_activity.*

/**
 * Created by sodino on 2018/3/4.
 */
public class SearchActivity : TitlebarActivity<Presenter>(), View.OnClickListener, TextView.OnEditorActionListener, bei.zi.mu.mvp.SearchActivity.View {
    lateinit var editWord   : EditText
    var wordBean            : WordBean? = null
//    val presenter           : Presenter by lazy { Presenter(this@SearchActivity) }


    companion object {
        fun launch(context : Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
    }

    override fun createTitlebar(parentLayout: LinearLayout): View {
        layoutInflater.inflate(R.layout.titlebar_search, parentLayout, true)
        val relLayout = parentLayout.findViewById<RelativeLayout>(R.id.titlebar_layout)
        val txtCancel = relLayout.findViewById<TextView>(R.id.txtCancel)
        txtCancel.setOnClickListener(this)

        editWord = relLayout.findViewById(R.id.searchEdit)
        editWord.setOnEditorActionListener(this)

        return relLayout
    }


    override fun onClick(v: View) {
        when(v.id) {
            R.id.txtCancel      -> { finish() }
            R.id.txtPhoneticAm  -> { wordBean?.phoneticSymbol?.get(0)?.playMp3(PhoneticSymbol.AM) }
            R.id.txtPhoneticEn  -> { wordBean?.phoneticSymbol?.get(0)?.playMp3(PhoneticSymbol.EN) }
        }
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        when(actionId) {
            EditorInfo.IME_ACTION_SEARCH -> {
                val word = editWord.text.toString().trim()
                if (TextUtils.isEmpty(word)) {
                    "Please enter a word".showToast()
                    return true
                }

                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
//                searchWord(word)
                dlgLoading.setMessage(getString(R.string.searching))
                dlgLoading.show()
                presenter.reqWord(word)
            }
        }
        return true
    }

    private fun showWordDetail(bean: WordBean) {
        wordBean = bean
        txtWord.text = bean.name
        barRating.visibility = View.VISIBLE
        barRating.rating = bean.frequence.toFloat()

        if (bean.phoneticSymbol?.isNotEmpty() == true) {
            val phonetic = bean.phoneticSymbol?.get(0)

            layoutPhonetic.visibility = View.VISIBLE
            txtPhoneticEn.text = "en\n[${phonetic?.en}]"
            txtPhoneticAm.text = "am\n[${phonetic?.am}]"

            txtPhoneticEn.setOnClickListener(this@SearchActivity)
            txtPhoneticAm.setOnClickListener(this@SearchActivity)
        } else {
            layoutPhonetic.visibility = View.GONE
        }

        if (bean.means?.isNotEmpty() == true) {
            var strMeans = ""
            bean.means?.forEach {
                if (strMeans.isNotEmpty()) {
                    strMeans += "\n"
                }
                strMeans += it.part + " " + it.mean
            }

            txtMeans.setText(strMeans)
        } else {
            txtMeans.visibility = View.GONE
        }

        val tag = bean.tag
        if (tag != null && tag.isNotEmpty()) {
            var strTag = ""
            tag.split(",").forEach {
                if (strTag.length > 0) {
                    strTag += " "
                }
                strTag += Const.WordTag.map.get(it.toInt())
            }

            txtTag.setText(strTag)
        }
    }

    override fun respWord(bean: WordBean) {
        dismissSearchingDialog()
        showWordDetail(bean)
    }

    override fun respError() {
        dismissSearchingDialog()
    }

    fun dismissSearchingDialog() {
        if (dlgLoading.isShowing) {
            dlgLoading.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}
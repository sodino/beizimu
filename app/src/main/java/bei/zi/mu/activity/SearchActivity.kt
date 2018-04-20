package bei.zi.mu.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
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
import bei.zi.mu.LogCat
import bei.zi.mu.R
import bei.zi.mu.TitlebarActivity
import bei.zi.mu.http.bean.WordBean
import bei.zi.mu.http.retrofit.ARetrofit
import bei.zi.mu.http.retrofit.BeanCallback
import bei.zi.mu.util.showToast
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.search_activity.*
import retrofit2.Call
import retrofit2.Response

/**
 * Created by sodino on 2018/3/4.
 */
class SearchActivity : TitlebarActivity(), View.OnClickListener, TextView.OnEditorActionListener, Handler.Callback {
    val MSG_SHOW_WORD                   = 1

    lateinit var editWord   : EditText
    var wordBean            : WordBean? = null
    val handler                         = Handler(this@SearchActivity)


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

    override fun handleMessage(msg: Message?): Boolean {
        val what = msg?.what?:-1
        when(what) {
            MSG_SHOW_WORD -> {
                val bean = msg?.obj as WordBean
                wordBean = bean
                showWordDetail(bean)
            }
        }

        return true
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
            R.id.txtCancel -> { finish() }
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
                searchWord(word)
            }
        }
        return true
    }

    fun searchWord(word : String) {
//        val findBean = WordBean.findWord(word)
        reqWord(word)

    }

    fun reqWord(word: String) {
        var single = Single.just(word)
        single = single.subscribeOn(Schedulers.io())
//        single.do
    }

    fun reqWordByBeanCallback(word: String) {
        val api = ARetrofit.wordApi
//        var map = ARetrofit.baseQueryMap()
//        map = map.plus(Pair("word", word))
//        val call = api.reqIciba(map)
        val call = api.reqGithubWord(word[0].toString(), word)
        call.enqueue(object : BeanCallback<WordBean>(false) {
            override fun onResponse(bean: WordBean, isFilled: Boolean) {
                LogCat.d("isFilled=${isFilled}")
                if (isFilled) {
                    bean.initMemoryBean()
                    bean.insertOrUpdate()
                    var strMeans = bean.name + "\n"
                    bean.means?.forEach { strMeans += "${it.part} ${it.mean}\n" }


                    val msg = Message.obtain(handler, MSG_SHOW_WORD, bean)
                    msg.sendToTarget()
                } else {
                    "$word isFilled=false".showToast()
                }
            }

            override fun onFailure(call: Call<WordBean>, t: Throwable?, response: Response<*>?, respCode: Int) {
                LogCat.d("respCode=${respCode}")
                "$word failure, respCode=$respCode".showToast()
            }
        })
    }

    private fun showWordDetail(bean: WordBean) {
        txtWord.setText(bean.name)
        barRating.visibility = View.VISIBLE
        barRating.rating = bean.frequence.toFloat()

        if (bean.phoneticSymbol?.isNotEmpty() == true) {
            val phonetic = bean.phoneticSymbol?.get(0)

            layoutPhonetic.visibility = View.VISIBLE
            txtPhoneticEn.setText("en\n[${phonetic?.en}]")
            txtPhoneticAm.setText("am\n[${phonetic?.am}]")

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
}
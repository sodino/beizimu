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
import bei.zi.mu.LogCat
import bei.zi.mu.R
import bei.zi.mu.TitlebarActivity
import bei.zi.mu.http.bean.WordBean
import bei.zi.mu.http.retrofit.ARetrofit
import bei.zi.mu.http.retrofit.BeanCallback
import bei.zi.mu.util.showToast
import retrofit2.Call
import retrofit2.Response

/**
 * Created by sodino on 2018/3/4.
 */
class SearchActivity : TitlebarActivity(), View.OnClickListener, TextView.OnEditorActionListener {

    lateinit var editWord : EditText

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

                reqWord(word)
            }
        }
        return true
    }

    fun reqWord(word: String) {
        val api = ARetrofit.wordApi
//        var map = ARetrofit.baseQueryMap()
//        map = map.plus(Pair("word", word))
//        val call = api.reqIciba(map)
        val call = api.reqGithubWord(word[0].toString(), word)
        call.enqueue(object : BeanCallback<WordBean>(){
            override fun onResponse(bean: WordBean, isFilled: Boolean) {
                LogCat.d("isFilled=${isFilled}")
                if (isFilled) {
                    bean.save()
                    var strMeans = bean.name + "\n"
                    bean.means?.forEach { strMeans += "${it.part} ${it.mean}\n" }
                    strMeans.showToast()
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
}
package bei.zi.mu.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import bei.zi.mu.Const
import bei.zi.mu.R
import bei.zi.mu.TitlebarActivity
import bei.zi.mu.dialog.WordGroupDialog
import bei.zi.mu.ext.hideSoftImputFromWindow
import bei.zi.mu.ext.showToast
import bei.zi.mu.http.bean.GroupNameBean
import bei.zi.mu.http.bean.PhoneticSymbol
import bei.zi.mu.http.bean.WordBean
import bei.zi.mu.mvp.SearchActivity.Presenter
import bei.zi.mu.util.playMp3
import kotlinx.android.synthetic.main.search_activity.*
import kotlinx.android.synthetic.main.titlebar_search.*

/**
 * Created by sodino on 2018/3/4.
 */
public class SearchActivity : TitlebarActivity<Presenter>(), View.OnClickListener, TextView.OnEditorActionListener, bei.zi.mu.mvp.SearchActivity.View {
    lateinit var editWord   : EditText
    lateinit var imgClear   : ImageView
    var wordBean            : WordBean? = null
//    val presenter           : Presenter by lazy { Presenter(this@SearchActivity) }
    val textWatcher         = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            val string = s?.toString() ?: ""
            imgClear.visibility = if (string.isEmpty()) { View.INVISIBLE } else { View.VISIBLE }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }

    companion object {
        fun launch(context : Context, word : String = "") {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra(Const.Param.S_WORD, word)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)


        val word = intent.getStringExtra(Const.Param.S_WORD) ?: ""
        if (word.isNotEmpty()) {
            editWord.setText(word)
            try2RequestWord()
        }
    }

    override fun createTitlebar(parentLayout: LinearLayout): View {
        layoutInflater.inflate(R.layout.titlebar_search, parentLayout, true)
        val relLayout = parentLayout.findViewById<RelativeLayout>(R.id.titlebar_layout)
        val txtCancel = relLayout.findViewById<TextView>(R.id.txtCancel)
        txtCancel.setOnClickListener(this)

        editWord = relLayout.findViewById(R.id.searchEdit)
        editWord.setOnEditorActionListener(this)
        editWord.addTextChangedListener(textWatcher)


        imgClear = relLayout.findViewById(R.id.imgClear)
        imgClear.setOnClickListener(this)

        return relLayout
    }


    override fun onClick(v: View) {
        when(v.id) {
            R.id.txtCancel      -> { finish() }
            R.id.imgClear       -> { editWord.setText("") }
            R.id.txtPhoneticAm  -> { wordBean?.phoneticSymbol?.get(0)?.playMp3(PhoneticSymbol.AM) }
            R.id.txtPhoneticEn  -> { wordBean?.phoneticSymbol?.get(0)?.playMp3(PhoneticSymbol.EN) }
            R.id.txtAdd2Group   -> { showWordGroupDialog() }
        }
    }

    fun showWordGroupDialog() {
        val word = wordBean
        if (word == null) {
            return
        }
        val listGroup = GroupNameBean.all()
        val dlg = WordGroupDialog(word, this@SearchActivity, listGroup)
        dlg.show()
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        when(actionId) {
            EditorInfo.IME_ACTION_SEARCH -> {
                try2RequestWord()
            }
        }
        return true
    }

    fun try2RequestWord() {
        val word = editWord.text.toString().trim()
        if (TextUtils.isEmpty(word)) {
            "Please enter a word".showToast()
            return
        }

        if (currentFocus != null) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
//                searchWord(word)
        dlgLoading.setMessage(getString(R.string.searching))
        dlgLoading.show()
        presenter.reqWord2(word)
    }

    private fun showWordDetail(bean: WordBean) {
        wordBean = bean
        txtWord.text = bean.name

        txtAdd2Group.visibility = View.VISIBLE

        barRating.visibility = View.VISIBLE
        barRating.rating = bean.frequence.toFloat()

        if (bean.phoneticSymbol?.isNotEmpty() == true) {
            val phonetic = bean.phoneticSymbol?.get(0)

            layoutPhonetic.visibility = View.VISIBLE
            txtPhoneticEn.text = "en\n[${phonetic?.en}]"
            txtPhoneticAm.text = "am\n[${phonetic?.am}]"

            txtPhoneticEn.setOnClickListener(this@SearchActivity)
            txtPhoneticAm.setOnClickListener(this@SearchActivity)
            txtAdd2Group.setOnClickListener(this@SearchActivity)
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
//        editWord.hideSoftImputFromWindow()
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
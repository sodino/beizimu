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
import bei.zi.mu.R
import bei.zi.mu.TitlebarActivity
import bei.zi.mu.util.showToast

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
                val text = editWord.text.toString().trim()
                if (TextUtils.isEmpty(text)) {
                    "Please enter a word".showToast(this@SearchActivity)
                    return true
                }

                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)


            }
        }
        return true
    }
}
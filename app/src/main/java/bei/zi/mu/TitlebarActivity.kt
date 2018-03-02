package bei.zi.mu

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

/**
 * Created by sodino on 2018/2/26.
 */
@SuppressLint("Registered")
open class TitlebarActivity : BaseActivity() {
    protected lateinit  var viewContent         : View
    protected           var viewTitlebar        : View?         = null
    protected           var titleText           : TextView?     = null
    protected           var titleRightView      : View?         = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
        var v = LayoutInflater.from(this).inflate(layoutResID, null)
        setContentView(v)
    }

    override fun setContentView(view: View) {
        viewContent = view

        var linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.isScrollContainer = true

        viewStatusbarBackground = createStatusbar(linearLayout)
        viewTitlebar = createTitlebar(linearLayout)

        linearLayout.addView(viewContent, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))

        super.setContentView(linearLayout)

    }

    open fun setContentViewNoTitlebar(layoutResId: Int) {
        val v = LayoutInflater.from(this).inflate(layoutResId, null)
        setContentViewNoTitlebar(v)
    }

    open fun setContentViewNoTitlebar(view : View) {
        viewContent = view;
        super.setContentView(view)
    }

    override fun setTitle(stringId : Int) {
        titleText?.let { titleText -> titleText.setText(stringId) }
    }

    override fun setTitle(title : CharSequence) {
        titleText?.let { titleText -> titleText.setText(title) }
    }

    open fun createStatusbar(parentLayout: LinearLayout): View {
        LayoutInflater.from(this).inflate(R.layout.transparent_status_bar_bg_view, parentLayout, true)
        val v = parentLayout.findViewById<View>(R.id.status_bar_background)
        return v
    }

    open fun createTitlebar(parentLayout: LinearLayout): View {
        LayoutInflater.from(this).inflate(R.layout.titlebar, parentLayout, true)
        val relLayout = parentLayout.findViewById<RelativeLayout>(R.id.titlebar_layout)

        titleRightView = createTitlebarRightView(relLayout)
        titleRightView?.let {titleRightView ->
            titleRightView.id = R.id.titlebar_right
            val lParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            lParams.addRule(RelativeLayout.ALIGN_PARENT_END)
            lParams.addRule(RelativeLayout.CENTER_VERTICAL)
            relLayout.addView(titleRightView, lParams)
        }

        titleText = relLayout.findViewById(R.id.titlebar_title)

        return relLayout
    }

    open fun createTitlebarRightView(relLayout: RelativeLayout): View? {
        return null
    }
}
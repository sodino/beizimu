package bei.zi.mu

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout

/**
 * Created by sodino on 2018/2/26.
 */
@SuppressLint("Registered")
open class TitlebarActivity : BaseActivity() {
    protected lateinit  var vContent            : View
    protected           var vTitlebar           : View?         = null
    protected lateinit  var titleText           : View
    protected           var titleRightView      : View?         = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
        var v = LayoutInflater.from(this).inflate(layoutResID, null)
        setContentView(v)
    }

    override fun setContentView(view: View) {
        vContent = view

        var linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.isScrollContainer = true

        super.setContentView(view)

    }
}
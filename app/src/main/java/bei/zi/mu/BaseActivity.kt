package bei.zi.mu

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.AndroidRuntimeException
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import bei.zi.mu.util.Device
import bei.zi.mu.util.Statusbar

/**
 * Created by sodino on 2018/2/26.
 */
@SuppressLint("Registered")
open class BaseActivity : FragmentActivity() {
    protected lateinit  var rootView                 : View
    protected           var viewStatusbarBackground  : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogCat.d("${this@BaseActivity.javaClass.simpleName}@${Integer.toHexString(hashCode())}")
    }

    override fun setContentView(layoutResID: Int) {
        val v = LayoutInflater.from(this).inflate(layoutResID, null)
        setContentView(v)
    }

    override fun setContentView(view: View) {
        rootView = view
        super.setContentView(view)
        if (isFixTransparentStatusBar()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 在kotlin中用 or 代替 java 中的'|'
                var visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 亮色模式,避免系统状态栏的图标不可见
                    visibility = visibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
                window.decorView.systemUiVisibility = visibility
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT

                fixTransparentStatusBar(view)
                // 最后fix一下状态栏背景白色与系统的文字图标白色的问题
                fixTransparentStatusBarWhiteTextColor(view, viewStatusbarBackground)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

                fixTransparentStatusBar(view)
                // 最后fix一下状态栏背景白色与系统的文字图标白色的问题
                fixTransparentStatusBarWhiteTextColor(view, viewStatusbarBackground)
            } else {
                setStatusbarBackgroundGone()
            }
        } else {
            setStatusbarBackgroundGone()
        }
    }

    open fun setStatusbarBackgroundGone() {
        val vStatusbarBackground = viewStatusbarBackground

        if (vStatusbarBackground != null && vStatusbarBackground.visibility != View.GONE) {
            vStatusbarBackground.visibility = View.GONE
        }

        val v = findViewById<View>(R.id.status_bar_background)
        // setContentViewNoTitlebar()的话，viewStatusbarBackground为null
        if (v != null && v.visibility != View.GONE) {
            v.visibility = View.GONE
        }
    }

    open fun fixTransparentStatusBar(view: View) {
        // 当出现自定义TransparentStatusbarView时，重载处理
    }

    open fun isFixTransparentStatusBar() : Boolean {
        return true;
    }

    open fun fixTransparentStatusBarWhiteTextColor(rootView :View, viewTSBarBg : View?) : Boolean {
        val vTSBarBg = viewTSBarBg
        if (Device.isMeizu()) {
            Statusbar.fix(Device.MEIZU, this, false)
            return true
        } else if (Device.isMIUI()) {
            Statusbar.fix(Device.XIAOMI, this, false)
            return true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // M及以上会使用亮色模式,不需要修改
            return false
        } else if (vTSBarBg != null && vTSBarBg.visibility == View.VISIBLE) {
            // 其它的机子在android 4.4到5.2之间的，都没办法改状态栏图标及文字的颜色，所以要改背景
            vTSBarBg.setBackgroundResource(R.drawable.status_bar_background)
            return true
        } else {
            return false
        }
    }



    override fun onStart() {
        super.onStart()
        LogCat.d("${this@BaseActivity.javaClass.simpleName}@${Integer.toHexString(hashCode())}")
    }

    override fun onRestart() {
        super.onRestart()

        LogCat.d("${this@BaseActivity.javaClass.simpleName}@${Integer.toHexString(hashCode())}")
    }

    override fun onResume() {
        super.onResume()
        LogCat.d("${this@BaseActivity.javaClass.simpleName}@${Integer.toHexString(hashCode())}")
    }

    override fun onPause() {
        super.onPause()
        LogCat.d("${this@BaseActivity.javaClass.simpleName}@${Integer.toHexString(hashCode())}")
    }

    override fun onStop() {
        super.onStop()
        LogCat.d("${this@BaseActivity.javaClass.simpleName}@${Integer.toHexString(hashCode())}")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogCat.d("${this@BaseActivity.javaClass.simpleName}@${Integer.toHexString(hashCode())}")
    }

    @Deprecated("Use doBackPressed() instead")
    override fun onBackPressed() {
        super.onBackPressed()
        throw AndroidRuntimeException("Use doBackPressed() instead")
    }

    open fun doBackPressed() :Boolean{
        finish()
        return true;
    }

    override fun onKeyUp(keyCode : Int, event : KeyEvent) :Boolean {
        if (applicationInfo.targetSdkVersion >= Build.VERSION_CODES.ECLAIR) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking
                    && !event.isCanceled) {
                val bool = doBackPressed()
                if (bool) {
                    return true
                } else {
                    // 没消耗掉的话，默认finish掉
                    super.onBackPressed()
                    finish()
                    return true
                }
            }
        }
        return false
    }
}
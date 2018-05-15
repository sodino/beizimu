package bei.zi.mu

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.AndroidRuntimeException
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import bei.zi.mu.ext.hexString
import bei.zi.mu.mvp.VoidPresenter
import bei.zi.mu.util.Device
import bei.zi.mu.util.Statusbar
import java.lang.reflect.ParameterizedType

/**
 * Created by sodino on 2018/2/26.
 */
@SuppressLint("Registered")
open class BaseActivity<PresenterType> : FragmentActivity() {
    protected lateinit  var rootView                 : View
    protected           var viewStatusbarBackground  : View? = null
    protected           val dlgLoading               : ProgressDialog by lazy {
        val dlg = ProgressDialog(this@BaseActivity)
        dlg.setCancelable(false)
        dlg.setCanceledOnTouchOutside(false)
//        dlg.setMessage(getString(R.string.searching))
        dlg
    }

    protected           val presenter                : PresenterType by lazy {
        val thiz = this@BaseActivity

        val clazzPresenter = (thiz::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>

        if (VoidPresenter::class.java == clazzPresenter) {
            VoidPresenter.void as PresenterType
        } else {
            val constructor = clazzPresenter.constructors[0]
            constructor.isAccessible = true

            constructor.newInstance(thiz) as PresenterType
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogCat.d("${javaClass.simpleName}@${hashCode().hexString()}")

        if (presenter == null) {

        }
    }

    override fun setContentView(layoutResID: Int) {
        val v = LayoutInflater.from(this).inflate(layoutResID, null)
        setContentView(v)
    }

    override fun setContentView(view: View) {
        rootView = view
        super.setContentView(view)
        setTransparentStatusbar(view)
    }

    protected fun setTransparentStatusbar(view: View?) {
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
                fixTransparentStatusBarWhiteTextColor(viewStatusbarBackground)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

                fixTransparentStatusBar(view)
                // 最后fix一下状态栏背景白色与系统的文字图标白色的问题
                fixTransparentStatusBarWhiteTextColor(viewStatusbarBackground)
            } else {
                setStatusbarBackgroundGone()
            }
        } else {
            setStatusbarBackgroundGone()
        }
    }

    @SuppressLint("MissingSuperCall")
    public override fun onSaveInstanceState(outState: Bundle?) {
        // 注释掉super,否则二级Activity崩溃的话，首页Fragment会白屏（华为、美图手机上）
        //        super.onSaveInstanceState(outState);
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

    open fun fixTransparentStatusBar(view: View?) {
        // 当出现自定义TransparentStatusbarView时，重载处理
    }

    open fun isFixTransparentStatusBar() : Boolean {
        return true;
    }

    open fun fixTransparentStatusBarWhiteTextColor(viewTSBarBg : View?) : Boolean {
        if (Device.isMeizu()) {
            Statusbar.fix(Device.MEIZU, this, true)
            return true
        } else if (Device.isMIUI()) {
            Statusbar.fix(Device.XIAOMI, this, true)
            return true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // M及以上会使用亮色模式,不需要修改
            return false
        } else if (viewTSBarBg != null && viewTSBarBg.visibility == View.VISIBLE) {
            // 其它的机子在android 4.4到5.2之间的，都没办法改状态栏图标及文字的颜色，所以要改背景
            viewTSBarBg.setBackgroundResource(R.drawable.status_bar_background)
            return true
        } else {
            return false
        }
    }



    override fun onStart() {
        super.onStart()
        LogCat.d("${javaClass.simpleName}@${hashCode().hexString()}")
    }

    override fun onRestart() {
        super.onRestart()

        LogCat.d("${javaClass.simpleName}@${hashCode().hexString()}")
    }

    override fun onResume() {
        super.onResume()
        LogCat.d("${javaClass.simpleName}@${hashCode().hexString()}")
    }

    override fun onPause() {
        super.onPause()
        LogCat.d("${javaClass.simpleName}@${hashCode().hexString()}")
    }

    override fun onStop() {
        super.onStop()
        LogCat.d("${javaClass.simpleName}@${hashCode().hexString()}")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogCat.d("${javaClass.simpleName}@${hashCode().hexString()}")
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
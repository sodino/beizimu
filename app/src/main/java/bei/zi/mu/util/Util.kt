package bei.zi.mu.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Looper
import android.widget.Toast
import bei.zi.mu.App
import bei.zi.mu.Const
import bei.zi.mu.LogCat
import bei.zi.mu.thread.ThreadPool

/**
 * Created by sodino on 2018/2/27.
 */

fun String.iciba() {
    // http://www.iciba.com/index.php?a=getWordMean&c=search&list=1%2C3%2C4%2C8%2C9%2C12%2C13%2C15&word=flat

}

fun String.showToast() {
    Toast.makeText(App.myApp, this, Toast.LENGTH_SHORT).show()
}



fun Long.hexString() : String {
    return java.lang.Long.toHexString(this)
}

fun Int.showToast() {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        Toast.makeText(App.myApp, App.myApp.getText(this), Toast.LENGTH_SHORT).show()
    } else {
        ThreadPool.UIHandler.post({
            Toast.makeText(App.myApp, App.myApp.getText(this), Toast.LENGTH_SHORT).show()
        })
    }
}

fun Int.hexString() : String {
    return java.lang.Integer.toHexString(this)
}

object Device {
    val BASE = 1000
    val XIAOMI = BASE + BASE
    val MEIZU = XIAOMI + BASE
    fun isMeizu(): Boolean {
        var bool = Build.BOARD.toLowerCase().contains("meizu")
        return bool
    }
    fun isMIUI(): Boolean {
        var bool = Build.BOARD.toLowerCase().contains("xiaomi")
        return bool
    }
}

object Statusbar {
    fun fix(phone : Int, baseActivity : Activity, dark : Boolean){
        ThreadPool.UIHandler.post { ->
            when(phone){
                Device.MEIZU -> {fixMeizu(baseActivity, dark)}
                Device.XIAOMI -> {fixXiaomi(baseActivity, dark)}
            }
        }
    }

    fun fixXiaomi(baseActivity: Activity, dark: Boolean) {
        val clazz = baseActivity.window.javaClass
        try {
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = baseActivity.window.javaClass.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            extraFlagField.invoke(baseActivity.window, if (dark) {darkModeFlag} else {0}, darkModeFlag)
        } catch (e : Exception) {
            LogCat.e(throwable = e)
        }
    }

    fun fixMeizu(baseActivity: Activity, dark: Boolean) {
        MeizuStatusbarColorUtils.setStatusBarDarkIcon(baseActivity, dark)
    }

    fun isFixWhiteTextColor() : Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 有亮色模式，也就不需要处理白色的文字冲突了
            return false
        } else if (Device.isMIUI()) {
            // MiUI自家的方法，也就不需要处理白色的文字冲突了
            return false
        } else if (Device.isMeizu()) {
            // Meizu自家的方法，也就不需要处理白色的文字冲突了
            return false
        }
        return true
    }
}
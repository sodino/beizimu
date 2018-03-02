package bei.zi.mu.util

import android.app.Activity
import android.os.Build
import bei.zi.mu.LogCat
import bei.zi.mu.thread.ThreadPool

/**
 * Created by sodino on 2018/2/27.
 */

fun Long.hexString() : String {
    return java.lang.Long.toHexString(this)
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
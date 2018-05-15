package bei.zi.mu.util

import android.app.Activity
import android.os.Build
import android.os.Looper
import android.widget.Toast
import bei.zi.mu.App
import bei.zi.mu.Const
import bei.zi.mu.LogCat
import bei.zi.mu.ext.download2sdcard
import bei.zi.mu.ext.showToast
import bei.zi.mu.http.bean.PhoneticSymbol
import bei.zi.mu.player.MP3Player
import bei.zi.mu.thread.ThreadPool
import io.reactivex.schedulers.Schedulers
import java.io.File
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.BufferedSink
import okio.BufferedSource
import okio.Okio


/**
 * Created by sodino on 2018/2/27.
 */

//fun String.iciba() {
//    // http://www.iciba.com/index.php?a=getWordMean&c=search&list=1%2C3%2C4%2C8%2C9%2C12%2C13%2C15&word=flat
//
//}

fun PhoneticSymbol.playMp3(type : Int = PhoneticSymbol.EN) {
    Schedulers.newThread().createWorker().schedule {
        val phoneticSymbol = PhoneticSymbol@this
        val word = phoneticSymbol.word?.target?.name ?: ""
        val filePath = Const.SDCard.mp3Path(word, type)
        val f = File(filePath)
        if (f.exists()) {
            MP3Player.play(filePath)
        } else {
            "downloading $word mp3".showToast()
            val url = if (type == PhoneticSymbol.AM) phoneticSymbol.am_mp3 else phoneticSymbol.en_mp3
            val isDownload = url?.download2sdcard(filePath) ?: false
            if (isDownload) {
                MP3Player.play(filePath)
            } else {
                "download $word mp3 failed.".showToast()
            }
        }
    }
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
            val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
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
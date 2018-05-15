package bei.zi.mu.util

import android.app.Activity
import android.os.Build
import android.os.Looper
import android.widget.Toast
import bei.zi.mu.App
import bei.zi.mu.Const
import bei.zi.mu.LogCat
import bei.zi.mu.http.bean.PhoneticSymbol
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

fun PhoneticSymbol.playMp3(type : Int) {
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


fun String.download2sdcard(filePath : String) : Boolean {
    val tmpPath = Const.SDCard.newTmpPath()
    val tmpFile = File(tmpPath)
    if (tmpFile.exists()) {
        tmpFile.delete()
    }
    if (!tmpFile.parentFile.exists()) {
        tmpFile.parentFile.mkdirs()
    }
    tmpFile.createNewFile()

    var source : BufferedSource? = null
    var sink : BufferedSink?  = null

    var totalBytesRead = 0L
    var contentLength : Long? = 0L
    try {
        val req = Request.Builder().url(this).build()
        val resp = OkHttpClient().newCall(req).execute()
        val body = resp.body()
        contentLength = body?.contentLength()?.toLong()
        source = body?.source()
        sink = Okio.buffer(Okio.sink(tmpFile))
        val sinkBuffer = sink.buffer()
        val bufferSize = 8 * 1024L

        var bytesRead : Long = 0L

        while (true) {
            bytesRead = source?.read(sinkBuffer, bufferSize) ?: -1L
            if (bytesRead == -1L) {
                break
            }

            sink.emit()
            totalBytesRead += bytesRead
        }

        sink.flush()
    } catch(t : Throwable) {
        LogCat.e("download mp3 errors", t)
    } finally {
        source?.close()
        sink?.close()
    }


    var result = totalBytesRead == contentLength
    if (result) {
        val fResult = File(filePath)
        if (!fResult.parentFile.exists()) {
            fResult.parentFile.mkdirs()
        }
        result = tmpFile.renameTo(File(filePath))
    }
    return result
}

fun String.showToast() {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        Toast.makeText(App.myApp, this, Toast.LENGTH_SHORT).show()
    } else {
        ThreadPool.UIHandler.post({
            Toast.makeText(App.myApp, this, Toast.LENGTH_SHORT).show()
        })
    }
}

fun Int.showToast() {
    ((App.myApp.getText(this))as String).showToast()
}



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
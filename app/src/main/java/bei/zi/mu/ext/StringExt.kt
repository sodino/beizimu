package bei.zi.mu.ext

import android.os.Looper
import android.widget.Toast
import bei.zi.mu.App
import bei.zi.mu.Const
import bei.zi.mu.LogCat
import bei.zi.mu.http.bean.WordBean
import bei.zi.mu.http.retrofit.ARetrofit
import bei.zi.mu.thread.ThreadPool
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.BufferedSink
import okio.BufferedSource
import okio.Okio
import java.io.File


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

fun String.reqWord() : WordBean? {
    val word = this
    val findResult = WordBean.findFirstByPrimaryKey(word)
    if (findResult != null) {
        return findResult
    } else {
        val resp = ARetrofit.wordApi.reqGithubWord(word[0].toString(), word).execute()
        val bean = resp.body()
        if (bean?.isFilled() == true) {
            bean?.initMemoryBean()
            bean?.insertOrUpdate()
        }
        return bean
    }
}

fun String.d() {
    LogCat.d(this)
}
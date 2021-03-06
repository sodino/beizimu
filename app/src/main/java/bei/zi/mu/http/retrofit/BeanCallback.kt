package bei.zi.mu.http.retrofit

import android.text.TextUtils
import bei.zi.mu.App
import bei.zi.mu.Const
import bei.zi.mu.LogCat
import bei.zi.mu.R
import bei.zi.mu.http.bean.Bean
import bei.zi.mu.http.bean.ErrorBean
import bei.zi.mu.util.showToast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.nio.charset.Charset



data class UrlTypeName(val type: String, val urlSuffix : String)
fun String.getUrlTypeName() : UrlTypeName {
    var type : String
    var suffix : String?

    when {
        this.startsWith(Const.URL.ICIBA)                -> {
            type = "iciba"
            suffix = this.substring(Const.URL.ICIBA.length)
        }
        this.startsWith(Const.URL.GITHUB_USER_CONTENT)  -> {
            type = "github"
            suffix = this.substring(Const.URL.GITHUB_USER_CONTENT.length)
        }
        else                        -> {
            type = "other"
            suffix = this
        }
    }

    return UrlTypeName(type, suffix)
}

fun String.getUrlBodyTag() :String {
    val queIndex = this.indexOf("?")

    var lastIndex : Int
    if (queIndex <= 0) {
        // 没有带参数的url
        lastIndex = this.length
    } else {
        lastIndex = queIndex
    }

    val strNoParam = this.substring(0, lastIndex)

    val arrs = strNoParam.split("/")

    val minSize = 1 // arrs 如果不包含"/"，则arrs.lenght的最小值为1。
    var content = "other"
    if (arrs.size == minSize) {
        content = arrs.get(minSize - 1)
    } else {
        var charNum = 0
        var first = arrs.get(minSize - 1)
        val length = first.length
        if (length > 3) {
            first = first.substring(0, 3)
            charNum = length - 3
        }

        charNum = charNum + 1 // "1"是"/"的个数

        var endIndex = arrs.size - 1 // 最后一个也单独处理
        for (i in minSize until endIndex) {
            charNum += arrs.get(i).length + 1 // "1"是"/"的个数
        }

        val lastAction = arrs.get(arrs.size - 1)
        content = first + "(" + charNum.toString() + ")" + lastAction
    }

    return content
}

/**
 * Created by sodino on 2018/3/4.
 */
abstract class BeanCallback<T> : Callback<T> {
    /**允许显示错误提示 */
    protected var showToast : Boolean = false
    protected var logMark   : String? = null

    constructor() {
        // 默认显示错误提示
        showToast = true
    }

    constructor(showToast : Boolean) {
        this.showToast = showToast
    }

    override fun onFailure(call: Call<T>, t: Throwable?) {
        if (showToast) {
            t?.let {
                R.string.error_network.showToast()
                LogCat.e("url[${getLogMark(call)}] ${t?.message}")
            }
        }

        onFailure(call, t, null, 1)
    }


    override fun onResponse(call: Call<T>, response: Response<T>) {
        val respCode = response.code()
        LogCat.d("url[" + getLogMark(call) + "]" + respCode)
        if (respCode == HttpURLConnection.HTTP_OK) {
            val t = response.body()
            var isFilled = false
            if (t is Bean<*>) {
                isFilled = t.isFilled()
            } else if (t is String) {
                isFilled = t.length > 0
            }
            onResponse(t!!, isFilled)
        } else {
            var responseBody: ResponseBody? = null
            if (response != null) {
                responseBody = response.errorBody()
            }
            noHttpOK(respCode, responseBody)

            onFailure(call, null, response, respCode)
        }
    }

    protected fun noHttpOK(respCode: Int, respBody: ResponseBody?) {
        var responseBodyString = ""

        if (respBody != null) {
            val source = respBody.source()
            try {
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val buffer = source.buffer()
            try {
                responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"))

                LogCat.d(responseBodyString)
            } catch (t: Throwable) {
                LogCat.e("noHttpOK", t)
            }

        }

        val error = ErrorBean()
        error.parse(responseBodyString)

        val defaultError = App.myApp.getString(R.string.error_network)
        if (error.code !== -1) {
            var strMsg = error.message
            if (TextUtils.isEmpty(strMsg)) {
                strMsg = defaultError
            }

            strMsg = strMsg + "(" + (if (TextUtils.isEmpty(logMark)) "" else logMark + ":") + respCode + ")"

            if (showToast) {
                strMsg.showToast()
            }
        } else {
            // 服务器错误，只能toast respCode
            if (showToast) {
                (defaultError + "(" + respCode + ")").showToast()
            }
        }
    }
    private fun getLogMark(call : Call<*>) : String {
        var tmp = logMark
        if (tmp != null) {
            return tmp
        }

        val url = call.request().url().toString()
        val (type, urlSuffix) = url.getUrlTypeName()
        val urlBodyTag = urlSuffix.getUrlBodyTag()


        val tmpMark = type + "/" + urlBodyTag
        logMark = tmpMark
        return tmpMark
    }


    /**
     * @param isFilled true:真正取到数据，各种list或bean不会为空。false:具体的list.size==0或bean为null.
     */
    abstract fun onResponse(bean: T, isFilled: Boolean)

    /**
     * @param  response 当服务器返回的respCode不符合预期时,此处response为[Callback.onResponse]中的response
     * @param  respCode HttpURLConnection.responseCode.如果网络连续不成功/异常等, 则值是-1.
     */
    abstract fun onFailure(call: Call<T>, t: Throwable?, response: Response<*>?, respCode: Int)

}
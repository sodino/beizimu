package bei.zi.mu.http.retrofit

import android.os.Build
import bei.zi.mu.App
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by sodino on 2018/3/4.
 */
open class AInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
                .header("Charset", "UTF-8")
                .header("Content-Type", "application/json")
                .header("os_version", Build.VERSION.SDK_INT.toString())
                .header("os", "android")
                .header("app_version", "android")
                .header("pkg", App.myApp.packageName)
                .header("versionCode", App.versionCode.toString())
                .header("versionName", App.versionName)

        val newRequest = builder.build()
        val response = chain.proceed(newRequest)
        return response
    }
}
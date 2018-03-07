package bei.zi.mu

import android.app.Application
import bei.zi.mu.http.bean.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser

/**
 * Created by sodino on 2018/3/4.
 */
open class App : Application() {
    lateinit var boxStore : BoxStore

    companion object {
        lateinit var myApp : App
        val packageInfo by lazy {
            myApp.packageManager.getPackageInfo(myApp.packageName, 0)
        }
        val versionName by lazy {
            packageInfo.versionName
        }
        val versionCode by lazy {
            packageInfo.versionCode
        }
    }

    override fun onCreate() {
        super.onCreate()
        myApp = this@App

        initObjectBox()
    }

    fun initObjectBox() {
        // 第一次写代码时
        // 要先声明data class 中 @Entity
        // 然后 Build -> Make Project，
        // 然后才在Application 中 import MyObjectBox，执行ObjectBox的初始化
        boxStore = MyObjectBox.builder().androidContext(this).build()
        AndroidObjectBrowser(boxStore).start(this)
    }

}
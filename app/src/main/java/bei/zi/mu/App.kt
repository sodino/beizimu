package bei.zi.mu

import android.app.Application
import android.content.pm.PackageInfo

/**
 * Created by sodino on 2018/3/4.
 */
open class App : Application() {

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
    }
}
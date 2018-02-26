package bei.zi.mu

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.FragmentActivity
import android.view.View

/**
 * Created by sodino on 2018/2/26.
 */
@SuppressLint("Registered")
open class BaseActivity : FragmentActivity() {
    lateinit protected var rootView : View
    lateinit protected var viewStatusbarBackground :View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogCat.d("${this@BaseActivity.javaClass.simpleName}@${Integer.toHexString(hashCode())}")
    }

    override fun onStart() {
        super.onStart()
        LogCat.d("${this@BaseActivity.javaClass.simpleName}@${Integer.toHexString(hashCode())}")
    }

    override fun onRestart() {
        super.onRestart()

        LogCat.d("${this@BaseActivity.javaClass.simpleName}@${Integer.toHexString(hashCode())}")
    }

    override fun onResume() {
        super.onResume()
        LogCat.d("${this@BaseActivity.javaClass.simpleName}@${Integer.toHexString(hashCode())}")
    }

    override fun onPause() {
        super.onPause()
        LogCat.d("${this@BaseActivity.javaClass.simpleName}@${Integer.toHexString(hashCode())}")
    }

    override fun onStop() {
        super.onStop()
        LogCat.d("${this@BaseActivity.javaClass.simpleName}@${Integer.toHexString(hashCode())}")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogCat.d("${this@BaseActivity.javaClass.simpleName}@${Integer.toHexString(hashCode())}")
    }

}
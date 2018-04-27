package bei.zi.mu.mvp

import java.lang.ref.WeakReference

/**
 * Created by sodino on 2018/4/26.
 */

public abstract class BasePresenter<View>(v : BaseView) {


    private val weakReference : WeakReference<BaseView> by lazy { WeakReference(v) }

    protected val view : View? by lazy {
        val v = weakReference.get()
        if (v == null) {
            null
        } else {
            v as View
        }
    }

    public abstract fun onDestroy()
}

package bei.zi.mu.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bei.zi.mu.LogCat
import bei.zi.mu.ext.hexString


/**
 * Created by sodino on 2018/2/28.
 */
open class BaseFragment : Fragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LogCat.d("${javaClass.simpleName}@${hashCode().hexString()}")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        LogCat.d("${javaClass.simpleName}@${hashCode().hexString()}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogCat.d("${javaClass.simpleName}@${hashCode().hexString()}")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return null
    }

//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState)  {
//        LOG.debug("%s@%s", getClass().getSimpleName(), Long.toHexString(hashCode()));
//        return null;
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isFixTransparentStatusBar()) {
            fixTransparentStatusBar(view)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * 是否需要改变status bar背景色，对于某些机型手机（如oppo）无法改变状态栏字体颜色，
     * 会被当前状态栏挡住字体颜色，因此修改透明状态栏背景色
     *
     * @return true: 调用fixTransparentStatusBar()
     */
    protected fun isFixTransparentStatusBar(): Boolean {
        return false
    }

    /**
     * @param view [.onCreateView]中返回的view．
     */
    protected fun fixTransparentStatusBar(view: View) {

    }

    override fun onDetach() {
        super.onDetach()
        LogCat.d("${javaClass.simpleName}@${hashCode().hexString()}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogCat.d("${javaClass.simpleName}@${hashCode().hexString()}")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogCat.d("${javaClass.simpleName}@${hashCode().hexString()}")
    }

}
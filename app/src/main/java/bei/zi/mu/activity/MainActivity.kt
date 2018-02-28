package bei.zi.mu.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.widget.LinearLayout
import bei.zi.mu.R
import bei.zi.mu.TitlebarActivity
import bei.zi.mu.adapter.MainAdapter
import bei.zi.mu.thread.ThreadPool
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : TitlebarActivity(), ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {

    val mainAdapter : MainAdapter   by lazy { MainAdapter(supportFragmentManager) }
    val tabLayoutListener           by lazy { TabLayout.TabLayoutOnPageChangeListener(tabLayout) }
    var oldSelectedTab: Int?        = null
    companion object {
        val SHOW_MAIN               = 0             // 显示tab的主界面
        val SHOW_SPLASH             = SHOW_MAIN + 1 // 显示splash页

        var isFirstShow             = true          // 第一次启动需要显示splash或welcome
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        setTitle(R.string.app_name)
        val type = getShowType()

        when(type) {
            SHOW_SPLASH ->  {showSplash()}
            else        ->  {showMain()}
        }
    }

    private fun showMain() {
        window.setBackgroundDrawableResource(R.drawable.activity_white_bg)
        setContentViewNoTitlebar(R.layout.activity_main)

        MainAdapter.fillTab(layoutInflater, tabLayout)

        vPager.adapter = mainAdapter
        // fix fragment因为被回收会再重新显示时会没有数据
        vPager.offscreenPageLimit = MainAdapter.IDX_ALL

        tabLayout.addOnTabSelectedListener(this)
        vPager.addOnPageChangeListener(this)

    }

    private fun showSplash() {
        // SAM constructor : single abstract method
        ThreadPool.UIHandler.postDelayed({ showMain() }, 2000);
    }

    private fun getShowType(): Int {
        return if (isFirstShow) SHOW_SPLASH else SHOW_MAIN
    }


    override fun onTabReselected(tab: TabLayout.Tab) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        vPager.setCurrentItem(tab.position)
    }

    override fun onPageScrollStateChanged(state: Int) {
        tabLayoutListener.onPageScrollStateChanged(state)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        tabLayoutListener.onPageScrolled(position, positionOffset, positionOffsetPixels)
    }

    override fun onPageSelected(position: Int) {
        tabLayoutListener.onPageSelected(position)
        var tmpOld = oldSelectedTab
        if (tmpOld == null) {
            tmpOld = tabLayout.selectedTabPosition
            oldSelectedTab = tmpOld
        }

        if (tmpOld == position) {
            // 两者相等，不再处理ui显示
            return
        }

        val oldTabView = (tabLayout.getChildAt(0) as LinearLayout).getChildAt(tmpOld) as LinearLayout
        oldTabView.isSelected = false

        val newTabView = (tabLayout.getChildAt(0) as LinearLayout).getChildAt(position) as LinearLayout
        newTabView.isSelected = true

        oldSelectedTab = position
    }

}

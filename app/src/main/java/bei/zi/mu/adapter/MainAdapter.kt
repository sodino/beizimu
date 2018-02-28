package bei.zi.mu.adapter

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import bei.zi.mu.R
import bei.zi.mu.fragment.GroupFragment
import bei.zi.mu.fragment.SettingFragment
import bei.zi.mu.fragment.WordCardFragment

/**
 * Created by sodino on 2018/2/28.
 */
class MainAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    val fragments : SparseArray<Fragment> by lazy { SparseArray<Fragment>() }

    companion object {
        val IDX_WORD_CARD           = 0
        val IDX_GROUP               = IDX_WORD_CARD + 1
        val IDX_SETTING             = IDX_GROUP + 1
        val IDX_ALL                 = IDX_SETTING + 1
        fun fillTab(layoutInflater: LayoutInflater, tabLayout : TabLayout) {
            for (i in IDX_WORD_CARD..IDX_SETTING) {
                val tab = tabLayout.newTab()
                val tabView = layoutInflater.inflate(R.layout.tab_view, null)
                tab.customView = tabView

                var stringId : Int = R.string.word_card
                when(i) {
                    IDX_WORD_CARD   -> {stringId = R.string.word_card}
                    IDX_GROUP       -> {stringId = R.string.group}
                    IDX_SETTING     -> {stringId = R.string.setting}
                }
                var tabText = tabView.findViewById<TextView>(R.id.tabText)
                tabText.setText(stringId)

                tabLayout.addTab(tab)
                // 为4个tab添加圆形的水波点击效果，取代之前的方框效果
                val innerTabView = (tabLayout.getChildAt(0) as LinearLayout).getChildAt(i) as LinearLayout
                innerTabView.setBackgroundResource(R.drawable.ripple_overflow)
            }
        }
    }

    init {
        fragments.put(IDX_WORD_CARD, WordCardFragment())
        fragments.put(IDX_GROUP, GroupFragment())
        fragments.put(IDX_SETTING, SettingFragment())
    }

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getCount(): Int {
        return fragments.size()
    }

}


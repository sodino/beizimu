package bei.zi.mu.dialog

import android.app.Activity
import android.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import bei.zi.mu.R
import bei.zi.mu.activity.adapter.GroupDialogAdapter
import bei.zi.mu.http.bean.GroupNameBean
import bei.zi.mu.http.bean.GroupWordBean
import bei.zi.mu.http.bean.WordBean


public class WordGroupDialog(bean : WordBean, activity: Activity, listGroup: List<GroupNameBean>) : AlertDialog(activity), View.OnClickListener {
    private val bean = bean
    init {
        setTitle(R.string.add_to)

        val recyclerView = RecyclerView(activity)

        val adapter = GroupDialogAdapter(this)
        adapter.initGroupBean(listGroup)

        val layoutMgr = LinearLayoutManager(activity)
        layoutMgr.orientation = LinearLayoutManager.VERTICAL

        recyclerView.layoutManager = layoutMgr
        recyclerView.adapter = adapter

        setView(recyclerView)
    }

    override fun onClick(v: View) {
        val group = v.tag as GroupNameBean

        val groupWordBean = GroupWordBean(group = group.name, word = bean.name)
        groupWordBean.insertOrUpdate()

        dismiss()
    }
}

package bei.zi.mu.activity.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bei.zi.mu.Const
import bei.zi.mu.R
import bei.zi.mu.activity.WordListActivity
import bei.zi.mu.activity.adapter.GroupAdapter
import bei.zi.mu.http.bean.GroupNameBean
import kotlinx.android.synthetic.main.group_fragment2.*
import android.content.DialogInterface
import android.widget.EditText
import bei.zi.mu.ext.resString
import bei.zi.mu.ext.showToast


class GroupFragment : BaseFragment(), View.OnClickListener {
    private val adapter = GroupAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.group_fragment2, container, false)
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listBean = GroupNameBean.all()
        adapter.initGroupBean(listBean)

        val layoutMgr = LinearLayoutManager(view.context)
        layoutMgr.orientation = LinearLayoutManager.VERTICAL

        recyclerView.layoutManager = layoutMgr
        recyclerView.adapter = adapter
    }

    override fun onClick(v: View) {
        val tag = v.getTag()
        when(tag) {
            is Int          -> {
                when(tag) {
                    R.string.recent100    -> {WordListActivity.launch(context, Const.WordTag.RECENT_100)}
                    R.string.CET4         -> {WordListActivity.launch(context, Const.WordTag.CET_4)}
                    R.string.CET6         -> {WordListActivity.launch(context, Const.WordTag.CET_6)}
                    R.string.TOEFL        -> {WordListActivity.launch(context, Const.WordTag.TOEFL)}
                    R.string.GRE          -> {WordListActivity.launch(context, Const.WordTag.GRE)}
                    R.string.kaoYan       -> {WordListActivity.launch(context, Const.WordTag.KAO_YAN)}
                    R.string.new_group    -> {showAddGroupDialog()}
                }
            }
            is GroupNameBean    -> {
                val name = tag.name
                WordListActivity.launch(context, group = name)
            }
        }
    }

    private fun showAddGroupDialog() {
        val alert = AlertDialog.Builder(activity)

        val edittext = EditText(activity)
//        alert.setMessage("Enter Your Message")
        alert.setTitle(R.string.new_group.resString())

        alert.setView(edittext)

        alert.setPositiveButton(R.string.new_create.resString(), DialogInterface.OnClickListener { dialog, whichButton ->
            val newName = edittext.text.toString()
            tryNewGroup(newName)
        })

        alert.setNegativeButton(R.string.cancel.resString(), null)

        alert.show()
    }

    private fun tryNewGroup(newName: String) {
        val oldBean = GroupNameBean.findFirstByPrimaryKey(newName)
        if (oldBean != null) {
            "Group '$newName' had existed.".showToast()
            return
        }
        val bean = GroupNameBean(name = newName)
        bean.insertOrUpdate()

        val listBean = GroupNameBean.all()
        adapter.initGroupBean(listBean)
        adapter.notifyDataSetChanged()
    }
}
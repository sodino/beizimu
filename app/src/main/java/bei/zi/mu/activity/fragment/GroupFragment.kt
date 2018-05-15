package bei.zi.mu.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bei.zi.mu.Const
import bei.zi.mu.R
import bei.zi.mu.activity.WordListActivity
import kotlinx.android.synthetic.main.group_fragment.*

class GroupFragment : BaseFragment(), View.OnClickListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.group_fragment, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        txtRecent100.setOnClickListener(this)
        txtCET4.setOnClickListener(this)
        txtCET6.setOnClickListener(this)
        txtToefl.setOnClickListener(this)
        txtGRE.setOnClickListener(this)
        txtKaoYan.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.txtRecent100   -> {WordListActivity.launch(context, Const.WordTag.RECENT_100)}
            R.id.txtCET4        -> {WordListActivity.launch(context, Const.WordTag.CET_4)}
            R.id.txtCET6        -> {WordListActivity.launch(context, Const.WordTag.CET_6)}
            R.id.txtToefl       -> {WordListActivity.launch(context, Const.WordTag.TOEFL)}
            R.id.txtGRE         -> {WordListActivity.launch(context, Const.WordTag.GRE)}
            R.id.txtKaoYan      -> {WordListActivity.launch(context, Const.WordTag.KAO_YAN)}
        }
    }
}
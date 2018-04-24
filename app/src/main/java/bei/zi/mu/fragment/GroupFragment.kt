package bei.zi.mu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        WordListActivity.launch(context, v.id)
//        when(v.id) {
//            R.id.txtRecent100   -> {}
//            R.id.txtCET4        -> {}
//            R.id.txtCET6        -> {}
//            R.id.txtToefl       -> {}
//            R.id.txtGRE         -> {}
//            R.id.txtKaoYan      -> {}
//        }
    }
}
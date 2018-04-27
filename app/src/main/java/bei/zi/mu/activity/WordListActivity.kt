package bei.zi.mu.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import bei.zi.mu.Const
import bei.zi.mu.R
import bei.zi.mu.TitlebarActivity
import bei.zi.mu.adapter.WordListAdapter
import bei.zi.mu.http.bean.WordBean
import bei.zi.mu.mvp.WordListActivity.Presenter
import kotlinx.android.synthetic.main.word_list_activity.*

/**
 * Created by sodino on 2018/4/24.
 */
public class WordListActivity : TitlebarActivity<Presenter>(), View.OnClickListener, bei.zi.mu.mvp.WordListActivity.View {
    var type            : Int              = R.id.txtRecent100
//    val presenter       : Presenter        by lazy { Presenter(this@WordListActivity) }
    val adapter         : WordListAdapter  = WordListAdapter()
    companion object {
        public fun launch(context : Context, id : Int) {
            val intent = Intent(context, WordListActivity::class.java)
            intent.putExtra(Const.Param.I_TYPE, id)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.word_list_activity)

        type = intent.getIntExtra(Const.Param.I_TYPE, R.id.txtRecent100)
        title = Const.WordTag.map.get(type) ?: getString(R.string.recent100)

        val layoutMgr = LinearLayoutManager(this@WordListActivity)
        layoutMgr.orientation = LinearLayoutManager.VERTICAL

        recyclerView.layoutManager = layoutMgr
        recyclerView.adapter = adapter


        dlgLoading.setMessage(getString(R.string.loading))
        dlgLoading.show()
        presenter.reqWordList(type)
    }

    override fun onClick(v: View) {

    }

    override fun respWordList(list: List<WordBean>) {
        dlgLoading.dismiss()
        adapter.updateWordList(list, false)
        adapter.notifyDataSetChanged()
    }

    override fun respEmpty() {
        dlgLoading.dismiss()

    }

    override fun doBackPressed(): Boolean {
        dlgLoading.dismiss()
        return super.doBackPressed()
    }
}
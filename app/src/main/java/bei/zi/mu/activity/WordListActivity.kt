package bei.zi.mu.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import bei.zi.mu.Const
import bei.zi.mu.R
import bei.zi.mu.TitlebarActivity
import bei.zi.mu.activity.adapter.WordListAdapter
import bei.zi.mu.ext.showToast
import bei.zi.mu.http.bean.WordBean
import bei.zi.mu.mvp.WordListActivity.Presenter
import bei.zi.mu.player.WordsPlayer
import bei.zi.mu.rxbus.RxBus
import bei.zi.mu.rxbus.event.PlayingWordEvent
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.word_list_activity.*

/**
 * Created by sodino on 2018/4/24.
 */
public class WordListActivity : TitlebarActivity<Presenter>(),
        View.OnClickListener,
        bei.zi.mu.mvp.WordListActivity.View,
        Consumer<PlayingWordEvent> {

    var type                        : Int              = R.id.txtRecent100
//    val presenter                 : Presenter        by lazy { Presenter(this@WordListActivity) }
    val adapter                     : WordListAdapter  = WordListAdapter()
    lateinit var disposable         : Disposable

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

        disposable = RxBus.toObservable(PlayingWordEvent::class.java)
                .subscribe(this@WordListActivity)
    }

    // RxBus.**.subscribe(onNext : Consumer)
    override fun accept(event: PlayingWordEvent) {
            "${event.word.name}\nidx=${event.index}\ncnt=${event.repeatCount}".showToast()
    }


    override fun createTitlebarRightView(relLayout: RelativeLayout): View {
        val txt = TextView(this)
        txt.text = getString(R.string.play)
        txt.id = R.id.titlebar_right
        txt.setOnClickListener(this@WordListActivity)
        return txt
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.titlebar_right -> {
                var list = v.tag as List<WordBean>
                WordsPlayer.player.setWords(list)
                WordsPlayer.player.play()
            }
        }
    }

    override fun respWordList(list: List<WordBean>) {
        dlgLoading.dismiss()
        adapter.updateWordList(list, false)
        adapter.notifyDataSetChanged()

        titleRightView?.visibility = if (list.isEmpty()) View.INVISIBLE else View.VISIBLE
        titleRightView?.tag = list
    }

    override fun respEmpty() {
        dlgLoading.dismiss()
    }

    override fun doBackPressed(): Boolean {
        dlgLoading.dismiss()
        return super.doBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
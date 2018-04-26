package bei.zi.mu.mvp.SearchActivity

import bei.zi.mu.LogCat
import bei.zi.mu.http.bean.WordBean
import bei.zi.mu.http.retrofit.ARetrofit
import bei.zi.mu.mvp.BasePresenter
import bei.zi.mu.util.showToast
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by sodino on 2018/4/26.
 */
public class Presenter(v : View) : BasePresenter<View>(v) {
    private var disposable : Disposable? = null

    public fun reqWord(word : String) {
        disposable = Single.just(word)
            .map{
                val findResult = WordBean.findFirstByPrimaryKey(word)
                if (findResult != null) {
                    findResult
                } else {
                    val resp = ARetrofit.wordApi.reqGithubWord(word[0].toString(), word).execute()
                    val bean = resp.body()
                    if (bean?.isFilled() == true) {
                        bean?.initMemoryBean()
                        bean?.insertOrUpdate()
                    }
                    bean
                }
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // onSuccess
                if (it != null) {
                    view?.respWord(it)
                } else {
                    view?.respError()
                }
            }, {
                // onError
                LogCat.e("searchWord [$word] errors", it)
                "$word failure[${it.javaClass}:${it.message}]".showToast()
                view?.respError()
            })
    }

    override fun onDestroy() {
        disposable?.dispose()
    }
}
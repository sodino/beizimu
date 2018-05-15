package bei.zi.mu.mvp.SearchActivity

import bei.zi.mu.LogCat
import bei.zi.mu.ext.reqWord
import bei.zi.mu.ext.showToast
import bei.zi.mu.http.bean.WordBean
import bei.zi.mu.http.retrofit.ARetrofit
import bei.zi.mu.mvp.BasePresenter
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
                word.reqWord()
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
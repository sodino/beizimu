package bei.zi.mu.mvp.SearchActivity

import bei.zi.mu.LogCat
import bei.zi.mu.ext.reqGithubWord
import bei.zi.mu.ext.reqLocalWord
import bei.zi.mu.ext.showToast
import bei.zi.mu.mvp.BasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by sodino on 2018/4/26.
 */
public class Presenter(v : View) : BasePresenter<View>(v) {
    private var disposable : Disposable? = null

    public fun reqWord2(word : String) {
        val reqLocal = word.reqLocalWord()
        val reqNet = word.reqGithubWord()
        Observable.concat(reqLocal, reqNet)
                .firstOrError()
                .map { bean ->
                    if (bean.id == 0L) { // 从后台拉取的数据需要保存到db
                        if (bean.isFilled()) {
                            bean.initMemoryBean()
                            bean.insertOrUpdate()
                        }
                    }

                    bean
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.respWord(it)
                },{
                    // onError
                    LogCat.e("searchWord [$word] errors", it)
                    "$word failure[${it.javaClass}:${it.message}]".showToast()
                    view?.respError()
                })

    }

//    public fun reqWord(word : String) {
//        disposable = Single.just(word)
//            .map{
//                word.reqWord()
//            }.subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                // onSuccess
//                if (it != null) {
//                    view?.respWord(it)
//                } else {
//                    view?.respError()
//                }
//            }, {
//                // onError
//                LogCat.e("searchWord [$word] errors", it)
//                "$word failure[${it.javaClass}:${it.message}]".showToast()
//                view?.respError()
//            })
//    }

    override fun onDestroy() {
        disposable?.dispose()
    }
}
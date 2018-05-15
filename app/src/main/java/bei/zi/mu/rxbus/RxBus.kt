package bei.zi.mu.rxbus

import io.reactivex.Observable
import io.reactivex.internal.functions.Functions
import io.reactivex.subjects.BehaviorSubject

object RxBus {
    private val bus by lazy {
        // toSerialized(): for multiple threads safe
        // https://github.com/ReactiveX/RxJava/wiki/Subject
//        PublishSubject.create<Any>().toSerialized()   // PublishSubject  : 会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
        BehaviorSubject.create<Any>().toSerialized()    // BehaviorSubject : 当观察者订阅BehaviorSubject时，它开始发射原始Observable最近发射的数据（如果此时还没有收到任何数据，它会发射一个默认值），
    }

    fun post(o: Any) {
        bus.onNext(o)
    }

    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        var observable = bus.filter(Functions.isInstanceOf(eventType))
        return observable.cast(eventType)
        // ofType = filter + cast
//        return bus.ofType(eventType)
    }
}

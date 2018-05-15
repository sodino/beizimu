package bei.zi.mu.player

import bei.zi.mu.http.bean.WordBean
import bei.zi.mu.rxbus.RxBus
import bei.zi.mu.rxbus.event.PlayingWordEvent
import bei.zi.mu.util.playMp3
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

const val PERIOD = 5 * 1000L // 5s播放一个单词
const val REPEAT = 5         // 每个单词重复播放5次
class WordsPlayer {
    private val         listWords               = mutableListOf<WordBean>()
    private var         index                   = 0
    private var         dispose : Disposable?   = null

    companion object {
        val player by lazy { WordsPlayer() }
    }

    fun setWords(list : List<WordBean>) {
        clear()
        this.listWords.addAll(list)
    }

    fun play() {
        dispose = Schedulers.io().schedulePeriodicallyDirect( {
            val size = listWords.size

            val idx = (index / REPEAT) % size
            val bean = listWords[idx]
            val playingEvent = PlayingWordEvent(idx, index % REPEAT, bean)
            RxBus.post(playingEvent)

            bean.phoneticSymbol?.get(0)?.playMp3()

            if (dispose != null) {
                index ++
            }
        },
        0,
        PERIOD, TimeUnit.MILLISECONDS)
    }

    fun stop() {
        dispose?.dispose()
        dispose = null
    }

    fun clear() {
        stop()
        index = 0
        listWords.clear()
    }
}
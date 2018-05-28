package bei.zi.mu.batch

import bei.zi.mu.ext.d
import bei.zi.mu.ext.reqWord
import bei.zi.mu.rxbus.RxBus
import bei.zi.mu.rxbus.event.ImportedWordEvent
import bei.zi.mu.rxbus.event.ImportingWordEvent
import io.reactivex.schedulers.Schedulers
import java.io.File

const val FILE_NAME = "/sdcard/beizimu/all.english.word.69287.log"

object BatchImporter {
    private val file by lazy { File(FILE_NAME) }

    var hasWordsFile : Boolean = false
        get() {
            return file.isFile
        }

    fun start() {
        Schedulers.io().scheduleDirect{
            file.useLines {
                it.forEachIndexed({idx, word ->
                    "importing $idx $word".d()

                    RxBus.post(ImportingWordEvent(idx, word))
                    val bean = word.reqWord(isBatchImport = true)
                    if (bean != null) {
                        RxBus.post(ImportedWordEvent(idx, bean))
                    }
                })
            }
        }
    }
}

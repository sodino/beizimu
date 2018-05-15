package bei.zi.mu.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bei.zi.mu.R
import bei.zi.mu.batch.BatchImporter
import bei.zi.mu.rxbus.RxBus
import bei.zi.mu.rxbus.event.ImportedWordEvent
import bei.zi.mu.rxbus.event.ImportingWordEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.setting_fragment.*

class SettingFragment : BaseFragment(), View.OnClickListener {
    var disposableImporting : Disposable? = null
    var disposableImported  : Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.setting_fragment, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtImportBatch.isEnabled = BatchImporter.hasWordsFile
        txtImportBatch.setOnClickListener(this@SettingFragment)
        txtImportInfo.text = if (txtImportBatch.isEnabled) {
            getString(R.string.click2import)
        } else {
            getString(R.string.cannotFindWordsFile)
        }
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.txtImportBatch -> {
                txtImportBatch.isEnabled = false
                txtImportBatch.text = getString(R.string.importing)

                disposableImporting = RxBus.toObservable(ImportingWordEvent::class.java)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{importingEvent ->
                            txtImportInfo.text = "importing ${importingEvent.index} ${importingEvent.word}"
                        }
                disposableImported = RxBus.toObservable(ImportedWordEvent::class.java)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{importedEvent ->
                            txtImportInfo.text = "imported ${importedEvent.index} ${importedEvent.word}"
                        }
                BatchImporter.start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposableImported?.dispose()
        disposableImporting?.dispose()
    }
}
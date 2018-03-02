package bei.zi.mu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bei.zi.mu.R
import kotlinx.android.synthetic.main.word_card_titlebar.*

class WordCardFragment : BaseFragment(), View.OnClickListener {
    override fun onClick(v: View) {
        when(v.id) {
            R.id.imgCamera -> {}
            R.id.imgSearch -> {}
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.word_card_fragment, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
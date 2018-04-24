package bei.zi.mu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bei.zi.mu.R
import bei.zi.mu.http.bean.PhoneticSymbol
import bei.zi.mu.http.bean.WordBean
import bei.zi.mu.util.playMp3
import bei.zi.mu.util.showToast
import kotlinx.android.synthetic.main.word_card_fragment.*

class WordCardFragment : BaseFragment(), View.OnClickListener {
    override fun onClick(v: View) {
        when(v.id) {
//            R.id.imgCamera -> {}
//            R.id.imgSearch -> {}
            R.id.txtPhoneticHint -> {
                txtPhoneticHint.visibility = View.GONE
                layoutPhonetic.visibility = View.VISIBLE
            }
            R.id.txtMeansHint -> {
                txtMeansHint.visibility = View.GONE
                txtMeans.visibility = View.VISIBLE
            }
            R.id.txtNext         -> {
                if (v.tag is WordBean) {
                    val bean = v.tag as WordBean
                    bean.updateMemory()
                }
                showNextWordCard()
            }
            R.id.txtPhoneticAm   -> { (v.tag as WordBean)?.phoneticSymbol?.get(0)?.playMp3(PhoneticSymbol.AM) }
            R.id.txtPhoneticEn   -> { (v.tag as WordBean)?.phoneticSymbol?.get(0)?.playMp3(PhoneticSymbol.EN) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.word_card_fragment, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showNextWordCard()
        txtNext.setOnClickListener(this)
    }

    fun showNextWordCard() {
        val bean = WordBean.find4WordCard()
        if (bean == null) {
            "no more word".showToast()
            return;
        }

        txtNext.tag = bean

        txtWord.text = bean.name
        barRating.rating = bean.frequence.toFloat()

        if (bean.phoneticSymbol?.isNotEmpty() == true) {
            val phonetic = bean.phoneticSymbol?.get(0)

            txtPhoneticHint.visibility = View.VISIBLE
            layoutPhonetic.visibility = View.INVISIBLE

            txtPhoneticHint.setOnClickListener(this)

            txtPhoneticEn.text = "en\n[${phonetic?.en}]"
            txtPhoneticAm.text = "am\n[${phonetic?.am}]"
            txtPhoneticEn.tag = bean
            txtPhoneticAm.tag = bean

            txtPhoneticEn.setOnClickListener(this)
            txtPhoneticAm.setOnClickListener(this)
        } else {
            txtPhoneticHint.visibility = View.GONE
            layoutPhonetic.visibility = View.GONE
        }

        if (bean.means?.isNotEmpty() == true) {
            txtMeansHint.visibility = View.VISIBLE
            txtMeans.visibility = View.INVISIBLE

            txtMeansHint.setOnClickListener(this)

            var strMeans = ""
            bean.means?.forEach {
                if (strMeans.isNotEmpty()) {
                    strMeans += "\n"
                }
                strMeans += it.part + " " + it.mean
            }

            txtMeans.text = strMeans
        } else {
            txtMeansHint.visibility = View.GONE
            txtMeans.visibility = View.GONE
        }
    }
}
package bei.zi.mu.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bei.zi.mu.R
import bei.zi.mu.http.bean.WordBean

public class WordListAdapter : RecyclerView.Adapter<WordListAdapter.Holder>() {
    val list : MutableList<WordBean> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.word_list_item, parent, false)

        val holder = Holder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val bean = list[position]
        holder.txtWord.text = bean.name
    }

    public fun updateWordList(newlist : List<WordBean>, isReset : Boolean) {
        if (isReset) {
            list.clear()
        }

        list.addAll(newlist)
    }

    class Holder(v : View) : RecyclerView.ViewHolder(v){
        val txtWord : TextView = v.findViewById(R.id.txtWord)
    }

}

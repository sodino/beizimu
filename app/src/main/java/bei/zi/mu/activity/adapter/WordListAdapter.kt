package bei.zi.mu.activity.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bei.zi.mu.R
import bei.zi.mu.http.bean.WordBean

public class WordListAdapter(listener : View.OnClickListener) : RecyclerView.Adapter<WordListAdapter.Holder>() {
    val list : MutableList<WordBean>    = mutableListOf()
    val clickListener                   = listener
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
        val phonetic = bean.phoneticSymbol?.get(0)?.en ?: ""
        holder.txtWord.append( if (phonetic.isEmpty()) { "" } else { "      [$phonetic]"} )


        holder.txtWord.setOnClickListener(clickListener)
        holder.itemView.setOnClickListener(clickListener)
        holder.txtWord.setTag(bean)
        holder.itemView.setTag(bean)
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

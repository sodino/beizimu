package bei.zi.mu.activity.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bei.zi.mu.R
import bei.zi.mu.http.bean.GroupNameBean
/**
 * for GroupFragment
 * */
public class GroupDialogAdapter(listener : View.OnClickListener) : RecyclerView.Adapter<GroupDialogAdapter.GroupHolder>(){
    private val clickListener = listener

    private val arrGroup = mutableListOf<GroupNameBean>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        val context = parent.context
        val resource = context.resources

        val view = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false)
        val holder = GroupHolder(view)

        return holder
    }

    override fun getItemCount(): Int {
        return arrGroup.size
    }

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        holder.itemView.setOnClickListener(clickListener)
        val txtName = holder.txtName
        val bean = arrGroup.get(position)
        txtName.text = bean.name
        holder.itemView.setTag(bean)
    }

    fun initGroupBean(listBean: List<GroupNameBean>) {
        arrGroup.clear()
        arrGroup.addAll(listBean)
    }


    class GroupHolder(v : View) : RecyclerView.ViewHolder(v){
        val txtName = v.findViewById(R.id.txtName) as TextView
    }

}


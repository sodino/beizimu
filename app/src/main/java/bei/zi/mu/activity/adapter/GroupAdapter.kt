package bei.zi.mu.activity.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bei.zi.mu.R
import bei.zi.mu.ext.resString
import bei.zi.mu.http.bean.GroupNameBean
/**
 * for GroupFragment
 * */
public class GroupAdapter(listener : View.OnClickListener) : RecyclerView.Adapter<GroupAdapter.GroupHolder>(){
    val TYPE_PREFIX                 = 0
    val TYPE_GROUP                  = TYPE_PREFIX + 1
    val TYPE_ADD_BUTTON             = TYPE_GROUP + 1

    private val clickListener = listener


    private val prefix = arrayOf(R.string.recent100,
            R.string.CET4,
            R.string.CET6,
            R.string.TOEFL,
            R.string.GRE,
            R.string.kaoYan)

    private val arrGroup = mutableListOf<GroupNameBean>()

    override fun getItemViewType(position: Int): Int {
        if (position < prefix.size) {
            return TYPE_PREFIX
        } else if (position == itemCount -1) {
            return TYPE_ADD_BUTTON
        } else {
            return TYPE_GROUP
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        val context = parent.context
        val resource = context.resources

        val view = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false)
        val holder = GroupHolder(view)

        return holder
    }

    override fun getItemCount(): Int {
        val count = prefix.size + arrGroup.size + 1                 // 最后一项，创建新的组
        return count
    }

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        holder.itemView.setOnClickListener(clickListener)
        val txtName = holder.txtName
        if (position < prefix.size) {
            val resId = prefix[position]
            txtName.text = resId.resString()
            holder.itemView.setTag(resId)
        } else if (position == itemCount -1) {
            val resId = R.string.new_group
            txtName.text = resId.resString()
            holder.itemView.setTag(resId)
        } else {
            val bean = arrGroup.get(position - prefix.size)
            txtName.text = bean.name
            holder.itemView.setTag(bean)
        }
    }

    fun initGroupBean(listBean: List<GroupNameBean>) {
        arrGroup.clear()
        arrGroup.addAll(listBean)
    }


    class GroupHolder(v : View) : RecyclerView.ViewHolder(v){
        val txtName = v.findViewById(R.id.txtName) as TextView
    }

}


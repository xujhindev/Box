package com.xujhin.box.ui.main

import android.content.Context
import android.widget.TextView
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter
import com.donkingliang.groupedadapter.holder.BaseViewHolder
import com.xujhin.box.R
import com.xujhin.box.entity.GroupEntity

class GroupAdapter(context: Context) : GroupedRecyclerViewAdapter(context, false) {
    var listener: ActionListener? = null
    val dataResult: MutableList<GroupEntity> = arrayListOf()
    override fun getGroupCount(): Int {
        return dataResult.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return dataResult[groupPosition].childs.count()
    }

    override fun hasHeader(groupPosition: Int): Boolean {
        return dataResult.get(groupPosition).childs.isNotEmpty()
    }

    override fun hasFooter(groupPosition: Int): Boolean {
        return false
    }

    override fun getHeaderLayout(viewType: Int): Int {
        return R.layout.group_header
    }

    override fun getFooterLayout(viewType: Int): Int {
        return 0
    }

    override fun getChildLayout(viewType: Int): Int {
        return R.layout.item_cell
    }

    override fun onBindHeaderViewHolder(holder: BaseViewHolder?, groupPosition: Int) {
        holder?.setText(R.id.tv_group_name, dataResult[groupPosition].name)
        holder?.get<TextView>(R.id.tv_delete_all)?.setOnClickListener {
            listener?.deleteAll(dataResult[groupPosition])
        }
    }

    override fun onBindFooterViewHolder(holder: BaseViewHolder?, groupPosition: Int) {
    }

    override fun onBindChildViewHolder(holder: BaseViewHolder?, groupPosition: Int, childPosition: Int) {
        val data = dataResult[groupPosition].childs[childPosition]
        holder?.setText(
            R.id.tv_info,
            "名称:${data.name}     长:${data.length} mm    宽:${data.width} mm     高:${data.height} mm     重量:${data.weight} kg"
        )
        holder?.get<TextView>(R.id.tv_delete)?.setOnClickListener {
            listener?.delete(data, holder.adapterPosition)
        }
        holder?.get<TextView>(R.id.tv_modify)?.setOnClickListener {
            listener?.modify(data, holder.adapterPosition)
        }
        holder?.itemView?.setOnClickListener {
            listener?.clickItem(data,holder.adapterPosition)
        }
    }

    fun updateData(it: MutableList<GroupEntity>?) {
        it?.let {
            dataResult.clear()
            dataResult.addAll(it)
            this.notifyDataSetChanged()
        }
    }
}
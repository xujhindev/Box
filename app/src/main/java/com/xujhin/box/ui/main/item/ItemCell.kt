package com.xujhin.box.ui.main.item

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chad.library.adapter.base.binder.BaseItemBinder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xujhin.box.databinding.ItemCellBinding
import com.xujhin.box.entity.BaseEntity

interface ActionListener {
    fun delete(data: BaseEntity, pos: Int)
    fun modify(data: BaseEntity, pos: Int)
}

class ItemCell : BaseItemBinder<BaseEntity, ItemViewHolder>() {
    var listener: ActionListener? = null
    @SuppressLint("SetTextI18n")
    override fun convert(holder: ItemViewHolder, data: BaseEntity) {
        holder.binding.tvInfo.setText(
            "名称${data.name}     长:${data.length} mm    宽:${data.name} mm     高:${data.name} mm     重量:${data.name} mm"
        )
        holder.binding.tvDelete.setOnClickListener {
            listener?.delete(data, holder.adapterPosition)
        }
        holder.binding.tvModify.setOnClickListener {
            listener?.modify(data, holder.adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}

class ItemViewHolder(val binding: ItemCellBinding) : BaseViewHolder(binding.root) {}
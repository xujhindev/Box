package com.xujhin.box.ui.main

import com.xujhin.box.entity.BaseEntity
import com.xujhin.box.entity.GroupEntity

interface ActionListener {
    fun delete(data: BaseEntity, pos: Int)
    fun modify(data: BaseEntity, pos: Int)
    fun deleteAll(groupEntity: GroupEntity)
    fun clickItem(data: BaseEntity, adapterPosition: Int)
}
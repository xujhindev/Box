package com.xujhin.box.entity

import com.xujhin.box.ui.dialog.InputType
import java.util.StringJoiner

data class GroupEntity(
    val inputType: InputType,
    var name: String,
    var count: Int = 0,
    val childs: MutableList<BaseEntity> = mutableListOf()
)


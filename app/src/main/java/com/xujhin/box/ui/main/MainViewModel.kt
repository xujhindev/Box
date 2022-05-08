package com.xujhin.box.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xujhin.box.entity.BaseEntity
import com.xujhin.box.entity.GroupEntity
import com.xujhin.box.ui.dialog.InputType

class MainViewModel : ViewModel() {


    private val listResult: MutableList<GroupEntity> = arrayListOf()
    val resultLivedata: MutableLiveData<MutableList<GroupEntity>> = MutableLiveData()

    init {
        listResult.add(0, GroupEntity(inputType = InputType.Repository, "箱子数"))
        listResult.add(0, GroupEntity(inputType = InputType.Box, "盒子数"))
    }

    fun update(data: BaseEntity) {
        when (data) {
            is BaseEntity.Repository -> {
                listResult[0].apply {
                    childs.add(data)
                    count += 1
                    name = "箱子数 $count"
                }
            }
            is BaseEntity.Box -> {
                listResult[1].apply {
                    childs.add(data)
                    count += 1
                    name = "盒子数 $count"
                }
            }
        }
        resultLivedata.postValue(listResult)
    }
}
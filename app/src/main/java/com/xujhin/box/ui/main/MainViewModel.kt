package com.xujhin.box.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xujhin.box.entity.BaseEntity
import com.xujhin.box.entity.GroupEntity
import com.xujhin.box.ui.dialog.InputType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel : ViewModel() {


    private val listResult: MutableList<GroupEntity> = arrayListOf()
    val resultLivedata: MutableLiveData<MutableList<GroupEntity>> = MutableLiveData()

    init {
        listResult.add(0, GroupEntity(inputType = InputType.Repository, "箱子数"))
        listResult.add(1, GroupEntity(inputType = InputType.Box, "盒子数"))
        getAllData()
    }

    fun update(data: BaseEntity) {
        when (data) {
            is BaseEntity.Repository -> {
                listResult[0].apply {
                    childs.add(data)
                    count += 1
                    name = "箱子数 $count"
                }
                addRepository(data)
            }
            is BaseEntity.Box -> {
                listResult[1].apply {
                    childs.add(data)
                    count += 1
                    name = "盒子数 $count"
                }
                addBox(data)
            }
        }
        resultLivedata.postValue(listResult)
    }

    private fun addBox(data: BaseEntity.Box) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.boxDao.insertBox(data)
        }
    }


    private fun addRepository(repository: BaseEntity.Repository) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.repositoryDao.insertRepository(repository)
        }
    }

    fun deleteAllRepository() {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.repositoryDao.deleteRepositorys()
            getAllData()
        }
    }


    fun deleteAllBox() {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.boxDao.deleteBoxes()
            getAllData()
        }
    }

    fun deleteBox(box: BaseEntity.Box) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.boxDao.deleteBox(box)
            getAllData()
        }
    }

    fun deleteRepository(repository: BaseEntity.Repository) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.repositoryDao.deleteRepository(repository)
            getAllData()
        }
    }

    fun getAllData() = viewModelScope.launch(Dispatchers.Main) {
        val box = getAllBox()
        val repositorys = getAllRepository()
        repositorys?.let {
            listResult[0].apply {
                childs.clear()
                childs.addAll(repositorys)
                count = childs.count()
                name = "箱子数 $count"
            }
        }
        box?.let {
            listResult[1].apply {
                childs.clear()
                childs.addAll(box)
                count = childs.count()
                name = "盒子数 $count"
            }
        }
        resultLivedata.postValue(listResult)
    }

    private suspend fun getAllBox(): List<BaseEntity.Box>? {
        return viewModelScope.async(Dispatchers.IO) {
            val boxes = Repository.getAllBox()
            return@async boxes
        }.await()
    }

    private suspend fun getAllRepository(): List<BaseEntity.Repository>? {
        return viewModelScope.async(Dispatchers.IO) {
            val boxes = Repository.getAllRepository()
            return@async boxes
        }.await()
    }

    fun modifyBox(modifyBox: BaseEntity.Box) {
        viewModelScope.launch(Dispatchers.IO) {
            modifyBoxDataBase(modifyBox)
            getAllData()
        }
    }

    fun modifyRepository(repository: BaseEntity.Repository) {
        viewModelScope.launch(Dispatchers.IO) {
            modifyRepositoryDatabase(repository)
            getAllData()
        }
    }

    private fun modifyBoxDataBase(modifyBox: BaseEntity.Box) {
        viewModelScope.launch(Dispatchers.IO) {
            val updateBox = Repository.boxDao.updateBox(modifyBox)
            Log.d("viewmodel", updateBox.toString())
        }
    }

    private suspend fun modifyRepositoryDatabase(modifyRepository: BaseEntity.Repository) {
        viewModelScope.launch(Dispatchers.IO) {
            val updateRepository = Repository.repositoryDao.updateRepository(modifyRepository)
            Log.d("viewmodel", updateRepository.toString())
        }

    }
}
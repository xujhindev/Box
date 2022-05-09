package com.xujhin.box.ui.main

import com.xujhin.box.App
import com.xujhin.box.entity.BaseEntity
import com.xujhin.box.entity.database.BoxDataBase
import com.xujhin.box.entity.database.RepositoryDatabase

object Repository {

    val boxDao = BoxDataBase.getDatabase(App.application).boxDao
    val repositoryDao = RepositoryDatabase.getDatabase(App.application).repositoryDao

    fun getAllBox(): List<BaseEntity.Box>? {
        return boxDao.getAllBox()
    }

    fun getAllRepository(): List<BaseEntity.Repository>? {
        return repositoryDao.getAllRepository()
    }
}
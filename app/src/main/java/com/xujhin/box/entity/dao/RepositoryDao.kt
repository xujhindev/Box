package com.xujhin.box.entity.dao

import androidx.room.*
import com.xujhin.box.entity.BaseEntity

@Dao
interface RepositoryDao {

    @Insert
    fun insertRepository(vararg repository: BaseEntity.Repository)

    @Update(entity = BaseEntity.Repository::class)
    fun updateRepository(vararg repository: BaseEntity.Repository):Int

    @Delete
    fun deleteRepository(vararg repository: BaseEntity.Repository)


    @Query("DELETE FROM repository")
    fun deleteRepositorys()

    @Query("SELECT * FROM repository ORDER BY ID DESC")
    fun getAllRepository(): List<BaseEntity.Repository>?
}
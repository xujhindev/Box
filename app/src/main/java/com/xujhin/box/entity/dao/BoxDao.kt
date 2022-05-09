package com.xujhin.box.entity.dao

import androidx.room.*
import com.xujhin.box.entity.BaseEntity

@Dao
interface BoxDao {

    @Insert
    fun insertBox(vararg repository: BaseEntity.Box)

    @Update(entity = BaseEntity.Box::class)
    fun updateBox(vararg repository: BaseEntity.Box):Int

    @Delete
    fun deleteBox(vararg repository: BaseEntity.Box)


    @Query("DELETE FROM box")
    fun deleteBoxes()

    @Query("SELECT * FROM box ORDER BY ID DESC")
    fun getAllBox(): List<BaseEntity.Box>?
}
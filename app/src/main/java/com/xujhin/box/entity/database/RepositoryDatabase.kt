package com.xujhin.box.entity.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.xujhin.box.entity.BaseEntity
import com.xujhin.box.entity.dao.RepositoryDao

@Database(entities = [BaseEntity.Repository::class], version = 1, exportSchema = false)
abstract class RepositoryDatabase : RoomDatabase() {
    abstract val repositoryDao: RepositoryDao

    companion object {
        private var instance: RepositoryDatabase? = null

        // 使用单例模式
        @Synchronized
        fun getDatabase(context: Context): RepositoryDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                RepositoryDatabase::class.java, "repository_data_base"
            )
                .build().apply { instance = this }
        }
    }
}
package com.xujhin.box.entity.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.xujhin.box.entity.BaseEntity
import com.xujhin.box.entity.dao.BoxDao
import com.xujhin.box.entity.dao.RepositoryDao


@Database(
    entities = [BaseEntity.Box::class],
    version = 1,
    exportSchema = false
)
abstract class BoxDataBase : RoomDatabase() {
    abstract val boxDao: BoxDao

    companion object {
        private var instance: BoxDataBase? = null

        // 使用单例模式
        @Synchronized
        fun getDatabase(context: Context): BoxDataBase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                BoxDataBase::class.java, "box_data_base"
            ).build().apply { instance = this }
        }
    }
}
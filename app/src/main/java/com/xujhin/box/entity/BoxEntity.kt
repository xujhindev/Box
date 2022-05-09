package com.xujhin.box.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.StringJoiner


sealed class BaseEntity(
    open val name: String,
    open val weight: String,
    open val height: String,
    open val width: String,
    open val length: String
) {
    @Entity(tableName = "repository")
    data class Repository(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        override var name: String,
        override var weight: String,
        override var height: String,
        override var width: String,
        override var length: String
    ) : BaseEntity(name, weight, height, width, length)

    @Entity(tableName = "box")
    data class Box(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        override var name: String,
        override var weight: String,
        override var height: String,
        override var width: String,
        override var length: String
    ) : BaseEntity(name, weight, height, width, length)
}

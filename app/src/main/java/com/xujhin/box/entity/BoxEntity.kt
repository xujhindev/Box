package com.xujhin.box.entity


sealed class BaseEntity(
    open val name: String,
    open val weight: String,
    open val height: String,
    open val width: String,
    open val length: String
) {
    data class Repository(
        override val name: String,
        override val weight: String,
        override val height: String,
        override val width: String,
        override val length: String
    ) : BaseEntity(name, weight, height, width, length)

    data class Box(
        override val name: String,
        override val weight: String,
        override val height: String,
        override val width: String,
        override val length: String
    ) : BaseEntity(name, weight, height, width, length)
}

package com.xujhin.box.utils.sp

import com.tencent.mmkv.MMKV

object SpHelper {
    private val mmkv = MMKV.defaultMMKV()
    fun save(key: String, value: String) {
        mmkv.encode(key, value)
    }

    inline fun <reified T> getValue(){
        val java = T::class.java
    }

    fun getString(account: String): String? {
        return mmkv.getString(account, "")
    }
}
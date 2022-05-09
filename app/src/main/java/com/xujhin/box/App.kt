package com.xujhin.box

import android.app.Application
import com.tencent.mmkv.MMKV

class App : Application() {

    companion object{
        lateinit var application: Application
    }
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        application = this
    }
}
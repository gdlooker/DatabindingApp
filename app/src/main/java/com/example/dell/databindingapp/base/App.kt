package com.example.dell.databindingapp.base

import android.app.Application

class App:Application() {
    companion object {
        private var instance: App? = null
        fun instance() = instance ?: throw Throwable("instance 还未初始化完成")
    }

    override fun onCreate() {
        super.onCreate()
        instance = this;
    }
}
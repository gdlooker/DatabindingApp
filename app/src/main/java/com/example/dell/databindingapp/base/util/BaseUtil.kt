package com.example.dell.databindingapp.base.util

import android.content.Context
import android.content.SharedPreferences
import com.example.dell.databindingapp.base.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.withContext

const val mySharedName = "mySharedPreferences"

val mySharedPreferences: SharedPreferences by lazy {
    App.instance().createInstance()
}

private fun Context.createInstance(): SharedPreferences {
    return getSharedPreferences(mySharedName, Context.MODE_MULTI_PROCESS or Context.MODE_PRIVATE)
}

private var mTicker: ReceiveChannel<Unit>? = null

//是否停止计时
var stopTimer = false
    set(value) {
        field = value
        mTicker?.cancel()
        mTicker = null
    }

/**
 *开启计时器
 */
fun startTimer(interval: Int, function: (Long) -> Unit) {
    stopTimer = false
    mTicker = ticker(interval.toLong(), 0)
    var totalTime = 0L


}

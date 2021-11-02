package com.example.dell.databindingapp.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.PowerManager
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


/**
 * 唤醒手机屏幕
 */
@SuppressLint("InvalidWakeLockTag")
fun wakeUpScreen(context: Context?) {
    if(context == null) {
        return
    }
    // 获取电源管理器对象
    val pm = context.getSystemService(AppCompatActivity.POWER_SERVICE) as PowerManager
    val screenOn = pm.isInteractive
    if (!screenOn) {
        // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        val wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright")
        wl.acquire(10000) // 点亮屏幕
        wl.release() // 释放
    }
}

/**
 * 屏幕解锁
 */
fun unlockScreen(activity: Activity) {
    val keyguardManager = activity.getSystemService(AppCompatActivity.KEYGUARD_SERVICE) as KeyguardManager
//    val keyguardLock = keyguardManager.newKeyguardLock("unLock")
//    // 屏幕锁定
//    keyguardLock.reenableKeyguard()
//    keyguardLock.disableKeyguard() // 解锁
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        keyguardManager.requestDismissKeyguard(activity, null)
    } else {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

/**
 * 点亮屏幕
 * 机型设配不够 use [wakeUpScreen] instead
 */
fun turnScreenOn(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        activity.setTurnScreenOn(true)
    } else {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}
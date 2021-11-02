package com.example.dell.databindingapp.util

import android.content.Context
import android.content.SharedPreferences
import com.example.dell.databindingapp.base.util.mySharedPreferences


object BaseSharedPreferences {
    //用于单例实例化
    private var instance: SharedPreferences = mySharedPreferences

    fun getInstance(): SharedPreferences {
        return instance
    }

    fun putStr(key: String, value: String) {
        instance.edit().putString(key, value)?.apply()
    }

    fun getStr(key: String, defVal: String): String? {
        return instance.getString(key, defVal)
    }

    fun getStr(key: String): String? {
        return getStr(key, "")
    }

    fun putInt(key: String, value: Int) {
        instance.edit().putInt(key, value)?.apply()
    }

    fun getInt(key: String, defVal: Int): Int? {
        return instance.getInt(key, defVal)
    }

    fun getInt(key: String): Int? {
        return getInt(key, 0)
    }

    fun putLong(key: String, value: Long) {
        instance.edit().putLong(key, value)?.apply()
    }

    fun getLong(key: String, defVal: Long): Long {
        return instance.getLong(key, defVal)
    }

    fun putFloat(key: String, value: Float) {
        instance.edit().putFloat(key, value)?.apply()
    }

    fun getFloat(key: String, defVal: Float): Float {
        return instance.getFloat(key, defVal)
    }

    fun getLong(key: String): Long? {
        return getLong(key, 0)
    }

    fun putBool(key: String, value: Boolean) {
        instance.edit().putBoolean(key, value)?.apply()
    }

    fun getBool(key : String, defVal: Boolean) : Boolean? {
        return instance.getBoolean(key, defVal)
    }

    fun getBool(key : String) : Boolean? {
        return getBool(key, false)
    }

    /**
     * 获取键盘高度
     */
    fun getHeight(context: Context): Int {
        return instance.getInt("keyboard.height", context.dip2px(220f))
    }

    /**
     * 设置键盘高度
     */
    fun setHeight(context: Context, height: Int): Boolean {
        return instance.edit().putInt("keyboard.height", height).commit()
    }

    private inline fun Context.dip2px(dipValue: Float): Int {
        val scale = this.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

}
package com.example.dell.databindingapp.widget.keyboardutil;

import android.content.Context
import android.content.SharedPreferences
import com.example.dell.databindingapp.base.App
import com.example.dell.databindingapp.util.dp

object KeyBoardSharedPreferences {
    //单例实例化
    private val instance: SharedPreferences by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        createInstance()
    }

    private fun createInstance(): SharedPreferences {
        return App.Companion.instance().getSharedPreferences("keyboar", Context.MODE_PRIVATE)
    }

    /**
     * 获取键盘高度
     */
    fun getHeight(): Int {
        return instance.getInt("keyboard.height", 290.dp.toInt())
    }

    /**
     * 设置键盘高度
     */
    fun setHeight(height: Int): Boolean {
        return instance.edit().putInt("keyboard.height", height).commit()
    }

    private inline fun Context.dip2px(dipValue: Float): Int {
        val scale = this.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

}
package com.example.dell.databindingapp.widget.keyboardutil;

import com.example.dell.databindingapp.util.dp


/**
 * 获取保存的键盘高度
 */
object KeyboardUtil {
    //键盘高度
    private var keyHeight = 0

    /**
     * 设置键盘高度
     */
    fun setKeyboardHeight(height: Int) {
        //键盘显示
        if (height > 10) {
            if (keyHeight != height && height >= 288.dp.toInt()) {//设置可用的最低高度（290）不小于289dp
                keyHeight = height
                //保存键盘高度
                KeyBoardSharedPreferences.setHeight(keyHeight)
            }
        }
    }

    /**
     * 获取键盘高度
     */
    fun getKeyboardHeight(): Int {
        return if (keyHeight == 0) {
            KeyBoardSharedPreferences.getHeight()
        } else {
            keyHeight
        }

    }

}
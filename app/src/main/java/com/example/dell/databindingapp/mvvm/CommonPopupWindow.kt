package com.example.dell.databindingapp.mvvm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

class CommonPopupWindow<T : ViewDataBinding> @JvmOverloads constructor(
        context: Context,
        layoutId: Int,
        isOutsideTouchable: Boolean = true,
        popWith: Int? = ViewGroup.LayoutParams.WRAP_CONTENT,
        popHeight: Int? = ViewGroup.LayoutParams.WRAP_CONTENT,
) : PopupWindow() {

    var context: Context = context

    var binding: T = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false)

    //初始化回调
    var initBinding: (T.() -> Unit)? = null

    init {
        contentView = binding.root
        this.isOutsideTouchable = isOutsideTouchable
        width = popWith?:ViewGroup.LayoutParams.WRAP_CONTENT
        height = popHeight?:ViewGroup.LayoutParams.WRAP_CONTENT

        initBinding?.invoke(binding)
    }
}
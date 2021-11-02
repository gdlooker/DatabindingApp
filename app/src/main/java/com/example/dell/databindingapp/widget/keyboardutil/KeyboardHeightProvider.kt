package com.example.dell.databindingapp.widget.keyboardutil;

import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow
import androidx.lifecycle.LifecycleOwner
import com.example.dell.databindingapp.util.onDestroy
import com.example.dell.databindingapp.util.screenPoint

/**
 * 键盘高度测量
 */
class KeyboardHeightProvider(view: View, val function: (keyboardHeight: Int) -> Unit) :
        PopupWindow(view.context),
        ViewTreeObserver.OnGlobalLayoutListener {

    //当前PopupWindow最大的显示高度
    private var maxHeight = 0

    init {
        contentView = View(view.context)
        width = 0
        height = ViewGroup.LayoutParams.MATCH_PARENT
        //设置背景
        setBackgroundDrawable(ColorDrawable(0))
        //设置键盘弹出模式
        softInputMode =
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        inputMethodMode = INPUT_METHOD_NEEDED
        //设置监听
        contentView.viewTreeObserver.addOnGlobalLayoutListener(this)
        //显示弹窗
        view.post {
            try {
                showAtLocation(
                        view,
                        Gravity.NO_GRAVITY,
                        0,
                        0
                )
            } catch (e: Exception) {

            }
        }
        //监测生命 防止泄露
        val lifecycle = view.context
        if (lifecycle is LifecycleOwner) {
            lifecycle.onDestroy {
                kotlin.runCatching {
                    if(view.windowToken != null && isShowing) {
                        dismiss()
                    }
                }
            }
        }
    }

    override fun onGlobalLayout() {
        val rect = Rect()
        contentView.getWindowVisibleDisplayFrame(rect)
        if (rect.bottom > maxHeight) {
            maxHeight = rect.bottom
        }
        //键盘的高度
        var keyboardHeight = maxHeight - rect.bottom
        if(keyboardHeight > screenPoint.y / 2) {
            keyboardHeight = screenPoint.y / 2
        }
        function(keyboardHeight)
    }


}

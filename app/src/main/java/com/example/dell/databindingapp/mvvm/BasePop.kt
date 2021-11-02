package com.example.dell.databindingapp.mvvm

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.dell.databindingapp.R


/**
 * 公共弹窗
 */
open class BasePop<T : ViewDataBinding> : DialogFragment {
    lateinit var binding: T

    var initBinding: (T.() -> Unit)? = null

    var onDismissListener: (() -> Unit)? = null

    //是否浮动
    var floating = true

    //是否正在显示
    var isShowing = false

    //键盘模式
    var inputMode: Int = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
    var windowColor: Int = android.R.color.transparent
    var windowTheme: Int = R.style.Common_Theme_Dialog

    //背景是否透明
    var backgroundWhite = false

    private var layoutId: Int = 0
    private var isCanceled: Boolean = false
    private var setParameter: (WindowManager.LayoutParams.() -> Unit)? = null
    var function: (T.() -> Unit)? = null


    constructor() : super()

    constructor(
            layoutId: Int,
            isCanceled: Boolean = false,
            //默认弹窗设置
            setParameter: (WindowManager.LayoutParams.() -> Unit) = {
                gravity = Gravity.BOTTOM
                width = ViewGroup.LayoutParams.MATCH_PARENT
                windowAnimations = R.style.common_bottomPop
            },
            //初始化Binding
            function: (T.() -> Unit)? = null) {
        this.layoutId = layoutId
        this.isCanceled = isCanceled
        this.setParameter = setParameter
        this.function = function
        //重置为false
        isShowing = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //将布局膨胀为对话框或嵌入片段
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        function?.invoke(binding)
        initBinding?.invoke(binding)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!floating) {
            setStyle(STYLE_NO_FRAME, windowTheme)
        }
    }

    /**
     * 这只在系统调用时创建的布局在一个对话。
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(isCanceled)
        val window = dialog.window
        window?.setBackgroundDrawableResource(windowColor)
        window?.setSoftInputMode(inputMode)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val win = dialog?.window
        win?.apply {
            val params = attributes
            setParameter?.also { params.it() }
            if (backgroundWhite) {
                params.dimAmount = 0f
            }
            attributes = params
        }

    }


    /**
     * 设置高
     */
    fun setHeight(height: Int) {
        val win = dialog?.window
        win?.apply {
            val params = attributes
            params.height = height
            attributes = params
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
//        dismiss()
        if (isShowing) {
            return
        }
        isShowing = true
        if (this@BasePop.isAdded) {
//            manager.commit {
//                remove(this@BasePop)
//            }
        }
        //可能会发生异常Can not perform this action after onSaveInstanceState
//        super.show(manager, tag)
//        androidx.fragment.app.FragmentManager.checkStateLoss(FragmentManager.java:1703)
//        androidx.fragment.app.FragmentManager.enqueueAction(FragmentManager.java:1743)
//        androidx.fragment.app.BackStackRecord.commitInternal(BackStackRecord.java:321)
//        androidx.fragment.app.BackStackRecord.commit(BackStackRecord.java:286)
//        androidx.fragment.app.DialogFragment.show(DialogFragment.java:181)
        //解决发生Can not perform this action after onSaveInstanceState异常
        try {
            val mDismissed = DialogFragment::class.java.getDeclaredField("mDismissed")
            mDismissed.isAccessible = true
            mDismissed.set(this, false)

            val mShownByMe = DialogFragment::class.java.getDeclaredField("mShownByMe")
            mShownByMe.isAccessible = true
            mShownByMe.set(this, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    /**
     * 弹窗
     */
    fun show(fragmentManager: FragmentManager) {
        if (fragmentManager.isDestroyed){
            return
        }
        show(fragmentManager, "")
    }


    /**
     * 消失弹窗
     */
    override fun dismiss() {
        if (!isShowing) {
            return
        }
        isShowing = false
        //可能会发生异常Can not perform this action after onSaveInstanceState
//        super.dismiss()
        super.dismissAllowingStateLoss()
    }

    /**
     * 弹窗消失监听
     */
    override fun onDismiss(dialog: DialogInterface) {
        isShowing = false
        onDismissListener?.invoke()
        super.onDismiss(dialog)
    }
}
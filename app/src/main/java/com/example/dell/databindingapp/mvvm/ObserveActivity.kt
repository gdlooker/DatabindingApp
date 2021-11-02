package com.example.dell.databindingapp.mvvm

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter

import kotlinx.coroutines.Dispatchers

open class ObserveActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()

    }

    /**
     * 跳转登录页面
     */
    fun toLoginPage() {
        ARouter.getInstance().build("/module_login/LoginRegisterActivity")
                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation(this)
    }

    /**
     * 跳转登录第一个界面
     */
    fun toLoginFirstPage() {
        ARouter.getInstance().build("/module_login/LoginFirstPageActivity")
                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation(this)
    }

    /**
     * 退出和关闭连接
     */
    fun logoutDisconnect(showToast: Boolean = false, function: ((Boolean) -> Unit)? = null) {

    }

    /**
     * 退出登录并关闭连接返回登录界面
     */
    fun logoutDisconnectToLoginPage(showToast: Boolean = false) {
        logoutDisconnect(showToast)
        toLoginPage()
    }

    /**
     * 退出登录并关闭连接返回第一个界面
     */
    fun logoutDisconnectToFirstPage(showToast: Boolean = false) {
        logoutDisconnect(showToast)
        ARouter.getInstance().build("/module_login/LoginFirstPageActivity")
                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation(this)
    }
}
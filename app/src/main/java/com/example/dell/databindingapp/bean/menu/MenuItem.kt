package com.example.dell.databindingapp.bean.menu

import androidx.lifecycle.LiveData

import java.io.Serializable

data class MenuItem(
        val type: String,
        var icon: Int,
        var title: String,
        var value: Any? = null,
        val showTopLine: Boolean = false,
        val showBottomLine: Boolean = true,
        val showArrow: Boolean = true,
        var visibility: Boolean = true
) : Serializable {

    //新的好友验证时 通知数据
    var notice: LiveData<List<NotificationTable>>? = null

    /**
     * 所有菜单类型
     */
    companion object {
        val TYPE_NEW_FRIEND = "newFriend"
        val TYPE_GROUP = "myGroup"
        val TYPE_PEOPLE_NEARBY = "peopleNearby"
        val TYPE_ADD_PHONE_FRIEND = "addPhoneFriend"
        val TYPE_OFFICIAL_NUMBER = "officialNumber"
    }

    override fun equals(other: Any?): Boolean {
        return other is MenuItem && type == other.type
    }
}
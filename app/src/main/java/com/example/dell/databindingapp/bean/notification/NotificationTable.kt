package com.example.dell.databindingapp.bean.menu
import com.example.dell.databindingapp.base.data.BaseData
import com.example.dell.databindingapp.util.toJson


/**
 * Created by DengLongFei
 * 2020/05/27
 * 消息通知表
 */

data class NotificationTable(

        var sMsgId: String,//消息id 主键
        var sGrpId: String,//群id
        var msgTime: Long,//消息时间
        var sOprUserId: String,//操作者id
        var sMnpledUserId: List<String>,//被操作用户id （例如 某某邀请谁加入群）
        var sContent: String,//消息体 是个json 根据具体类型具体解析
        var notifyType: Int,//消息类型 和NotificationType对应 (自己定义的类型-11群消息) SnsCommon.NotificationType
        var nMainNotifyType: Int,// 通知的通知源NotifyMainType对应
        var read: Boolean,//当前消息是否已读

) {
    //当前所属用户id
    var userId = ""

    constructor() : this(
            "",
            "",
            0L,
            "",
            arrayListOf(),
            Any().toJson(),
            -1,
            -1,
            false
    ) {
        userId = BaseData.userId
    }


    companion object {
        //群组通知
        const val GROUP_NOTICE = -11
        //添加好友验证
        const val VERIFY_FRIEND = -12
        //交易提醒的通知
        const val TRADE_REMINDER = -13
        //公众号通知
        const val SUBJECT_NOTICE = -14

    }
}
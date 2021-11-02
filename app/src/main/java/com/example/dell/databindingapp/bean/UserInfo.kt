package com.example.dell.databindingapp.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.io.Serializable

/**
 * Created by asus
 * Date:2020/4/30
 * Update:
 * Description:
 */
data class UserInfo(
        var userId: String? = null,
        var username: String? = null,
        var type: Int = 0,
        var nickname: String? = null,
        var token: String? = null,
        var gender: Int = 0,
        var imgUrl: String? = null,
        var sign: String? = null,
        var birthDay: String? = null,
        var email: String? = null,
        var location: String? = null,//所在地
        var countryArea: String? = null,
        var password: String? = null,
        var phoneNo: String? = null,
        var smsCode: String? = null,
        var defaultDeviceId: String? = null,
        var optFlowToken: String? = null,
        var enableState: Int = 0,
        var msgSwitch: Int = 1, //是否开启消息状态 1开启 0关闭
        var tradeSwitch: Int = 0, //是否开启交易消息状态 1开启 0关闭
        var searchHistory: String? = null,

) : BaseObservable(), Serializable {

    @get:Bindable
    var isChecked: Boolean = false
        set(value) {
            field = value

        }
}
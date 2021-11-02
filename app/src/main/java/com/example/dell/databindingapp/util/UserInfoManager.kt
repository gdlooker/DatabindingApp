package com.example.dell.databindingapp.util

import com.example.dell.databindingapp.base.data.BaseData
import com.example.dell.databindingapp.bean.UserInfo

/**
 * Created by asus
 * Date:2020/3/19
 * Update:
 * Description:
 */
class UserInfoManager private constructor() {
    fun saveUserInfo(loginInfo: UserInfo?) {
        var editor = BaseSharedPreferences.getInstance().edit()
                .putString(KEY_USER_ID, loginInfo?.userId)
                .putString(KEY_USER_NAME, loginInfo?.username)
                .putString(KEY_NICKNAME, loginInfo?.nickname)
                .putString(KEY_IMG_URL, loginInfo?.imgUrl)
                .putInt(KEY_GENDER, loginInfo?.gender ?: 0)
                .putString(KEY_BIRTHDAY, loginInfo?.birthDay)
                .putString(KEY_SIGN, loginInfo?.sign)
                .putString(KEY_LOCATION, loginInfo?.location)
                .putString(KEY_COUNTRY_AREA, loginInfo?.countryArea)
                .putString(KEY_PHONE_NO, loginInfo?.phoneNo)
                .putString(KEY_PHONE_NO, loginInfo?.phoneNo)
                .putInt(MSG_SWITCH, loginInfo?.msgSwitch ?: 1)
                .putInt(TRADE_SWITCH, loginInfo?.tradeSwitch ?: 1)
                .putString(KEY_SEARCH_HISTORY, loginInfo?.searchHistory)
        //密码
        if (!loginInfo?.password.isNullOrEmpty()) {
            editor.putString(KEY_PASSWORD, loginInfo?.password)
        }
        //设备唯一号
        if (!loginInfo?.defaultDeviceId.isNullOrEmpty()) {
            editor.putString(KEY_DEFAULT_DEVICE_ID, loginInfo?.defaultDeviceId)
        }
        //登录token
        if (!loginInfo?.token.isNullOrEmpty()) {
            editor.putString(KEY_TOKEN, loginInfo?.token)
        }
        editor.apply()
    }


    val userInfo: UserInfo
        get() {
            val spf = BaseSharedPreferences
            val userInfo = UserInfo()
            userInfo.userId = spf.getStr(KEY_USER_ID)
            userInfo.username = spf.getStr(KEY_USER_NAME)
            userInfo.nickname = spf.getStr(KEY_NICKNAME)
            userInfo.imgUrl = spf.getStr(KEY_IMG_URL)
            userInfo.gender = spf.getInt(KEY_GENDER) ?: 0
            userInfo.token = spf.getStr(KEY_TOKEN)
            userInfo.sign = spf.getStr(KEY_SIGN)
            userInfo.birthDay = spf.getStr(KEY_BIRTHDAY)
            userInfo.location = spf.getStr(KEY_LOCATION)
            userInfo.countryArea = spf.getStr(KEY_COUNTRY_AREA)
            userInfo.password = spf.getStr(KEY_PASSWORD)
            userInfo.phoneNo = spf.getStr(KEY_PHONE_NO)
            userInfo.defaultDeviceId = spf.getStr(KEY_DEFAULT_DEVICE_ID)
            userInfo.msgSwitch = spf.getInt(MSG_SWITCH) ?: 1
            userInfo.tradeSwitch = spf.getInt(TRADE_SWITCH) ?: 1
            userInfo.searchHistory = spf.getStr(KEY_SEARCH_HISTORY)
//            userInfo.smsCode = spf.getStr(KEY_SMS_CODE)
//            userInfo.enableState = spf.getInt(KEY_ENABLE_STATE) ?: 0
            return userInfo
        }

    fun clearUserInfo() {
        BaseData.lastUpdateContactTime = 0L
        BaseData.userId = ""
        //清空其他信息
        BaseSharedPreferences.getInstance().edit()
                .remove(KEY_USER_ID)
                .remove(KEY_USER_NAME)
                .remove(KEY_NICKNAME)
                .remove(KEY_IMG_URL)
                .remove(KEY_GENDER)
                .remove(KEY_TOKEN)
                .remove(KEY_SIGN)
                .remove(KEY_BIRTHDAY)
                .remove(KEY_LOCATION)
                .remove(KEY_COUNTRY_AREA)
                .remove(KEY_PHONE_NO)
                .remove(KEY_DEFAULT_DEVICE_ID)
                .remove(HAS_PASSWORD)
                .remove(KEY_SEARCH_HISTORY)
//                .remove(KEY_SMS_CODE)
//                .remove(KEY_ENABLE_STATE)
                .apply()
    }

    companion object {
        const val KEY_USER_ID = "IMUserId"
        const val KEY_USER_NAME = "IMUserName"
        const val KEY_NICKNAME = "IMNickname"
        const val KEY_IMG_URL = "IMImgUrl"
        const val KEY_TYPE = "IMType"
        const val KEY_TOKEN = "IMToken"
        const val KEY_PUSH_TOKEN = "pushToken"
        const val KEY_GENDER = "gender"
        const val KEY_SIGN = "sign"
        const val KEY_BIRTHDAY = "birthday"
        const val KEY_LOCATION = "location"
        const val KEY_COUNTRY_AREA = "countryArea"
        const val KEY_PASSWORD = "password"
        const val KEY_PHONE_NO = "phoneNo"
        const val KEY_SMS_CODE = "smsCode"
        const val KEY_DEFAULT_DEVICE_ID = "defaultDeviceId"
        const val KEY_DEVICE_VERSION = "deviceVersion"
        const val KEY_DEVICE_TOKEN = "deviceToken"
        const val KEY_PUSH_TYPE = "pushType"
        const val HAS_PASSWORD = "hasPassword"
        const val MSG_SWITCH = "msgSwitch"
        const val TRADE_SWITCH = "tradeSwitch"
        const val KEY_SEARCH_HISTORY = "searchHistory"
        const val KEY_FULL_PHONE_NO = "fullPhoneNo"
        val instance = UserInfoManager()
    }
}
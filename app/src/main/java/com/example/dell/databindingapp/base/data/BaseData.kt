package com.example.dell.databindingapp.base.data

import androidx.lifecycle.MutableLiveData
import com.example.dell.databindingapp.base.util.mySharedPreferences
import com.example.dell.databindingapp.bean.KickOutInfo
import com.example.dell.databindingapp.bean.UserInfo
import com.example.dell.databindingapp.util.UserInfoManager


//import androidx.paging.PagingConfig



/**
 * 请求基础数据
 */
object BaseData {


    //谁信官网，App下载链接
    const val shuixinWebsite: String = "http://home.imshuixin.com"

    //谁信社区规范
    const val shuixinPlazaAgreement: String = "https://s-yfb.imshuixin.com:18845/CommunityNorms.html"

    //谁信软件服务协议
    const val shuixinSoftwareAgreement: String = "https://s.imshuixin.com/privacy.html"

    //谁信助手隐私协议
    const val shuixinAgentSecret: String = "https://partnerapi-yfb.imshuixin.com:18844/im-partner-api/static/agreement/ios/registrationPrivacyAgreement.html"

    //谁信助手服务协议
    const val shuixinAgentService: String = "https://partnerapi-yfb.imshuixin.com:18844/im-partner-api/static/agreement/ios/registrationServiceAgreement.html"

    //谁信隐私保护指引
    const val shuixinPrivacy: String = "https://s.imshuixin.com/Privacyagreement.html"

    //盛迪嘉服务协议
    const val sdjPrivacy: String = "https://s.imshuixin.com/SDJprivacy.html"
    //盛迪嘉支付服务协议
    const val sdjPayPrivacy: String = "https://s.imshuixin.com/PaymentAccountServiceAgreement.html"

    //谁信快捷支付服务协议
    const val shuixinPaymentAgreement: String = "https://s.imshuixin.com/CardAgreement.html"

    const val shuixinPromotionRules: String = "https://s.imshuixin.com/PromotionRules.html"

    //谁信用户注销协议
    const val shuixinSoftwareLogoffAgreement: String = "https://s.imshuixin.com/Userlogout.html"

    //动态分享链接h5界面
    const val shareDynamicUrl: String = "https://s-yfb.imshuixin.com:18845/activity/share/?hexPublishId="

    const val RECHARGE_AGREEMENT_URL: String = "https://s.imshuixin.com/RechargeAgreement.html"

    //表情包协议
    const val memePrivacy: String = "https://s.imshuixin.com/Expressionserviceagreement.html"

    //转账协议
    const val accountsTransferAgreement = "https://s.imshuixin.com/TransferAccountsServiceAgreement.html"

    //当前正在聊天的用户id
    var chatId: String = ""

    //1，谁信助手ID：500000 2，系统通知ID：500001
    const val systemNotification = "500001"

    //-1：未连接
    const val STATUS_DISCONNECT = -1

    //0：连接中
    const val STATUS_CONNECTING = 0

    //1：收取中
    const val STATUS_RECEIVING = 1

    //2：完成
    const val STATUS_COMPLETE = 2

    //结束界面requestCode
    const val REQUEST_CODE_FINISH = 7878

    //分页配置
//    val pagingConfig by lazy { PagingConfig(pageSize = 10) }

    //打开红包弹窗别名
    const val redPackagePop = "redPackagePop"

    //更新RecyclerView指定的position
    var updatePosition: ((Int) -> Unit)? = null

    //http错误码消息
    var msgCode: ((Int) -> Unit)? = null

    //被踢出监听
    var onKickOut: ((KickOutInfo?) -> Unit)? = null

    //已经跳转到登录页
    var isAlreadyToLoginPage = false

    //网络状态是否可用
    var networkAvailable = false

    //未连接socket（聊天服务）显示
    val showNoConnectedSocket = MutableLiveData(false)

    //是否登陆成功socket（聊天服务）
    val isLoginSocket = MutableLiveData(false)

    //聊天会话页socket状态显示，0：连接中 1：收取中，2：完成
    val chatSocketStatus = MutableLiveData<Int>()

    //抢红包成功与否通知
    var redPacketNotification: ((String) -> Unit)? = null

    //下载完语音通知播放
    var downloadVoidPlay: ((String) -> Unit)? = null

    //焚烧前语音通知停止
    var burnVoiceStop: ((String) -> Unit)? = null

    //焚烧前视频通知停止
    var burnVideoStop: ((String) -> Unit)? = null

    //焚烧前图片通知退出预览
    var burnImageExit: ((String) -> Unit)? = null

    //焚烧前自定义表情通知退出预览
    var burnEmojiExit: ((String) -> Unit)? = null

    //用户登录链路失效过期(app后台自动重新login)
    var reLoginSocket: (() -> Unit)? = null

    //挂断
    var handleUp: (() -> Unit)? = null

    var isCancelUpdate: Boolean = false

    //跳转
    var jumpIndexEvent: ((Int) -> Unit)? = null

    //消息迁移socket状态显示，0：连接中 1：收取中，2：完成 ，-1:未连接
    val MsgMoveSocketStatus = MutableLiveData<Int>()

    @JvmStatic
    //用户ID
    var userId: String = ""
        get() {
            if (field.isEmpty()) {
                return mySharedPreferences.getString(UserInfoManager.KEY_USER_ID, "") ?: ""
            }
            return field
        }

    //用户名
    var username: String = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString(UserInfoManager.KEY_USER_NAME, value).apply()
        }
        get() {
            if (field.isEmpty()) {
                return mySharedPreferences.getString(UserInfoManager.KEY_USER_NAME, "") ?: ""
            }
            return field
        }

    //用户国家码
    var countryArea: String = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString(UserInfoManager.KEY_COUNTRY_AREA, value).apply()
        }
        get() = mySharedPreferences.getString(UserInfoManager.KEY_COUNTRY_AREA, "") ?: ""

    //用户手机号
    var phoneNo: String = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString(UserInfoManager.KEY_PHONE_NO, value).apply()
        }
        get() = mySharedPreferences.getString(UserInfoManager.KEY_PHONE_NO, "") ?: ""

    //用户登录token鉴权
    var authorization: String = ""
        set(value) {
            field = value
            if (field.isNotEmpty()) {
                mySharedPreferences.edit().putString(UserInfoManager.KEY_TOKEN, value).apply()
            }
        }
        get() {
            if (field.isEmpty()) {
                return mySharedPreferences.getString(UserInfoManager.KEY_TOKEN, "") ?: ""
            }
            return field
        }

    //用户头像url
    var imgUrl: String = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString(UserInfoManager.KEY_IMG_URL, value).apply()
        }
        get() {
            return mySharedPreferences.getString(UserInfoManager.KEY_IMG_URL, "") ?: ""
        }

    //用户性别
    var gender: Int = 0
        set(value) {
            field = value
            mySharedPreferences.edit().putInt(UserInfoManager.KEY_GENDER, value).apply()
        }
        get() {
            return mySharedPreferences.getInt(UserInfoManager.KEY_GENDER, 0)
        }

    //用户昵称
    var nickname: String = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString(UserInfoManager.KEY_NICKNAME, value).apply()
        }
        get() {
            return mySharedPreferences.getString(UserInfoManager.KEY_NICKNAME, "") ?: ""
        }

    //签名
    var signature: String = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString(UserInfoManager.KEY_SIGN, value).apply()
        }
        get() {
            return mySharedPreferences.getString(UserInfoManager.KEY_SIGN, "") ?: ""
        }


    //是否开启消息状态
    var msgSwitch: Int = -1
        set(value) {
            field = value
            mySharedPreferences.edit().putInt(UserInfoManager.MSG_SWITCH, value).apply()
        }
        get() {
            return if (field == -1) {
                mySharedPreferences.getInt(UserInfoManager.MSG_SWITCH, 1)
            } else {
                field
            }
        }

    //用户信息
    var userInfo: UserInfo = UserInfo()
        set(value) {
            field = value
            userId = value.userId ?: ""
            username = value.username ?: ""
            if (!value.token.isNullOrEmpty()) {
                authorization = value.token ?: ""
            }
            UserInfoManager.instance.saveUserInfo(value)
        }
        get() {
            return UserInfoManager.instance.userInfo
        }

    //设备token更新监听
    var deviceTokenUpdate: ((String) -> Unit)? = null

    //桌面图标未读数量更新监听
    var iconBadgeNumUpdate: ((Int) -> Unit)? = null

    var initPushFunction:(()->Unit)? = null

    //重要：设备token，登录聊天服务socket传入
    var deviceToken: String = ""
        set(value) {
            field = value
            deviceTokenUpdate?.invoke(value)
            mySharedPreferences.edit().putString(UserInfoManager.KEY_DEVICE_TOKEN, value).apply()
        }
        get() {
            if (field.isEmpty()) {

            }
            return field
        }

    /**
     * 是否已登录标志
     */
    val isLogin: Boolean
        get() {
            return userId.isNotEmpty()
        }

    /**
     * 语音消息是否设置听筒播放
     */
    var voiceIsReceiverPlay: Boolean = false
        set(value) {
            field = value
            mySharedPreferences.edit().putBoolean("voiceIsReceiverPlay", value).apply()
        }
        get() {
            return mySharedPreferences.getBoolean("voiceIsReceiverPlay", false)
        }

    /**
     * 是否已设置自启动
     */
    var hasSetAutoStartUp = false
        set(value) {
            field = value
            mySharedPreferences.edit().putBoolean("hasSetAutoStartUp", value).apply()
        }
        get() {
            return mySharedPreferences.getBoolean("hasSetAutoStartUp", false)
        }

    /**
     * 是否已设置自启动
     */
    var isSetAutoStartUpLater = true
        set(value) {
            field = value
            mySharedPreferences.edit().putBoolean("isSetAutoStartUpLater", value).apply()
        }
        get() {
            return mySharedPreferences.getBoolean("isSetAutoStartUpLater", true)
        }

    /**
     * 是否已设置自启动
     */
    var alwaysNotSetAutoStartUp = false
        set(value) {
            field = value
            mySharedPreferences.edit().putBoolean("alwaysNotSetAutoStartUp", value).apply()
        }
        get() {
            return mySharedPreferences.getBoolean("alwaysNotSetAutoStartUp", false)
        }

    //是否开启附近的人
    var peopleNearby: Boolean = true
        set(value) {
            field = value
            mySharedPreferences.edit().putBoolean("switchPeopleNearby_$userId", value).apply()
        }
        get() {
            return mySharedPreferences.getBoolean("switchPeopleNearby_$userId", true)
        }

    //是否开启附近的人
    var giftRedemptionMethod: Int = 1
        set(value) {
            field = value
            mySharedPreferences.edit().putInt("giftRedemptionMethod_$userId", value).apply()
        }
        get() {
            return mySharedPreferences.getInt("giftRedemptionMethod_$userId", 1)
        }


    /**
     * 最后一次更新联系人信息时间
     */
    var lastUpdateContactTime = 0L
        set(value) {
            field = value
            mySharedPreferences.edit().putLong("lastUpdateContactTime_${userId}", value).apply()
        }
        get() {
            return mySharedPreferences.getLong("lastUpdateContactTime_${userId}", 0L)
        }

    /**
     * 钱包零钱
     */
    var walletSmallChange = 0f
        set(value) {
            field = value
            mySharedPreferences.edit().putFloat("walletSmallChange_${userId}", value).apply()
        }
        get() {
            return mySharedPreferences.getFloat("walletSmallChange_${userId}", 0f)
        }

    /**
     * 是否开启了消息详情
     */
    var showMessageDetail = false
        set(value) {
            field = value

        }
        get() {
            return false
        }

    /**
     * 我的推荐人
     */
    var myReferrer = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString("myReferrer_${userId}", value).apply()
        }
        get() {
            return mySharedPreferences.getString("myReferrer_${userId}", "") ?: ""
        }

    /**
     * 我的邀请码
     */
    var myInvitationCode = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString("myInvitationCode_${userId}", value).apply()
        }
        get() {
            return mySharedPreferences.getString("myInvitationCode_${userId}", "") ?: ""
        }

    /**
     * 刷脸支付开关状态
     */
    var facePayStatus = 0
        set(value) {
            field = value
            mySharedPreferences.edit().putInt("faceStatus_${userId}", value).apply()
        }
        get() {
            return mySharedPreferences.getInt("faceStatus_${userId}", 0)
        }

    /**
     * 代理商级别或普通用户
     */
    var agentLevel = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString("agentLevel_${userId}", value).apply()
        }
        get() {
            return mySharedPreferences.getString("agentLevel_${userId}", "") ?: ""
        }

    /**
     * 广场默认选择范围对应key
     */
    var plazaDefaultRangeKey = "0"
        set(value) {
            field = value
            mySharedPreferences.edit().putString("plazaSelectRangeKey_$userId", value).apply()
        }
        get() {
            return mySharedPreferences.getString("plazaSelectRangeKey_$userId", "0") ?: "0"
        }

    /**
     * 部分好友可见，保存的可见好友用户ids
     */
    var plazaDefaultRangeKey4Value = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString("plazaDefaultRangeKey4Value_$userId", value).apply()
        }
        get() {
            return mySharedPreferences.getString("plazaDefaultRangeKey4Value_$userId", "") ?: ""
        }

    /**
     * 发布动态时，是否还提示保存图片到相册
     */
        var postNewShouldSavePhotoRequireTip = true
        set(value) {
            field = value
            mySharedPreferences.edit().putBoolean("postNewShouldSavePhotoRequireTip$userId", value).commit()
        }
        get() {
            return mySharedPreferences.getBoolean("postNewShouldSavePhotoRequireTip$userId", true)
        }

    /**
     * 发布动态时，是否保存图片到相册
     */
    var postNewShouldSavePhoto = false
        set(value) {
            field = value
            mySharedPreferences.edit().putBoolean("postNewShouldSavePhoto$userId", value).commit()
        }
        get() {
            return mySharedPreferences.getBoolean("postNewShouldSavePhoto$userId", false)
        }


    /**
     * 自定义表情版本
     */
    var memeVersion = 0L
        set(value) {
            field = value
            mySharedPreferences.edit().putLong("memeVersion_$userId", value).apply()
        }
        get() {
            return mySharedPreferences.getLong("memeVersion_$userId", 0L)
        }

    /**
     * 本地礼物缓存版本
     */
    var giftVersion = -1L
        set(value) {
            field = value
            mySharedPreferences.edit().putLong("giftVersion_$userId", value).apply()
        }
        get() {
            return mySharedPreferences.getLong("giftVersion_$userId", 0L)
        }

    /**
     * 本地谁信豆缓存
     */
    var envelopeCount = 0
        set(value) {
            field = value
            mySharedPreferences.edit().putInt("envelopeCount_$userId", value).apply()
        }
        get() {
            return mySharedPreferences.getInt("envelopeCount_$userId", 0)
        }

    /**
     * 本地谁信豆缓存
     */
    var envelopeCountStr = "0"
        set(value) {
            field = value
            mySharedPreferences.edit().putString("envelopeCountStr_$userId", value).apply()
        }
        get() {
            return mySharedPreferences.getString("envelopeCountStr_$userId", "0") ?: "0"
        }


    /**
     * 发送礼物是否不弹窗确认
     */
    var giftSendConfirmSwitch = true
        set(value) {
            field = value
            mySharedPreferences.edit().putBoolean("giftSendConfirmSwitch_$userId", value).apply()
        }
        get() {
            return mySharedPreferences.getBoolean("giftSendConfirmSwitch_$userId", true)
        }

    var agentType = when (agentLevel) {
        "1" -> {
            "agent1"
        }
        "2" -> {
            "agent2"
        }
        else -> {
            ""
        }
    }
    /**
     * 文字大小
     */
    var commonFontSize: Int = 1
        set(value) {
            field = value
            mySharedPreferences.edit().putInt("commonFontSize", value).apply()
        }
        get() {
            return mySharedPreferences.getInt("commonFontSize", 1)
        }
    /**
     * 投诉原因缓存
     */
    var complaintsReasonsJson: String = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString("complaintsReasonsJson", value).apply()
        }
        get() {
            return mySharedPreferences.getString("complaintsReasonsJson","")?:""
        }

    /**
     * 是否是第一次安装
     */
    var isFirstInstall: Boolean = true
        set(value) {
            field = value
            mySharedPreferences.edit().putBoolean("isFirstInstall", value).apply()
        }
        get() {
            return mySharedPreferences.getBoolean("isFirstInstall", true)
        }

    /**
     * openinstall邀请码(code)
     */
    var openinstallCode = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString("openinstallCode", value).apply()
        }
        get() {
            return mySharedPreferences.getString("openinstallCode", "") ?: ""
        }


    /**
     * openinstall来源 (source)
     */
    var openinstallSource = -1
        set(value) {
            field = value
            mySharedPreferences.edit().putInt("openinstallSource", value).apply()
        }
        get() {
            return mySharedPreferences.getInt("openinstallSource", -1)
        }

    /**
     * 是否可见钱包余额
     *
     * */
    var isLookMoney = true
        set(value) {
            field = value
            mySharedPreferences.edit().putBoolean("isLookMoney", value).apply()
        }
        get() {
            return mySharedPreferences.getBoolean("isLookMoney", true)
        }

    /**
     * 首页广告所有节点的版号之和
     */
    var homeAdVersionSum = 0
        set(value) {
            field = value
            mySharedPreferences.edit().putInt("homeAdVersionSum_$userId", value).apply()
        }
        get() {
            return mySharedPreferences.getInt("homeAdVersionSum_$userId", 0)
        }

    /**
     * 判断X5内核是否加载完成
     */
    var isX5coreInitComplete = false
        set(value) {
            field = value
            mySharedPreferences.edit().putBoolean("isX5coreInitComplete", value).apply()
        }
        get() {
            return mySharedPreferences.getBoolean("isX5coreInitComplete", false)
        }


    //用户设置铃声id
    var ringId: Int = 0
        set(value) {
            field = value
            mySharedPreferences.edit().putInt("ringId", value).apply()
        }
        get() {
            return mySharedPreferences.getInt("ringId", 0)
        }

    //用户设置铃声id
    var ringName: String = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString("ringName", value).apply()
        }
        get() {
            return mySharedPreferences.getString("ringName", "")?:""
        }

    //用户设置铃声id
    var ringMsgId: Int = 0
        set(value) {
            field = value
            mySharedPreferences.edit().putInt("ringMsgId", value).apply()
        }
        get() {
            return mySharedPreferences.getInt("ringMsgId", 0)
        }

    //用户设置铃声id
    var ringMsgName: String = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString("ringMsgName", value).apply()
        }
        get() {
            return mySharedPreferences.getString("ringMsgName", "")?:""
        }

    /**
     * 用户转账上次选择的付款银行卡信息id缓存
     */
    var accountTransferSelectPayCardId:String = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString(userId+"accountTransferSelectPayCardId", value).apply()
        }
        get() {
            return mySharedPreferences.getString(userId+"accountTransferSelectPayCardId", "")?:""
        }

    /**
     * 是否在消息迁移界面
     */
    var isOnMessageMovePage=false

    var isFirstEnterGreet: Boolean = true
        set(value) {
            field = value
            mySharedPreferences.edit().putBoolean("isFirstEnterGreet_$userId",value).apply()
        }
    get() {
        return mySharedPreferences.getBoolean("isFirstEnterGreet_$userId",true)?:true
    }
    var isFirstEnterPlazaDetail: Boolean = true
    set(value) {
        field = value
        mySharedPreferences.edit().putBoolean("isFirstEnterPlazaDetail_$userId",value).apply()
    }
    get() {
        return mySharedPreferences.getBoolean("isFirstEnterPlazaDetail_$userId",true)?:true
    }

    //完整手机号
    var mFullPhone: String = ""
        set(value) {
            field = value
            mySharedPreferences.edit().putString(UserInfoManager.KEY_FULL_PHONE_NO, value).apply()
        }
        get() {
            return mySharedPreferences.getString(UserInfoManager.KEY_FULL_PHONE_NO, "") ?: ""
        }
}
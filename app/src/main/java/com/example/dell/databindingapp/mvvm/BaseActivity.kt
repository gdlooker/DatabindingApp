package com.example.dell.databindingapp.mvvm

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.dell.databindingapp.R
import com.example.dell.databindingapp.databinding.CommontLoadingAndTipsBinding
import com.example.dell.databindingapp.databinding.CommontLoadingBinding
import com.example.dell.databindingapp.mvvm.ScreenAdaptUtil.setFullScreenBlack
import com.example.dell.databindingapp.ui.activity.MainActivity
import com.example.dell.databindingapp.util.JImageLoader
import com.example.dell.databindingapp.util.wakeUpScreen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * @author Michael
 * @date  2018/11/14 - 14:52.
 */
abstract class BaseActivity<T : ViewDataBinding> : ObserveActivity() {
    lateinit var binding: T
    private val loadingPop by lazy {
        BasePop<CommontLoadingBinding>(R.layout.commont_loading, setParameter = {
            gravity = Gravity.CENTER
        }).apply {
            //外部背景不变暗
            setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Common_Theme_Dialog_NO_Background_Dim)
        }
    }
    private val loadingTipsPop by lazy {
        BasePop<CommontLoadingAndTipsBinding>(R.layout.commont_loading_and_tips, setParameter = {
            gravity = Gravity.CENTER
        }).apply {
            //外部背景不变暗
            setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Common_Theme_Dialog_NO_Background_Dim)
        }
    }

    //注册登录弹窗
    private val commonRegisterLoginPop by lazy {

    }

    //群合成默认群头像初始化
    val jImageLoader by lazy {
        JImageLoader(this).apply {
            configDefaultPic(R.mipmap.ic_default_avatar_group)
        }
    }

    /**
     * @author WuShengjun
     * @time 2021/4/26 11:48
     * 是否固定竖屏
     */
    protected open fun requestOrientation() : Boolean {
        return true
    }

    /**
     * 设置适配
     */
    open fun initCustomDensity() {
        if(requestOrientation()) {
            //屏幕方向
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        //今日头条适配方案
        ScreenAdaptUtil.setCustomDensity(this, application)
    }

    /**
     * binding是否初始化
     */
    fun isBindingInit(): Boolean {
        return ::binding.isInitialized
    }

    @RequiresApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //异常关闭时重启App，如手动禁止了权限
        if(savedInstanceState != null) {
            restartApp(this)
            finish()
            return
        }

        initCustomDensity()
        setStatusMode()
        binding = DataBindingUtil.setContentView(this, initContentView())
        val viewModel = initViewModel()
                ?: ViewModelProvider.AndroidViewModelFactory(application).create(BaseViewModel::class.java)
        binding.setVariable(initVariableId(), viewModel)
        //初始化BaseViewModel数据
        if (viewModel is BaseViewModel) {
            initBaseDate(viewModel)
        }
        binding.lifecycleOwner = this
        initView()
        initData()
    }
    /**
     * 返回绑定的VariableId 默认viewMode 可以覆盖重写
     */
    open fun initVariableId(): Int {
        return 0
    }
    /**
     * 初始化基础数据
     */
    private fun initBaseDate(viewModel: BaseViewModel) {


        loadingPop.onDismissListener={
            loadingShowStatus(false)
            viewModel.loadingShowStatus(false)
        }
    }
    /**
     * 重启App
     */
    fun restartApp(context: Context) {
//    // 获取启动的intent
//    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
//    val restartIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
//    // 设置杀死应用后立马重启
//    val mgr = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
//    mgr.set(AlarmManager.RTC, SystemInfoUtil.currentRealTime, restartIntent)
//    // 重启应用
//    Process.killProcess(Process.myPid())
        val intent = Intent(context,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }



    /**
     * 锁屏显示
     */
    fun setShowOnLockedScreen(keepScreenOn: Boolean) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> {
                setShowWhenLocked(true) // API 27
                if(keepScreenOn) {
                    //保持常亮
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }
            keepScreenOn -> {
                //使Activity在锁屏界面上面显示且保持常亮
                window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            else -> {
                //使Activity在锁屏界面上面显示
                window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            }
        }
        //唤醒屏幕
        wakeUpScreen(this)
    }


    /**
     * loadingPop状态改变
     */
    open fun loadingShowStatus(isShow : Boolean) {

    }



    /**
     * 是否展示加载框
     */
    private fun showLoading(show: Boolean) {
        if (show) {
            if (!loadingPop.isShowing) {
                loadingPop.show(supportFragmentManager, "loadingPop")
            }
        } else {
            if (loadingPop.isShowing) {
                loadingPop.dismiss()
            }
        }
    }

    /**
     * 是否展示加载框
     */
    fun showLoadingOnUI() {
         showLoading(true)
    }

    /**
     * 是否展示加载框
     */
    fun hideLoadingOnUI() {
        showLoading(false)
    }

    /**
     * 是否展示加载框
     */
    private fun showLoadingTips(show: Boolean, tips: String = getString(R.string.on_loading)) {
        if (show) {
            if (!loadingTipsPop.isShowing) {
                loadingTipsPop.initBinding = {
                    loadingTips.text = tips
                }
                loadingTipsPop.show(supportFragmentManager, "loadingTipsPop")
            }
        } else {
            if (loadingTipsPop.isShowing) {
                loadingTipsPop.dismiss()
            }
        }
    }

    /**
     * 初始化Loading数据
     */
    fun initLoading(viewModels: List<BaseViewModel>) {

    }

    open fun initViewModel(): ViewModel? {
        return null
    }


    /**
     * 返回布局id
     */
    abstract fun initContentView(): Int


    /**
     * 设置状态栏字体颜色默认黑色
     */
    open fun setStatusMode() {
        setFullScreenBlack()
    }

    /**
     * 初始化view
     */
    abstract fun initView()

    /**
     * 初始化data
     */
    abstract fun initData()

    /**
     * 显示加载框
     */
    fun showLoading() {

    }
    /**
     * 显示加载框
     */
    fun showLoadingTips(tips : String = getString(R.string.on_loading)) {

    }

    /**
     * 关闭activity 并传个下一个
     */
    fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null && resultCode != Activity.RESULT_CANCELED) {
            setResult(resultCode, data)
            finish()
        }
    }

    /**
     * 跳转登录首页及登录页
     */
    fun toRegisterAndLoginPage() {
        //跳转登录首页及登录页
//        val firstPageIntent = Intent(this, get().koin.get<Class<*>>(named("LoginFirstPageActivity")))
//        val loginPageIntent = Intent(this, get().koin.get<Class<*>>(named("LoginRegisterActivity")))
//        startActivities(arrayOf(firstPageIntent, loginPageIntent))
       // startActivity(loginPageIntent)
    }

    /**
     * 跳转登录或注册
     * return 是否已登录
     */
    fun loginOrRegister(showDialog: Boolean) {

    }

    /**
     * 切换Fragment
     */
    fun replaceFragment(containerViewId: Int, fragment: Fragment, isAddToBackStack: Boolean = true, showAnim: Boolean = true) {

    }

    fun showToast(text: String, isCenter: Boolean = true) {

    }

    fun showToast(resId: Int, isCenter: Boolean = true) {

    }

    /**
     * 显示实名认证状态提示信息
     * -1-身份证过期； 0-需身份正反认证；1-需刷脸认证；2-需手持身份证认证；3-手持审核中；4-手持审核失败；5-认证成功；
     * goUnRealNamePage:是否跳实名认证界面
     */
    fun showRealNameStatusTips(status: Int, goUnRealNamePage: Boolean = false, pointStr: String? = null, function: ((Boolean) -> Unit)? = null) {

    }


    /**
     * 检查位置信息开启状态
     */
    fun checkLocationStatus(function: ((Boolean) -> Unit)? = null): Boolean {
       return false
    }

    /**
     * 提示用户处理当前阅后即焚开关
     */
    fun switchBurnMsgConfirm(burnSwitch:Boolean,function: ((Boolean) -> Unit)? = null) {

    }


}
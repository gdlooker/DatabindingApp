package com.example.dell.databindingapp.mvvm

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.dell.databindingapp.R
import com.example.dell.databindingapp.base.App
import com.example.dell.databindingapp.base.data.BaseData
import com.example.dell.databindingapp.databinding.CommontLoadingAndTipsBinding
import com.example.dell.databindingapp.databinding.CommontLoadingBinding
import com.example.dell.databindingapp.util.JImageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable.cancel


/**
 * @date  2018/12/10 - 16:42.
 */
abstract class BaseFragment<T : ViewDataBinding> : Fragment() {
    lateinit var binding: T
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, initContentView(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true
    }

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

    //应用上下文
    protected val appContext by lazy { App.instance().applicationContext }

    //是否第一次加载
    private var isFirst: Boolean = true

    //群合成默认群头像初始化
    val jImageLoader by lazy {
        JImageLoader(context).apply {
            configDefaultPic(R.mipmap.ic_default_avatar_group)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = initViewModel()
            ?: ViewModelProvider.AndroidViewModelFactory(App.instance()).create(BaseViewModel::class.java)
        //inding.setVariable(initVariableId(), viewModel)
        //初始化BaseViewModel数据
        if (viewModel is BaseViewModel) {
            initBaseDate(viewModel)
        }
        binding.lifecycleOwner = this
        initView()
        initData()
    }

    override fun onResume() {
        super.onResume()
        setResize()
        onVisible()
    }

    open fun setResize() {

    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            //等待view加载后触发懒加载
            view?.post {
                lazyLoadData()
                isFirst = false
            }
        }
    }

    /**
     * 初始化Loading数据
     */
    fun initLoading(vararg viewModels: BaseViewModel) {

    }

    /**
     * binding是否初始化
     */
    fun isBindingInit(): Boolean {
        return ::binding.isInitialized
    }

    /**
     * 初始化基础数据
     */
    @SuppressLint("FragmentLiveDataObserve")
    private fun initBaseDate(viewModel: BaseViewModel) {

    }

    /**
     * 是否展示加载框
     */
    private fun showLoading(show: Boolean) {
        if (show) {
            if (!loadingPop.isShowing) {
                loadingPop.show(childFragmentManager, "loadingPop")
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
                loadingTipsPop.show(childFragmentManager, "loadingTipsPop")
            }
        } else {
            if (loadingTipsPop.isShowing) {
                loadingTipsPop.dismiss()
            }
        }
    }

    /**
     * 显示加载框
     */
    fun showLoadingTips(tips: String = getString(R.string.on_loading)) {

    }

    /**
     * 隐藏加载框
     */
    fun hideLoadingTips() {

    }

    /**
     * 返回布局id
     */
    abstract fun initContentView(): Int

    /**
     * 返回ViewMode
     */
    open fun initViewModel(): ViewModel? {
        return null
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
     * 懒加载数据
     */
    open fun lazyLoadData() {

    }

    /**
     * 切换Fragment
     */
    fun replaceFragment(
        containerViewId: Int,
        fragment: Fragment,
        isAddToBackStack: Boolean = true,
        showAnim: Boolean = true
    ) {

    }

    /**
     * 跳转登录首页及登录页
     */
    fun toRegisterAndLoginPage() {

    }

}
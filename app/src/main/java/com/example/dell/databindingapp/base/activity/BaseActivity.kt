package com.example.dell.databindingapp


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Created by chentao
 * Date:2019/3/7
 * Description:
 */
open abstract class BaseActivity<T: ViewDataBinding>(): AppCompatActivity() {

    protected var bindingView:T?=null
    var baseView: View?=null

    // 内容布局
    protected var mContainer: RelativeLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseView=layoutInflater.inflate(R.layout.activity_base,null,false) //获取基类的布局
        setContentView(baseView)  //设置基类的布局
        bindingView = DataBindingUtil.inflate(layoutInflater, getLayoutId(), baseView as ViewGroup?, false)  //使用databinding获取子类布局

        var params: RelativeLayout.LayoutParams= RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        bindingView?.getRoot()?.layoutParams=params
        mContainer=baseView?.findViewById(R.id.container) //获取父类的RelativieLayout布局
        mContainer?.addView(bindingView?.getRoot())
        initData() //初始化数据
    }


    /**
     * 基类返回一个layout布局的int地址
     */
    abstract protected fun getLayoutId():Int

    abstract protected fun initData()
}
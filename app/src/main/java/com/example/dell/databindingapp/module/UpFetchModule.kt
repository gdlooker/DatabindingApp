package com.example.dell.databindingapp.module

import com.example.dell.databindingapp.base.adapter.BaseQuickAdapter
import com.example.dell.databindingapp.listener.OnUpFetchListener
import com.example.dell.databindingapp.listener.UpFetchListenerImp


/**
 * Author: WuShengjun
 * Date: 2021/2/25
 * Time: 16:40
 * Description: 向上加载
 */

/**
 * 需要【向上加载更多】功能的，[BaseQuickAdapter]继承此接口
 */
interface UpFetchModule

open class BaseUpFetchModule(private val baseQuickAdapter: BaseQuickAdapter<*, *>) :
    UpFetchListenerImp {

    private var mUpFetchListener: OnUpFetchListener? = null

    var isUpFetchEnable = false
    var isUpFetching = false
    /**
     * start up fetch position, default is 1.
     */
    var startUpFetchPosition = 1

    internal fun autoUpFetch(position: Int) {
        if (!isUpFetchEnable || isUpFetching) {
            return
        }
        if (position <= startUpFetchPosition) {
            mUpFetchListener?.onUpFetch()
        }
    }

    override fun setOnUpFetchListener(listener: OnUpFetchListener?) {
        this.mUpFetchListener = listener
    }
}

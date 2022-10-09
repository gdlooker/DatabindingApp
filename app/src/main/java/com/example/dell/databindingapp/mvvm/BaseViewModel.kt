package com.example.dell.databindingapp.mvvm

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.TextUtils
import android.util.Base64
import androidx.lifecycle.ViewModel

import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope.coroutineContext
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.Serializable



/**
 * 默认基础ViewModel
 */
open class BaseViewModel : Serializable, ViewModel() {
  

    /**
     * Creates a _supervisor_ job object in an active state.
     * Children of a supervisor job can fail independently of each other.
     *
     * A failure or cancellation of a child does not cause the supervisor job to fail and does not affect its other children,
     * so a supervisor can implement a custom policy for handling failures of its children:
     *
     * * A failure of a child job that was created using [launch][CoroutineScope.launch] can be handled via [CoroutineExceptionHandler] in the context.
     * * A failure of a child job that was created using [async][CoroutineScope.async] can be handled via [Deferred.await] on the resulting deferred value.
     *
     * If [parent] job is specified, then this supervisor job becomes a child job of its parent and is cancelled when its
     * parent fails or is cancelled. All this supervisor's children are cancelled in this case, too. The invocation of
     * [cancel][Job.cancel] with exception (other than [CancellationException]) on this supervisor job also cancels parent.
     *
     * @param parent an optional parent job.
     */
   

    //    Dispatchers.Main：Android中的主线程，可以直接操作UI
    //    Dispatchers.IO：针对磁盘和网络IO进行了优化，适合IO密集型的任务，比如：读写文件，操作数据库以及网络请求
    //    Dispatchers.Default：适合CPU密集型的任务，比如解析JSON文件，排序一个较大的list

    //协程作用域
    val scope = CoroutineScope(coroutineContext)


    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    /**
     * 内置封装好的可通知Activity/fragment 显示隐藏加载框 因为需要跟网络请求显示隐藏loading配套才加的，不然我加他个鸡儿加
     */
    inner class UiLoadingChange {

    }

    /**
     * loading状态改变
     */
    open fun loadingShowStatus(isShow: Boolean) {

    }

    fun showToast(text: String) {

    }

    fun showToast(resId: Int) {

    }

    val mainHandler by lazy { Handler(Looper.getMainLooper()) }

    //是否倒计时
    private var isCountDown = false
    private var mTotalTime: Long = 0

    /**
     * 开启倒计时
     */
    fun startTimerCountDown(millis: Long) {
        mTotalTime = millis
        isCountDown = true
        mainHandler.post(mTicker)
    }

    /**
     * 开启计时
     */
    open fun startTimerCountUp() {
        mTotalTime = 0
        isCountDown = false
        mainHandler.post(mTicker)
    }

    /**
     * 取消倒计时或计时
     */
    open fun cancelCountTimer() {
        mTotalTime = 0
        //重置为0
        mainHandler.removeCallbacksAndMessages(null)
    }

    /**
     * 取消倒计时或计时
     */
    open fun cancelCountNoTimer() {
        mTotalTime = 0
        //重置为0

        mainHandler.removeCallbacksAndMessages(null)
    }

    /**
     * 精确修正时间
     */
    private val mTicker by lazy {
        object : Runnable {
            override fun run() {
                val now = SystemClock.uptimeMillis()
                val next = now + (1000 - now % 1000)
                if (mTotalTime >= 0) {
                    mainHandler.postAtTime(this, next)
                    //是否倒计时

                }
                //是否倒计时
                if (isCountDown) {
                    mTotalTime -= 1000
                } else {
                    mTotalTime += 1000
                }
            }
        }
    }

    /**
     * 将图片转换成Base64编码的字符串
     */
    fun getBase64(path: String?): String? {
        if (TextUtils.isEmpty(path)) {
            return null
        }
        var inputStream: InputStream? = null
        var data: ByteArray? = null
        var result: String? = null
        try {
            inputStream = FileInputStream(path)
            //创建一个字符流大小的数组。
            data = ByteArray(inputStream.available())
            //写入数组
            inputStream.read(data)
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return result
    }


    /**
     * @author WuShengjun
     * @time 2021/5/27 17:14
     * 好友聊天置顶
    "userId":"mock",                //类型：String  必有字段  备注：用户id
    "friendId":"mock",                //类型：String  必有字段  备注：好友id
    "chatTop":1                //类型：Number  必有字段  备注：0不置顶 1置顶
     */
    private fun updateUserChatTop(
        chatTop: Int,
        friendId: String,
        showLoading: Boolean = true,
        onResult: ((success: Boolean, data: Any) -> Unit)? = null
    ) {

    }

    /**
     * @author WuShengjun
     * @time 2021/5/27 17:14
     * 群组聊天置顶
    "userId":"mock",                //类型：String  必有字段  备注：用户id
    "groupId":"mock",                //类型：String  必有字段  备注：群组id
    "chatTop":1                //类型：Number  必有字段  备注：0不置顶 1置顶
     */
    @JvmOverloads
    private fun updateGroupChatTop(
        chatTop: Int,
        groupId: String,
        showLoading: Boolean = true,
        onResult: ((success: Boolean, data: Any) -> Unit)? = null
    ) {

    }

}
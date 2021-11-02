package com.example.dell.databindingapp.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.media.MediaScannerConnection
import com.google.gson.reflect.TypeToken
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.telephony.PhoneNumberUtils
import android.text.*
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.transition.TransitionManager
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.databinding.OnRebindCallback
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.example.dell.databindingapp.base.App
import com.google.gson.Gson
import com.google.protobuf.ByteString
import kotlinx.coroutines.CoroutineScope
import top.zibin.luban.Luban
import java.io.*

import java.lang.String.format
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap
import kotlin.coroutines.CoroutineContext
import kotlin.math.round

/**
 * 获取应用程序名称
 */
@Synchronized
fun getAppName(context: Context): String? {
    try {
        val packageManager: PackageManager = context.packageManager
        val packageInfo: PackageInfo = packageManager.getPackageInfo(
                context.packageName, 0)
        val labelRes: Int = packageInfo.applicationInfo.labelRes
        return context.resources.getString(labelRes)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return null
}

/**
 * [获取应用程序版本名称信息]
 * @param context
 * @return 当前应用的版本名称
 */
@Synchronized
fun getPackageName(context: Context): String? {
    try {
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(
                context.packageName, 0)
        return packageInfo.packageName
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return null
}


/**
 * 状态栏高度
 */
val statusHeight: Int by lazy {
    App.instance().initStatusHeight()
}

/**
 * 添加UI变动动画
 */
fun ViewDataBinding.addOnRebindCallback() {
    addOnRebindCallback(object : OnRebindCallback<ViewDataBinding>() {
        override fun onPreBind(binding: ViewDataBinding): Boolean {
            TransitionManager.beginDelayedTransition(
                    binding.root as ViewGroup)
            return super.onPreBind(binding)
        }
    })
}

/**
 * 屏幕宽 高
 */
val screenPoint: Point by lazy {
    App.instance().getHeightWidth(false)
}

/**
 * 屏幕真实宽 高
 */
val screenRealPoint: Point by lazy {
    App.instance().getHeightWidth(true)
}

private fun Context.getHeightWidth(real: Boolean): Point {
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point = Point()
    val metrics = DisplayMetrics()
    if (real) {
        wm.defaultDisplay.getRealMetrics(metrics)
    } else {
        wm.defaultDisplay.getMetrics(metrics)
    }
    point.x = metrics.widthPixels
    point.y = metrics.heightPixels
    return point
}

/**
 * log日志
 */
fun Any.log(tag: String = "-----", block: () -> String) {

}


/**
 * 获取String
 */
fun Any.getMsgIdString(): String {
    return "getRandomMsgId()"
}


/**
 * ByteArray转换成 16进制小写字符串
 */
fun ByteArray.toBytes2Hex(): String {
    return HexUtils.bytes2Hex(this)
}

/**
 * 16进制字符串 转换为对应的 byte数组
 */
fun String.toHexString2Bytes(): ByteArray {
    return HexUtils.hex2Bytes(this)
}

/**
 * ByteString转换成 16进制小写字符串
 */
fun com.google.protobuf.ByteString.toBytes2Hex(): String {
    return HexUtils.bytes2Hex(this.toByteArray())
}

/**
 * string 先转16进制 再转ByteString的消息id
 */
val String.sMsgId: ByteString
    get() = ByteString.copyFrom(this.toHexString2Bytes())


/**
 * 组件销毁监听
 */
fun LifecycleOwner.onDestroy(callback: () -> Unit) {
    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() = callback()
    })
}



/**
 * 对象转Json
 */
fun Any.toJson(): String {
    return Gson().toJson(this)
}


/**
 * json转对象
 */
inline fun <reified T> String.toObject(): T {
    return Gson().fromJson<T>(this, T::class.java)
}

/**
 * json转List
 */
inline fun <reified T> String.toList(): List<T> {
    return Gson().fromJson(this, object : TypeToken<List<T>>() {}.type)
}

/**
 * 对象转map
 */
fun Any.toMap(): HashMap<String, String> {
    return Gson().fromJson(this.toJson(), HashMap<String, String>()::class.java)
}

/**
 * 将dip或dp值转换为px值，保证尺寸大小不变
 *
 */
fun View.dip2px(dipValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}

val Float.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics)


val Int.dp: Float
    get() {
        val scale: Float = App.instance().resources.displayMetrics.density
        return this * scale + 0.5f
    }

val Int.sp: Float
    get() {
        val scale: Float = App.instance().resources.displayMetrics.scaledDensity
        return this * scale + 0.5f
    }

fun Context.dip2px(dipValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}


/**
 * 获取状态栏高度
 */
fun Context.initStatusHeight(): Int {
    var resourceId =
            applicationContext.resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        applicationContext.resources.getDimensionPixelSize(resourceId)
    } else 0
}

/**
 * Context关闭Activity
 */
fun Context.finish() {
    if (this is Activity) {
        finish()
    }
}

/**
 * Context返回
 */
fun Context.onBackPressed() {
    if (this is Activity) {
        onBackPressed()
    }
}

/**
 * view跳转
 */
fun View.startActivity(intent: Intent, options: Bundle? = null) {
    if (context is Activity) {
        (context as Activity).startActivity(intent, options)
    }
}

/**
 * view跳转
 */
fun View.startActivityForResult(intent: Intent, requestCode: Int) {
    if (context is Activity) {
        (context as Activity).startActivityForResult(intent, requestCode)
    }
}

/**
 * view显示弹窗
 */
fun View.showToast(text: String) {
    text.showToast()
}

/**
 * String显示弹窗
 */
fun String.showToast(gravity: Int, xOff: Int = 0, yOff: Int = 0) {
   // ToastUtils.setGravity(gravity, xOff, yOff)
//    ToastUtils.showShort(this)
    this.innerShow()
}

/**
 * String显示弹窗
 */
fun String.showToast(isCenter: Boolean = true) {

   this.innerShow()
}

fun String.showRefreshToast(topOffsetY: Int){

}

private fun String.innerShow(){

}

fun String.desensitizedName(): String {
    val length = this.length
    return if (length <= 1) {
        this
    } else if (length == 2) {
        replaceFirst(substring(1), "*")
    } else {
        var temp=""
        for (index in 0 until this.length-2) {
            temp = "$temp*"
        }
        this.substring(0,1)+temp+this.substring(this.length-1,this.length)
    }
}


/**
 * view布局显示弹窗
 */
fun String.showCustomShort(isCenter: Boolean = true) {

}



/**
 * view关闭Activity
 */
fun View.finish() {
    if (context is Activity) {
        (context as Activity).finish()
    }
}

/**
 * view关闭Activity
 */
fun View.finishAfterTransition() {
    if (context is Activity) {
        (context as Activity).finishAfterTransition()
    }
}

/**
 * view关闭Activity
 */
fun View.setResult(int: Int, data: Intent) {
    if (context is Activity) {
        (context as Activity).setResult(int, data)
    }
}


/**
 * 显示键盘
 */
fun View.showKeyboard() {
    requestFocus()
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(this, 0)
}

/**
 * 隐藏键盘
 */
fun View.hideKeyboard(clearFocus: Boolean = true) {
    if (clearFocus) {
        clearFocus()
    }
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}




var simpleDateFormat: SimpleDateFormat? = null

/**
 * 毫秒数转指定字符格式的时间
 */
fun Long.toFormatTime(pattern: String): String {
    if (simpleDateFormat == null) {
        simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    }
    simpleDateFormat?.applyPattern(pattern)
    return simpleDateFormat?.format(Date(this)) ?: ""
}

/**
 * 毫秒数转计时时间，注意传入单位为：ms
 */
fun Long.toTimeString(): String {
    val oneHour = 1000 * 60 * 60
    val oneMin = 1000 * 60
    val oneSec = 1000
    val hour = this / oneHour
    val min = (this - hour * oneHour) / oneMin
    val sec = (this - hour * oneHour - min * oneMin) / oneSec
    return if (hour > 0) {
        format("%02d:%02d:%02d", hour, min, sec)
    } else {
        format("%02d:%02d", min, sec)
    }
}

/**
 * 秒数转计时时间，注意传入单位为：s
 */
fun Int.toTimeString(): String {
    val oneHour = 60 * 60
    val oneMin = 60
    val oneSec = 1
    val hour = this / oneHour
    val min = (this - hour * oneHour) / oneMin
    val sec = (this - hour * oneHour - min * oneMin) / oneSec
    return if (hour > 0) {
        format("%02d:%02d:%02d", hour, min, sec)
    } else {
        format("%02d:%02d", min, sec)
    }
}

/**
 * 毫秒数转计时时间，注意传入单位为：ms
 * 阅后即焚通知消息显示时间
 * 0,1ms(即刻焚烧)，10秒，5分钟，1小时，24小时
 */
fun Long.toBurnTimeString(): String {
    val oneHour = 1000 * 60 * 60
    val oneMin = 1000 * 60
    val oneSec = 1000
    val hour = this / oneHour
    val min = this / oneMin
    val sec = this / oneSec
    when {
        hour > 0 -> {
            return format("%d小时", hour)
        }
        min > 0 -> {
            return format("%d分钟", min)
        }
        sec > 0 -> {
            return format("%d秒", sec)
        }
        this > 0 -> {
            return if (this == 1L) {
                "即刻焚烧"
            } else {
                format("%d毫秒", this)
            }
        }
        else -> {
            return "非阅后即焚消息"
        }
    }

}


/**
 * Bitmap转byte数组
 */
fun Bitmap.toBytes(format: Bitmap.CompressFormat): ByteArray {
    var baos = ByteArrayOutputStream()
    this.compress(format, 100, baos)
    return baos.toByteArray()
}

/**
 * 保存bitmap到指定路径
 * @param bitmap Bitmap
 */
fun Bitmap.toSave(path: String, format: Bitmap.CompressFormat): String? {
    val imgName = String.format("Image_%d.jpg", System.currentTimeMillis())
    val filePic: File
    if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
        Log.e("saveBitmap", "saveBitmap failure : sdcard not mounted")
        return null
    }
    try {
        filePic = File(path, imgName)
        if (!filePic.exists()) {
            filePic.parentFile.mkdirs()
            filePic.createNewFile()
        }
        val fos = FileOutputStream(filePic)
        this.compress(format, 100, fos)
        fos.flush()
        fos.close()
        MediaScannerConnection.scanFile(App.instance(), arrayOf(filePic.absolutePath), null, null)
    } catch (e: IOException) {
        Log.e("saveBitmap", "saveBitmap: " + e.message)
        return null
    }
    return filePic.absolutePath
}

/**
 * 判断是否是手机号码
 */
fun String.isMobilePhoneNumber(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    if (this.length == 11) {
        for (i in 0..10) {
            if (!PhoneNumberUtils.isISODigit(this[i])) {
                return false
            }
        }
//        val p = Pattern.compile(
//                "^((13[^4,\\D])" +
//                        "|(134[^9,\\D])" +
//                        "|(14[5,7])" +
//                        "|(15[^4,\\D])" +
//                        "|(17[3,6-8])" +
//                        "|(18[0-9]))\\d{8}$")
        val p = Pattern.compile("^((13[0-9])|(14[5,9])|(15[0-3,5-9])|(16[5-6])|(17[0-8])|(18[0-9])|198|199)\\d{8}$")
        val m = p.matcher(this)
        return m.matches()
    }
    return false
}

/**
 * 根据url获取设置圆形图片
 */
fun ImageView.setCircleImage(url: String?, defPicResId: Int) {

}

/**
 * 根据url获取设置圆形图片
 */
fun ImageView.setCircleImage(url: String?) {

}

/**
 * 根据url设置图片
 */
fun ImageView.setImageRound(url: String?, round: Int = 0, defResId: Int = 0) {

}

/**
 * 根据uri设置图片
 */
fun ImageView.setImageRound(uri: Uri?, round: Int = 0, defResId: Int = 0) {

}

//创建临时文件
fun newTempFile(prefix: String = "tmp", suffix: String? = null, directory: File? = null): File {
    return createTempFile(prefix, suffix, directory)
}

/**
 * 鲁班压缩
 */
fun luBanToPath(path: String): String {
    return try {
        //鲁班压缩
        return Luban.with(App.instance())
            .setTargetDir(App.Companion.instance().filesDir.absolutePath)
            .load(path).get().firstOrNull()?.absolutePath ?: path
    } catch (e: Exception) {
        path
    }
}
// 复制文件
fun copyFile(input: InputStream?, output: OutputStream) {
    var inBuff: BufferedInputStream? = null
    var outBuff: BufferedOutputStream? = null
    try {
        // 新建文件输入流并对它进行缓冲
        inBuff = BufferedInputStream(input)

        // 新建文件输出流并对它进行缓冲
        outBuff = BufferedOutputStream(output)

        // 缓冲数组
        val b = ByteArray(1024 * 5)
        var len: Int
        while (inBuff.read(b).also {
                len = it
            } != -1) {
            outBuff.write(b, 0, len)
        }
        // 刷新此缓冲的输出流
        outBuff.flush()
    } catch (e: java.lang.Exception) {
    } finally {
        //关闭流
        inBuff?.close()
        outBuff?.close()
        output.close()
        input?.close()
    }
}
/**
 * 获取图片文件宽高
 *
 */
fun String.getFilePathWH(): Pair<Int, Int> {
    return try {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        if (this.isNotEmpty()) {
            BitmapFactory.decodeFile(this, options)
            when (ImageUtil.getImageOrientation(this)) {
                ExifInterface.ORIENTATION_ROTATE_90, ExifInterface.ORIENTATION_ROTATE_270 -> {
                    Pair(options.outHeight, options.outWidth)
                }
                else -> {
                    Pair(options.outWidth, options.outHeight)
                }
            }
        } else {
            Pair(1, 1)
        }
    } catch (e: java.lang.Exception) {
        Pair(1, 1)
    }

}

/**
 * 根据媒体资源路径去适配View的极限宽高
 */
fun View.adaptViewMaxWH(url: String?, isFilePath: Boolean = true, isVideo: Boolean = false, maxWith: Float = 160.dp,
                        maxHeight: Float = 160.dp
) {
    LogUtil.e("Felix", "---isFilePath:${isFilePath} isVideo:${isVideo} File:${File(url ?: "").exists()}, url:${url}")
    //获取宽高比
    val ratio = when {
        url == null -> {
            1F
        }
        isFilePath -> {
            if (!File(url).exists()) {
                1F
            } else if (isVideo) {
                0F
            } else {
                0F
            }
        }
        else -> {
            0F
        }
    }
    val layoutParams = this.layoutParams
//    when {
//        ratio >= 1 -> {
//            //最小宽高
//            layoutParams.width = maxWith.toInt()
//            layoutParams.height = (maxWith / ratio).toInt()
//        }
//        ratio < 1 -> {
//            //最大宽高
//            layoutParams.width = (maxHeight * ratio).toInt()
//            layoutParams.height = maxHeight.toInt()
//        }
//    }

    if (ratio < 1.0f / 3){
        val width = maxHeight * (1.0f / 3)
        layoutParams.width = width.toInt()
        layoutParams.height = maxHeight.toInt()
    }else if (ratio > (1.0f / 3) && ratio < 1){
        val width = maxHeight * ratio
        layoutParams.width = width.toInt()
        layoutParams.height = maxHeight.toInt()
    }else {
        val height = maxWith / ratio
        layoutParams.width = maxWith.toInt()
        layoutParams.height = height.toInt()
    }

    this.layoutParams = layoutParams
}

/**
 * 根据媒体资源路径去适配View的极限宽高
 */
fun View.adaptViewMinWH(
        with: Float,
        height: Float,
        minWith: Float = 268.dp,
        minHeight: Float = 460.dp, topOffset: Int = 169.dp.toInt(), round: Int = 115.dp.toInt()
): Pair<Int, Int> {
    var ratio = with * 1f / height

    val layoutParams = this.layoutParams
    when {
        ratio >= 1 -> {
            //宽大于高，以高为基础
            layoutParams.height = minHeight.toInt()
            layoutParams.width = (minHeight * ratio).toInt()
        }
        ratio < 1 -> {
            //宽小于高，以宽为基础
            layoutParams.width = minWith.toInt()
            layoutParams.height = (minWith / ratio).toInt()
        }
    }
    val topMargin = topOffset - (layoutParams.height - round * 2) / 2
    (layoutParams as ConstraintLayout.LayoutParams).topMargin = topMargin
//    Log.e("Felix","[adaptViewMinWH].width: ${layoutParams.width} .height: ${layoutParams.height} ratio:${ratio} topOffset:${topOffset} round:${round} " +
//            "topMargin:${topMargin}")
    this.layoutParams = layoutParams

    return Pair(layoutParams.width, layoutParams.height)
}







/**
 * 隐藏软键盘
 */
fun Activity?.hideSoftKeyboard() {
    this?.let { act ->
        val view = act.currentFocus
        view?.let {
            val inputMethodManager =
                    act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}

fun String?.copy() { 
    this?.apply {
        val manager: ClipboardManager = App.Companion.instance().baseContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(null, this)
        manager.setPrimaryClip(clipData)
    }
}

/**
 * 评论数显示的字符串
 */
fun Long.likeCommentCountStrNoChar(): String {
    if (this < 10000) {
        return this.toString()
    }
    val count = (this + 500) / 1000
    return (count * 1.0 / 10).toString()
}

/**
 * 评论数显示的字符串
 */
fun Long.letterCountShowNoChar(): String {
    if (this < 10000) {
        return this.likeCommentCountStrNoChar()
    }
    return "${this.likeCommentCountStrNoChar()}w"
}

/***
 * 判断是否是鸿蒙系统
 * @return Boolean
 */
fun isHarmonyOS(): Boolean{
    try {
        val clz = Class.forName("com.huawei.system.BuildEx")
        val method = clz.getMethod("getOsBrand")
        return "harmony" == method.invoke(clz)
    } catch (e: ClassNotFoundException) {
        LogUtil.d("occured ClassNotFoundException")
    } catch (e: Exception) {
        LogUtil.e( "occur other problem")
    }
    return false
}

fun String?.isDebitCard(): Boolean{
    return TextUtils.equals("DEBIT_CARD", this)
}

fun String?.isCreditCard(): Boolean{
    return TextUtils.equals("CREDIT_CARD", this)
}

fun String.callPhone(context: Context) {
    val intent = Intent(Intent.ACTION_DIAL)
    val data = Uri.parse("tel:${this}")
    intent.data = data
    context.startActivity(intent)
}


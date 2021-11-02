package com.example.dell.databindingapp.widget;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.dell.databindingapp.R
import com.example.dell.databindingapp.util.dip2px
import com.example.dell.databindingapp.util.dp
import com.example.dell.databindingapp.util.sp
import kotlin.math.min


/**
 * 密码输入
 */
@RequiresApi(Build.VERSION_CODES.CUPCAKE)
class PwdEditText @JvmOverloads constructor(
        context: Context, @Nullable attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val mTextPaint = Paint()

    //每个条目的宽
    private var itemWidth = 0f
    private var itemHeight = 0f

    //几位密码
    private var pwdLength = 6
    //是否显示密文
    private var isCiphertext = true

    //当前输入密码
    private var pwdText = ""
    // 输入完成时回调
    var inputFunction: ((completed: Boolean, pwd: String) -> Unit)? =null

    var frameGap = 0

    private val defaultFrameColor = Color.parseColor("#ffdddddd")
    private var pwdFrameColor = defaultFrameColor

    init {

        initAttrs(context,attrs)

        //可点击
        isClickable = true
        //触摸模式下此视图是否可以接收焦点
        isFocusableInTouchMode = true
        //可聚焦
        isFocusable = true
        //隐藏光标
        isCursorVisible = false
        //设置最小字体
        textSize=0f

        paint.isAntiAlias = true//消除锯齿
        paint.strokeWidth = dip2px(1f).toFloat()//设置画笔的宽度
        paint.style = Paint.Style.STROKE
        paint.color = pwdFrameColor
        paint.textSize = dip2px(1f).toFloat()

        mTextPaint.isAntiAlias = true// 抗锯齿效果
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.color = ContextCompat.getColor(context, R.color.common_black_33)

        postDelayed(Runnable {
            showKeyboard()
        }, 100)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PwdEditText, 0, 0)
            frameGap = typedArray.getDimensionPixelSize(R.styleable.PwdEditText_frameGap,3.dp.toInt())
            pwdLength = typedArray.getInt(R.styleable.PwdEditText_pwdLength,6)
            isCiphertext = typedArray.getBoolean(R.styleable.PwdEditText_isCiphertext,true)
            pwdFrameColor = typedArray.getColor(R.styleable.PwdEditText_pwdFrameColor,defaultFrameColor)
            typedArray.recycle()
        }
    }

    /**
     * 显示键盘
     */
    fun showKeyboard() {
        requestFocus()
        val inputManager =
                context.getSystemService(
                        Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
        inputManager.showSoftInput(this, 0)
    }

    /**
     * 隐藏键盘
     */
    fun hideKeyboard() {
        clearFocus()
        val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)

        if (text.length <= pwdLength) {
            pwdText = text.toString()
        } else {
            setText(pwdText)
            setSelection(pwdText.length)  //光标制动到最后
        }
        //输入状态变化
        inputFunction?.invoke(text.length == pwdLength, pwdText)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredSize(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * 设置控件尺寸
     */
    private fun setMeasuredSize(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        itemWidth = (measuredWidth.toFloat() - frameGap * (pwdLength-1)) / pwdLength
        itemHeight = if(measuredHeight.toFloat() < itemWidth) measuredHeight.toFloat() else itemWidth

        val desiredWidth = (itemWidth + 1.dp).toInt()
        val desiredHeight = (itemWidth + 1.dp).toInt()

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        //Measure Width
        val width = if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            widthSize
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            min(desiredWidth, widthSize)
        } else {
            //Be whatever you want
            desiredWidth
        }

        //Measure Height
        val height = if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            min(desiredHeight, heightSize)
        } else {
            //Be whatever you want
            desiredHeight
        }
        //MUST CALL THIS
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.style = Paint.Style.STROKE
        //绘制边框
        if (frameGap > 0){
            for (i in 0 until pwdLength) {
                var left = (itemWidth + frameGap) * i
                var right = left + itemWidth
                canvas.drawRect(left, 0f, right, itemHeight, paint)
            }
        }else{
            //绘制
            val halfStrokeWidth = paint.strokeWidth/2
            canvas.drawRect(halfStrokeWidth, halfStrokeWidth, itemWidth *pwdLength - halfStrokeWidth, itemHeight - halfStrokeWidth, paint)
            for (i in 0 until pwdLength) {
                var left = (itemWidth + frameGap) * i
                var right = left + itemWidth
                canvas.drawLine(right, 0f, right, itemHeight, paint)
            }
        }
        //绘制密码
        if(pwdText.isEmpty()) {
            for (i in 0 until pwdLength) {
                var left = (itemWidth + frameGap) * i
                var right = left + itemWidth
                canvas.drawText("",
                        (left + right) / 2,
                        itemHeight / 2,
                        mTextPaint)
            }
        } else {
            //绘制密码
            for (i in pwdText.indices) {
                var left = (itemWidth + frameGap) * i
                var right = left + itemWidth
                if (isCiphertext){
                    canvas.drawCircle((left + right) / 2, itemHeight / 2, dip2px(5f).toFloat(), mTextPaint)
                }else{
                    mTextPaint.textSize = 20.sp
                    val fontMetrics = mTextPaint.fontMetrics
                    val textHeight = fontMetrics.descent - fontMetrics.ascent
                    val textBaseline = itemHeight / 2 + (textHeight / 2 - fontMetrics.descent)
                    canvas.drawText(pwdText.subSequence(i,i+1).toString(),(left + right) / 2,textBaseline,mTextPaint)
                }
            }
        }
    }
}
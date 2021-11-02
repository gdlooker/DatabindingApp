package com.example.dell.databindingapp.widget;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import com.example.dell.databindingapp.R
import com.example.dell.databindingapp.util.dip2px


/**
 * 密码输入
 */
@RequiresApi(Build.VERSION_CODES.CUPCAKE)
class VerifyCodeEditText @JvmOverloads constructor(
        context: Context, @Nullable attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val mTextPaint = Paint()

    //几位验证码
    private val codeLength = 6

    //下划线间隔宽度
    private var deliverWidth = 6

    //验证码离下划线距离
    private var textPaddingBottom = 12

    //当前输入验证码
    private var codeText = ""

    //设置密码显示样式
    private var passwordStyle = false

    // 输入完成时回调
    var inputFunction: ((isFull: Boolean, code: String) -> Unit)? = null

    init {
        initAttribute(attrs, defStyleAttr)
        //可点击
        isClickable = true
        //触摸模式下此视图是否可以接收焦点
        isFocusableInTouchMode = true
        //可聚焦
        isFocusable = true
        //隐藏光标
        isCursorVisible = false
        //设置最小字体
        textSize = 0f
        //只允许输入数字
        inputType = InputType.TYPE_CLASS_NUMBER
        //限制输入长度
        filters = arrayOf(InputFilter.LengthFilter(codeLength))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            background = null
        }

        deliverWidth = dip2px(6f)
        textPaddingBottom = dip2px(8f)

        paint.isAntiAlias = true//消除锯齿
        paint.strokeWidth = dip2px(1f).toFloat()//设置画笔的宽度
        paint.style = Paint.Style.FILL
//        paint.color = Color.parseColor("#ff3d3d3d")

        mTextPaint.isAntiAlias = true// 抗锯齿效果
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = dip2px(21f).toFloat()
//        mTextPaint.color = Color.parseColor("#ff3d3d3d")

        postDelayed(Runnable {
            showKeyboard()
        }, 100)
    }

    private fun initAttribute(attrs: AttributeSet?, defStyleAttr: Int) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.VerifyCodeEditText, defStyleAttr, 0)
        val color = typeArray.getColor(R.styleable.VerifyCodeEditText_codeLineColor,Color.parseColor("#ff3d3d3d"))
        val textColor = typeArray.getColor(R.styleable.VerifyCodeEditText_codeTextColor,Color.parseColor("#ff3d3d3d"))
        typeArray.recycle()
        paint.color = color
        mTextPaint.color = textColor
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

        if (text.length <= codeLength) {
            codeText = text.toString()
        } else {
            setCodeText(codeText)
            setSelection(codeText.length)  //光标制动到最后
        }
        //输入状态变化
        inputFunction?.invoke(text.length == codeLength, codeText)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.style = Paint.Style.STROKE

        //下划线宽度
        var lineWidth = (width - codeLength * deliverWidth) / codeLength
        //绘制下划线
        for (index in 0 until codeLength) {
            canvas.drawLine(index * (lineWidth + deliverWidth).toFloat(),
                    height.toFloat(),
                    index * (lineWidth + deliverWidth) + lineWidth.toFloat(),
                    height.toFloat(),
                    paint)
        }
        //绘制验证码
        if(codeText.isEmpty()) {
            for (i in 0 until codeLength) {
                canvas.drawText("",
                        i * (lineWidth + deliverWidth).toFloat() + lineWidth / 2,
                        height.toFloat() - textPaddingBottom,
                        mTextPaint)
            }
        } else {
            for (i in codeText.indices) {
                canvas.drawText(if (passwordStyle) "*" else codeText[i].toString(),
                        i * (lineWidth + deliverWidth).toFloat() + lineWidth / 2,
                        height.toFloat() - textPaddingBottom,
                        mTextPaint)
            }
        }
    }

//    override fun setText(text: CharSequence) {
//        super.setText(text)
//        codeText = text.toString()
//    }

    fun setCodeText(text: String) {
        codeText = text
        setText(text)
    }

    /**
     * 是否设置密码显示样式
     */
    fun setPasswordStyle(passwordStyle: Boolean) {
        this.passwordStyle = passwordStyle
    }
}
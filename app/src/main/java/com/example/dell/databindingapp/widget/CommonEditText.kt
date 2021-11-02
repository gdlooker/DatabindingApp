package com.example.dell.databindingapp.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatEditText
import com.example.dell.databindingapp.R


/**
 * Created by WuShengjun on 2018-10-24.
 * Description: 通用EditText输入框
 */
class CommonEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle) : AppCompatEditText(context, attrs, defStyleAttr) {
    private val TOP_LEFT = 1
    private val TOP_RIGHT = 2
    private val BOTTOM_RIGHT = 4
    private val BOTTOM_LEFT = 8
    /**
     * 填充颜色
     */
    private var mNormalFillColor = Color.TRANSPARENT
    /**
     * 焦点填充颜色
     */
    private var mFocusedFillColor = Color.TRANSPARENT
    /**
     * 不可编辑填充颜色
     */
    private var mNotEnabledFillColor = Color.TRANSPARENT
    /**
     * 描边颜色
     */
    private var mStrokeColor = 0xbbbbbb
    /**
     * 描边焦点状态颜色
     */
    private var mStrokeFocusedColor = 0x000000
    /**
     * 描边不可编辑状态颜色
     */
    private var mStrokeNotEnabledColor = 0xe0e0e0
    /**
     * 描边宽度
     */
    private var mStrokeWidth = 1
    /**
     * 圆角半径
     */
    private var mCornerRadius = 0
    /**
     * 圆角位置
     */
    private var mCornerPosition = -1
    /**
     * 起始颜色
     */
    private var mStartColor = -0x1
    /**
     * 结束颜色
     */
    private var mEndColor = -0x1
    /**
     * 渐变方向
     * 0-GradientDrawable.Orientation.TOP_BOTTOM
     * 1-GradientDrawable.Orientation.LEFT_RIGHT
     */
    private var mOrientation = 0
    /**
     * 普通shape样式
     */
    private val normalGradientDrawable = GradientDrawable()
    /**
     * 焦点shape样式
     */
    private val focusGradientDrawable = GradientDrawable()
    /**
     * 不可编辑shape样式
     */
    private val notEnabledGradientDrawable = GradientDrawable()
    /**
     * shape样式集合
     */
    private val stateListDrawable = StateListDrawable()

    private fun inits(context: Context) {
        mNormalFillColor = Color.TRANSPARENT
        mFocusedFillColor = Color.TRANSPARENT
        mNotEnabledFillColor = Color.TRANSPARENT
        mStrokeColor = Color.parseColor("#bbbbbb")
        mStrokeFocusedColor = Color.parseColor("#bbbbbb")
        mStrokeNotEnabledColor = Color.parseColor("#e0e0e0")
        mStrokeWidth = dp2px(1f)
        mCornerRadius = 0
        mCornerPosition = -1
        mStartColor = -0x1
        mEndColor = -0x1
        setTextColor(Color.parseColor("#393939"))
        setHintTextColor(Color.parseColor("#aaaaaa"))
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonEditText)
        mNormalFillColor = typedArray.getColor(R.styleable.CommonEditText_commonET_normalFillColor, mNormalFillColor)
        mFocusedFillColor = typedArray.getColor(R.styleable.CommonEditText_commonET_focusedFillColor, mNormalFillColor)
        mNotEnabledFillColor = typedArray.getColor(R.styleable.CommonEditText_commonET_notEnabledFillColor, mNormalFillColor)
        mStrokeColor = if(typedArray.hasValue(R.styleable.CommonEditText_commonET_normalFillColor)) {
            typedArray.getColor(R.styleable.CommonEditText_commonET_strokeColor, mNormalFillColor)
        } else {
            typedArray.getColor(R.styleable.CommonEditText_commonET_strokeColor, mStrokeColor)
        }
        mStrokeFocusedColor = typedArray.getColor(R.styleable.CommonEditText_commonET_strokeFocusedColor, mStrokeColor)
        mStrokeNotEnabledColor = typedArray.getColor(R.styleable.CommonEditText_commonET_strokeNotEnabledColor, mStrokeColor)
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.CommonEditText_commonET_strokeWidth, mStrokeWidth)
        mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.CommonEditText_commonET_cornerRadius, mCornerRadius)
        mCornerPosition = typedArray.getInt(R.styleable.CommonEditText_commonET_cornerPosition, mCornerPosition)
        mStartColor = typedArray.getColor(R.styleable.CommonEditText_commonET_startColor, mStartColor)
        mEndColor = typedArray.getColor(R.styleable.CommonEditText_commonET_endColor, mEndColor)
        mOrientation = typedArray.getColor(R.styleable.CommonEditText_commonET_orientation, mOrientation)
        typedArray.recycle()
    }

    private fun initBackground() { // 渐变色
        if (mStartColor != -0x1 && mEndColor != -0x1) {
            normalGradientDrawable.colors = intArrayOf(mStartColor, mEndColor)
            if (mOrientation == 0) {
                normalGradientDrawable.orientation = GradientDrawable.Orientation.TOP_BOTTOM
            } else if (mOrientation == 1) {
                normalGradientDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
            }
        } else { // 初始化normal状态填充色
            normalGradientDrawable.setColor(mNormalFillColor)
        }
        // 初始化颜色及状态
        notEnabledGradientDrawable.setColor(mNotEnabledFillColor)
        focusGradientDrawable.setColor(mFocusedFillColor)
        normalGradientDrawable.shape = GradientDrawable.RECTANGLE
        focusGradientDrawable.shape = GradientDrawable.RECTANGLE
        notEnabledGradientDrawable.shape = GradientDrawable.RECTANGLE
        // 统一设置圆角半径
        if (mCornerPosition == -1) {
            val radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mCornerRadius.toFloat(), resources.displayMetrics)
            normalGradientDrawable.cornerRadius = radius
            focusGradientDrawable.cornerRadius = radius
            notEnabledGradientDrawable.cornerRadius = radius
        } else { // 根据圆角位置设置圆角半径
            val radii = cornerRadiusByPosition
            normalGradientDrawable.cornerRadii = radii
            focusGradientDrawable.cornerRadii = radii
            notEnabledGradientDrawable.cornerRadii = radii
        }
        // 默认的透明边框不绘制,否则会导致没有阴影
        if (mStrokeColor != 0) {
            normalGradientDrawable.setStroke(mStrokeWidth, mStrokeColor)
            focusGradientDrawable.setStroke(mStrokeWidth, mStrokeFocusedColor)
            notEnabledGradientDrawable.setStroke(mStrokeWidth, mStrokeNotEnabledColor)
        }
        // 注意此处的add顺序，normal必须在最后一个，否则其他状态无效
        if (mFocusedFillColor != mNormalFillColor || mStrokeFocusedColor != mStrokeColor) { // 设置focused状态
            stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), focusGradientDrawable)
            // 设置normal状态
            stateListDrawable.addState(intArrayOf(), normalGradientDrawable)
        } else { // 设置enabled状态
            stateListDrawable.addState(intArrayOf(android.R.attr.state_enabled), normalGradientDrawable)
            // 设置normal状态
            stateListDrawable.addState(intArrayOf(), notEnabledGradientDrawable)
        }
        //        setBackground(normalGradientDrawable);
        background = stateListDrawable
    }

    /**
     * 根据圆角位置获取圆角半径
     */
    private val cornerRadiusByPosition: FloatArray
        private get() {
            val result = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
            if (containsFlag(mCornerPosition, TOP_LEFT)) {
                result[0] = mCornerRadius.toFloat()
                result[1] = mCornerRadius.toFloat()
            }
            if (containsFlag(mCornerPosition, TOP_RIGHT)) {
                result[2] = mCornerRadius.toFloat()
                result[3] = mCornerRadius.toFloat()
            }
            if (containsFlag(mCornerPosition, BOTTOM_RIGHT)) {
                result[4] = mCornerRadius.toFloat()
                result[5] = mCornerRadius.toFloat()
            }
            if (containsFlag(mCornerPosition, BOTTOM_LEFT)) {
                result[6] = mCornerRadius.toFloat()
                result[7] = mCornerRadius.toFloat()
            }
            return result
        }

    /**
     * 是否包含对应flag
     * 按位或
     */
    private fun containsFlag(flagSet: Int, flag: Int): Boolean {
        return flagSet or flag == flagSet
    }

    /**
     * dp转px
     * @param dpVal
     * @return
     */
    private fun dp2px(dpVal: Float): Int {
        val scale = resources.displayMetrics.density
        return (scale * dpVal + 0.5f).toInt()
    }

    init {
        inits(context)
        initAttrs(context, attrs)
        initBackground()
    }
}
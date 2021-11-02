package com.example.dell.databindingapp.widget;

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.example.dell.databindingapp.R


/**
 * Created by WuShengjun on 2018-10-24.
 * Description: 矩形或圆角背景LinearLayout
 */
class ShapeTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.textViewStyle) : AppCompatTextView(context, attrs, defStyleAttr) {
    private val TOP_LEFT = 1
    private val TOP_RIGHT = 2
    private val BOTTOM_RIGHT = 4
    private val BOTTOM_LEFT = 8

    /**
     * 填充颜色
     */
    private var mFillColor = Color.TRANSPARENT
    var fillColor: Int
        get() = mFillColor
        set(value) {
            mFillColor = value
            initBackground()
        }

    /**
     * 点击填充颜色
     */
    private var mPressedFillColor = Color.TRANSPARENT
    var pressedFillColor: Int
        get() = mPressedFillColor
        set(value) {
            mPressedFillColor = value
            initBackground()
        }

    /**
     * 选中填充颜色
     */
    private var mSelectedFillColor = Color.TRANSPARENT

    /**
     * 描边颜色
     */
    private var mStrokeColor = Color.TRANSPARENT
    var strokeColor: Int
        get() = mStrokeColor
        set(value) {
            mStrokeColor = value
            initBackground()
        }

    /**
     * 点击描边颜色
     */
    private var mPressedStrokeColor = Color.TRANSPARENT
    var pressedStrokeColor: Int
        get() = mPressedStrokeColor
        set(value) {
            mPressedStrokeColor = value
            initBackground()
        }

    /**
     * 选中描边颜色
     */
    private var mSelectedStrokeColor = Color.TRANSPARENT
    var selectedStrokeColor: Int
        get() = mSelectedStrokeColor
        set(value) {
            mSelectedStrokeColor = value
            initBackground()
        }

    /**
     * 描边宽度
     */
    private var mStrokeWidth = 0

    /**
     * 点击描边宽度
     */
    private var mPressedStrokeWidth = 0

    /**
     * 选中描边宽度
     */
    private var mSelectedStrokeWidth = 0

    /**
     * 圆角半径
     */
    private var mCornerRadius = 0
    var cornerRadius: Int
        get() = mCornerRadius
        set(value) {
            mCornerRadius = value
            initBackground()
        }

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
     * 点击shape样式
     */
    private val pressedGradientDrawable = GradientDrawable()

    /**
     * 选中shape样式
     */
    private val selectedGradientDrawable = GradientDrawable()
    private val mStateListDrawable = StateListDrawable()
    private fun inits(context: Context) {
        mFillColor = Color.TRANSPARENT
        mPressedFillColor = Color.TRANSPARENT
        mSelectedFillColor = Color.TRANSPARENT
        mStrokeColor = Color.TRANSPARENT
        mPressedStrokeColor = Color.TRANSPARENT
        mSelectedStrokeColor = Color.TRANSPARENT
        mStrokeWidth = 0
        mPressedStrokeWidth = 0
        mSelectedStrokeWidth = 0
        mCornerRadius = 0
        mCornerPosition = -1
        mStartColor = -0x1
        mEndColor = -0x1
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Common_ShapeTextView)
        mFillColor = typedArray.getColor(R.styleable.Common_ShapeTextView_common_stv_fillColor, mFillColor)
        mPressedFillColor = typedArray.getColor(R.styleable.Common_ShapeTextView_common_stv_pressedFillColor, mFillColor) // 默认是普通状态的填充颜色
        mSelectedFillColor = typedArray.getColor(R.styleable.Common_ShapeTextView_common_stv_selectedFillColor, mFillColor) // 默认是普通状态的填充颜色
        mStrokeColor = typedArray.getColor(R.styleable.Common_ShapeTextView_common_stv_strokeColor, mStrokeColor)
        mPressedStrokeColor = typedArray.getColor(R.styleable.Common_ShapeTextView_common_stv_pressedStrokeColor, mStrokeColor) // 默认是普通状态的描边颜色
        mSelectedStrokeColor = typedArray.getColor(R.styleable.Common_ShapeTextView_common_stv_selectedStrokeColor, mStrokeColor) // 默认是普通状态的描边颜色
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.Common_ShapeTextView_common_stv_strokeWidth, mStrokeWidth)
        mPressedStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.Common_ShapeTextView_common_stv_pressedStrokeWidth, mStrokeWidth) // 默认是普通状态的描边宽度
        mSelectedStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.Common_ShapeTextView_common_stv_selectedStrokeWidth, mStrokeWidth) // 默认是普通状态的描边宽度
        mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.Common_ShapeTextView_common_stv_cornerRadius, mCornerRadius)
        mCornerPosition = typedArray.getInt(R.styleable.Common_ShapeTextView_common_stv_cornerPosition, mCornerPosition)
        mStartColor = typedArray.getColor(R.styleable.Common_ShapeTextView_common_stv_startColor, mStartColor)
        mEndColor = typedArray.getColor(R.styleable.Common_ShapeTextView_common_stv_endColor, mEndColor)
        mOrientation = typedArray.getColor(R.styleable.Common_ShapeTextView_common_stv_orientation, mOrientation)
        typedArray.recycle()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun initBackground() { // 渐变色
        if (mStartColor != -0x1 && mEndColor != -0x1) {
            normalGradientDrawable.colors = intArrayOf(mStartColor, mEndColor)
            if (mOrientation == 0) {
                normalGradientDrawable.orientation = GradientDrawable.Orientation.TOP_BOTTOM
                pressedGradientDrawable.orientation = GradientDrawable.Orientation.TOP_BOTTOM
                selectedGradientDrawable.orientation = GradientDrawable.Orientation.TOP_BOTTOM
            } else if (mOrientation == 1) {
                normalGradientDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
                pressedGradientDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
                selectedGradientDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
            }
        } else { // 初始化normal状态填充色
            normalGradientDrawable.setColor(mFillColor)
            pressedGradientDrawable.setColor(mPressedFillColor)
            selectedGradientDrawable.setColor(mSelectedFillColor)
        }
        // 初始化颜色及状态
        normalGradientDrawable.shape = GradientDrawable.RECTANGLE
        pressedGradientDrawable.shape = GradientDrawable.RECTANGLE
        selectedGradientDrawable.shape = GradientDrawable.RECTANGLE
        // 统一设置圆角半径
        if (mCornerPosition == -1) {
            val radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mCornerRadius.toFloat(), resources.displayMetrics)
            normalGradientDrawable.cornerRadius = radius
            pressedGradientDrawable.cornerRadius = radius
            selectedGradientDrawable.cornerRadius = radius
        } else { // 根据圆角位置设置圆角半径
            val radii = cornerRadiusByPosition
            normalGradientDrawable.cornerRadii = radii
            pressedGradientDrawable.cornerRadii = radii
            selectedGradientDrawable.cornerRadii = radii
        }
        // 默认的透明边框不绘制,否则会导致没有阴影
        if (mStrokeColor != 0) {
            normalGradientDrawable.setStroke(mStrokeWidth, mStrokeColor)
            pressedGradientDrawable.setStroke(mPressedStrokeWidth, mPressedStrokeColor)
            selectedGradientDrawable.setStroke(mSelectedStrokeWidth, mSelectedStrokeColor)
        }
        mStateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), pressedGradientDrawable)
        mStateListDrawable.addState(intArrayOf(android.R.attr.state_selected), selectedGradientDrawable)
        mStateListDrawable.addState(intArrayOf(), normalGradientDrawable)
        if (mStrokeWidth > 0 || mCornerRadius > 0 || mFillColor != Color.TRANSPARENT) {
            background = mStateListDrawable
        }
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
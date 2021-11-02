package com.example.dell.databindingapp.widget;

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.Nullable
import androidx.viewpager2.widget.ViewPager2
import com.example.dell.databindingapp.base.App
import com.example.dell.databindingapp.util.dip2px
import com.example.dell.databindingapp.util.dp


/**
 * Created by DengLongFei
 * 2020/06/28
 * 点指示器
 */
class DotPageIndicator @JvmOverloads constructor(
        context: Context, @Nullable attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private var normalColor = Color.parseColor("#999999")
    private var selectColor = Color.parseColor("#67F0AC")
    private var indicatorWidth = 9.dp

    //边距
//    val padding = 0f

    //当前位置
    private var positions = 0

    //当前偏移百分比
    private var positionOffsets = 0f

    /**
     * normal状态下的图片
     */
    private var indicatorBitmap: Bitmap? = null

    private var empty = 0

    private var totalPage = 0
    val paint = Paint()

    private  var r : Float

    init {
        // 设置画笔
        paint.isAntiAlias = true
        paint.isDither = true
        paint.isFilterBitmap = true
        paint.style = Paint.Style.FILL
        r = App.Companion.instance().applicationContext.dip2px(3f).toFloat()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 计算两边留白
        empty = ((measuredWidth - totalPage * indicatorWidth) / 2).toInt()
        // 绘制正常情况的indicator
        drawNormal(canvas)
        // 绘制选中情况额indicator
        drawSelect(canvas)
    }

    private fun drawNormal(canvas: Canvas, centerX: Float, centerY: Float) {
        paint.color = normalColor
        paint.apply { canvas.drawCircle(centerX, centerY, r, this) }
    }

    private fun drawSelect(canvas: Canvas, centerX: Float, centerY: Float) {
        paint.color = selectColor
        paint.apply { canvas.drawCircle(centerX, centerY, r, this) }
    }

    private fun drawNormal(canvas: Canvas) {
        // 图层
        val normalLayer = canvas.saveLayer(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint, Canvas.ALL_SAVE_FLAG)
        indicatorBitmap?.recycle()
        indicatorBitmap = null
        // 如果indicatorBitmap有就不用绘制，否则生成和控件一样大小的bitmap
//        if (indicatorBitmap == null) {
        indicatorBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
//        }
        indicatorBitmap?.also {
            // 使用新的canvas保存indicatorBitmap
            val indicatorCanvas = Canvas(it)
            // 绘制全部常态的indicator
            for (i in 0 until totalPage) {
                drawNormal(indicatorCanvas, empty + i * indicatorWidth, measuredHeight.toFloat() / 2)
            }
        }
//        }
        // 绘制indicatorBitmap
        indicatorBitmap?.also { canvas.drawBitmap(it, 0f, 0f, paint) }
        // 恢复图层
        canvas.restoreToCount(normalLayer)
    }

    private fun drawSelect(canvas: Canvas) {
        val selectLayer = canvas.saveLayer(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint, Canvas.ALL_SAVE_FLAG)
        drawSelect(canvas, empty + positions * indicatorWidth + indicatorWidth * positionOffsets, measuredHeight.toFloat() / 2)
        canvas.restoreToCount(selectLayer)
    }

    /**
     * 最多显示5个 多余中间停留不滑动
     */
    fun attachToRecyclerView(viewPager2: ViewPager2, maxCunt: Int, onPageSelected: ((Int) -> Unit)? = null) {
        //中间开始位置
        var startPos = 2
        //中间结束位置
        var endPos = 0
        var middleCunt = 0

        if (maxCunt > 5) {
            endPos = maxCunt - 3
            middleCunt = maxCunt - 5
            totalPage = 5
        } else {
            totalPage = maxCunt
        }
        // 设置滑动监听，动态绘制选中的Indicator
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                onPageSelected?.invoke(position)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (maxCunt <= 5) {
                    positions = position
                    positionOffsets = positionOffset
                    postInvalidate()
                } else {
                    //位置大于结束位置
                    if (position >= endPos) {
                        positions = position - middleCunt
                        positionOffsets = positionOffset
                        postInvalidate()
                    }
                    //位置小于开始位置
                    if (position < startPos) {
                        positions = position
                        positionOffsets = positionOffset
                        postInvalidate()
                    }

                }

            }
        })

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        indicatorBitmap?.recycle()
        indicatorBitmap = null
    }
}
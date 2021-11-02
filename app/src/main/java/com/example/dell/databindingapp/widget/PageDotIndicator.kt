package com.example.dell.databindingapp.widget;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.Scroller
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
class PageDotIndicator @JvmOverloads constructor(
    context: Context, @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private var maxShowCount: Int = 0
    private var onPageSelected: ((Int) -> Unit)? = null
    private var offestX: Int = 0
    private var startX: Int = 0
    private var normalColor = Color.parseColor("#999999")
    private var selectColor = Color.parseColor("#67F0AC")
    private var indicatorSpace = 4.dp
    private lateinit var scroller: Scroller

    //当前位置
    private var selectedIndex = 0
    private var lastSelectedIndex = 0
    private var totalPage = 0
    val paint = Paint()

    private var r: Float = 0f

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            onPageSelected?.invoke(position)
//            offestX = 0
//            val slideToRightSide = position < selectedIndex
            selectedIndex = position
////            invalidate()
//            if (lastSelectedIndex < selectedIndex){
//                if (selectedIndex >=3 && (totalPage - selectedIndex -1 >=2)){
//                    var deltaX = (indicatorSpace + 2*r).toInt()
//                    smoothScrollBy(deltaX)
//                }else{
//                    invalidate()
//                }
//
//            }else{
//                if (selectedIndex >=3){
//                    var deltaX = (indicatorSpace + 2*r).toInt()
//                    smoothScrollBy(-deltaX)
//                }else{
//                    invalidate()
//                }
//            }
//            lastSelectedIndex = selectedIndex


        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//            val slideToRightSide = (selectedIndex == position && positionOffset != 0f)
//            IMLog.e("zwz","onPageScrolled position=$position;selectedIndex=$selectedIndex;slideToRightSide=$slideToRightSide")
//            if (selectedIndex == position) {
//                if (slideToRightSide) {
//                    offestX = ((positionOffset * (indicatorSpace + 2*r)).toInt())
//                    invalidate()
//                } else {
//                    offestX = -((positionOffset  * (indicatorSpace + 2*r)).toInt())
//                    invalidate()
//                }
//            }


            selectedIndex = position
            offestX = ((positionOffset * (indicatorSpace + 2*r)).toInt())
            postInvalidate()


//            selectedIndex = position
        }
    }

    init {
        // 设置画笔
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        r = App.Companion.instance().applicationContext.dip2px(3f).toFloat()
        scroller = Scroller(getContext())
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 计算两边留白
        val measuredWidth = measuredWidth
        startX = if (totalPage % 2 != 0) {
            measuredWidth / 2 - (totalPage / 2 * (indicatorSpace + r * 2) + r).toInt()
        } else {
            measuredWidth / 2 - ((totalPage / 2 - 1) * (indicatorSpace + r * 2) + r + indicatorSpace * 1.0f / 2).toInt()
        }

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
//        if (maxShowCount == totalPage){
//            for (i in 0 until maxShowCount) {
//                drawNormal(canvas, startX + i * (r * 2 + indicatorSpace) + r, measuredHeight.toFloat() / 2)
//            }
//        }else{
//            for (i in 0 until maxShowCount) {
//                if (selectedIndex < maxShowCount-1){
//                    drawNormal(canvas, startX + i * (r * 2 + indicatorSpace) + r, measuredHeight.toFloat() / 2)
//                }else{
//                    drawNormal(canvas, startX + i * (r * 2 + indicatorSpace) + r, measuredHeight.toFloat() / 2)
//                }
//
//            }
//        }
        val offest = totalPage - selectedIndex - 1

        for (i in 0 until totalPage) {
            drawNormal(canvas, startX + i * (r * 2 + indicatorSpace) + r, measuredHeight.toFloat() / 2)
        }
    }

    private var selectedX : Int= 0
    private fun drawSelect(canvas: Canvas) {
//        selectedX += offestX
        drawSelect(canvas, startX + selectedIndex * (r * 2 + indicatorSpace) + r + offestX, measuredHeight.toFloat() / 2)
    }

    /**
     * 最多显示5个 多余中间停留不滑动
     */
    fun attachToRecyclerView(viewPager2: ViewPager2, maxCunt: Int,maxShowCount:Int, onPageSelected: ((Int) -> Unit)? = null) {
        this.onPageSelected = onPageSelected
        selectedIndex = 0
        totalPage = maxCunt
        this.maxShowCount = maxCunt.coerceAtMost(maxShowCount)

        viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback)
        viewPager2.registerOnPageChangeCallback(onPageChangeCallback)

    }

    fun smoothScrollBy(deltaX: Int) {
        scroller.startScroll(scroller.currX, 0, deltaX, 0, 1000)
        invalidate()
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }
}
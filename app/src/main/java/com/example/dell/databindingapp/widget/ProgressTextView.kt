package com.example.dell.databindingapp.widget;

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatTextView
import com.example.dell.databindingapp.util.dip2px


/**
 * Created by DengLongFei
 * 2020/07/23
 * 进度Text
 */
class ProgressTextView @JvmOverloads constructor(
        context: Context, @Nullable attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    //进度
    var progress = 0f
        set(value) {
            field = value
            postInvalidate()
        }
    private val paint = Paint()

    init {
        paint.strokeWidth = dip2px(1f).toFloat()//设置画笔的宽度
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#FF45D590")

        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                view ?: return
                outline?.setRoundRect(0, 0, view.width, view.height, (view.height / 2).toFloat())
            }
        }
        clipToOutline = true
    }


    override fun onDraw(canvas: Canvas?) {
        canvas?.drawRect(RectF(0f, 0f, width * progress, height.toFloat()), paint)
        super.onDraw(canvas)
    }
}
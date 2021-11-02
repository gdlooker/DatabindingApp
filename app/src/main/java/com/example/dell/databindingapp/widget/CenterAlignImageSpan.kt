package com.example.dell.databindingapp.widget;

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan


/**
 * 图片居中
 */
class CenterAlignImageSpan : ImageSpan {
    constructor(d: Drawable) : super(d)
    constructor(d: Drawable, verticalAlignment: Int) : super(d, verticalAlignment)

    private var mBounds =Rect()// lazily becomes a new Rect()

    fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        mBounds.left=left
        mBounds.top=top
        mBounds.right=right
        mBounds.bottom=bottom
    }

    /**
     * @Linpeng 2020/12/09
     * 图标和字体相对偏差值计算
     * view的高度基于内容绘制去计算，文本基于baseLine绘制，上下有偏量值，
     *
     * 不设置getSize出问题原因：单独发送 “表情”，TextView的高由于没有text，计算是按照ImageSpan的实际高来作为计算标准，
     *      但是draw方法根据中心值计算（其实已经居中，此处反而相对view的实际高度下移），导致显示不全
     *
     * getSize作用：根据ImageSpan大小来强制指定FontMetricsInt的绘制基准，来限制text文本绘制基线
     *
     * 【FontMetricsInt介绍】
     *      baseLine：一行文字的底线。
     *      Ascent： 字符顶部到baseLine的距离。
     *      Descent： 字符底部到baseLine的距离。
     *      Leading： 字符行间距。
     *
     * [文本绘制]                 height: 55  top: 0 bottom: 55 top:-39 bottom:11 descent:10 ascent:-39
     * [根据ImageSpan来绘制]      height: 55  top: 0 bottom: 55 top:-39 bottom:15 descent:15 ascent:-39
     */
    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int,
                         fontMetricsInt: FontMetricsInt?): Int {
        val drawable = drawable
        drawable.bounds=mBounds
        val rect: Rect = mBounds
        if (fontMetricsInt != null) {
            val fmPaint = paint.fontMetricsInt
            val fontHeight = fmPaint.bottom - fmPaint.top
            val drHeight: Int = rect.bottom - rect.top
//            LogUtil.v("Felix", "[Size-init] height: ${drawable.bounds.height()}  top: ${drawable.bounds.top} bottom: ${drawable.bounds.bottom}" +
//                    "\n top:${fontMetricsInt.top } bottom:${ fontMetricsInt.bottom} descent:${fontMetricsInt.descent} ascent:${fontMetricsInt.ascent}")

            val top = drHeight / 2 - fontHeight / 4
            val bottom = drHeight / 2 + fontHeight / 4
            fontMetricsInt.ascent = -bottom
            fontMetricsInt.top = -bottom
            fontMetricsInt.bottom = top
            fontMetricsInt.descent = top

//            LogUtil.v("Felix", "[Size] height: ${drawable.bounds.height()}  top: ${drawable.bounds.top} bottom: ${drawable.bounds.bottom}" +
//                    "\n top:${fontMetricsInt.top } bottom:${ fontMetricsInt.bottom} descent:${fontMetricsInt.descent} ascent:${fontMetricsInt.ascent}")
        }
        return rect.right
    }


    override fun draw(canvas: Canvas, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int,
                      paint: Paint) {
        val drawable = drawable
        drawable.bounds=mBounds
        val fm: Paint.FontMetricsInt = paint.fontMetricsInt
        //计算y方向的位移
        //Bounds是获取包裹image的矩形框尺寸；
        //y + fm.descent得到字体的descent线坐标
        //y + fm.ascent得到字体的ascent线坐标；
        //两者相加除以2就是两条线中线的坐标；
        //b.getBounds().bottom是image的高度（试想把image放到原点），除以2即高度一半；
        //前面得到的中线坐标减image高度的一半就是image顶部要绘制的目标位置；
        val translationY: Int = (y + fm.descent + y + fm.ascent) / 2 - drawable.bounds.bottom / 2
//        LogUtil.v("Felix", "[drawable] height: ${drawable.bounds.height()}  top: ${drawable.bounds.top} bottom: ${drawable.bounds.bottom}" +
//                "\n x:${x} y:${y} descent:${fm.descent} ascent:${fm.ascent}" +
//                "\n translationY:${translationY}")
        canvas.save()
        //绘制图片位移一段距离
        canvas.translate(x, translationY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
    }
}
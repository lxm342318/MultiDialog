package com.ym.multidialog.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*


/**
 * @author : lxm
 * @package_name ：com.ym.multidialog.view
 * @date : 2021/5/21 10:58
 * @description ：
 */
class OriView : View {

    private var mPaint: Paint = Paint()
    private var random: Random = Random()
    private val oriX = 300
    private val oriY = 300
    private val oriR = 30
    private val orir = 200
    private val oriNum = 6
    private val color = "#8F52AAF5"
    private var oriDegrees = 0f
    private var ori = 1

    private val oriSize = 10

    private val oriList: MutableList<Ori> = ArrayList(oriSize)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {

    }


    private fun init() {
        mPaint.color = Color.parseColor(color)
        mPaint.isAntiAlias = true
        mPaint.textAlign = Paint.Align.CENTER
        object : Thread() {
            override fun run() {
                super.run()
                while (true) {
                    try {
                        sleep(20)
                        ori++
                        if (oriList.size != oriSize) {
                            val o = Ori()
                            o.x = random.nextInt(720)
                            o.y = random.nextInt(1280)
                            o.r = 5 + random.nextInt(20)
                            oriList.add(o)
                        }
                        for (o in oriList) {
                            if (o.x != oriX) {
                                if (o.x > oriX) {
                                    o.x--
                                } else {
                                    o.x++
                                }
                            }
                            if (o.y != oriY) {
                                if (o.y > oriY) {
                                    o.y--
                                } else {
                                    o.y++
                                }
                            }
                        }
                        postInvalidate()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        oriDegrees = getCircumference(oriNum, 0, oriDegrees)
        canvas.rotate(ori.toFloat(), oriX.toFloat(), oriY.toFloat())
        onDrawOval(canvas, oriX - oriR, oriY - oriR, orir)
        canvas.restore()
        if (oriList.isNotEmpty()) {
            var i = 0
            while (i < oriList.size) {
                val o: Ori = oriList[i]
                Log.e("oriori", o.x.toString() + "  " + o.y + "   " + o.r)
                canvas.drawOval(
                    (o.x - o.r).toFloat(),
                    (o.y - o.r).toFloat(),
                    (o.x + o.r).toFloat(),
                    (o.y + o.r).toFloat(), mPaint
                )
                if ((o.x - o.r > oriX - orir) && (o.x + o.r < oriX + orir) && (o.y - o.r > oriY - orir) and (o.y + o.r < oriY + orir)) {
                    Log.e("oriori", "移除")
                    oriList.remove(o)
                    i--
                }
                i++
            }
        }
    }


    private fun onDrawOval(canvas: Canvas, x: Int, y: Int, r: Int) {
        canvas.save()
        for (i in 0 until oriNum) {
            canvas.drawOval(
                (x - r).toFloat(), (y - r).toFloat(), (x + r).toFloat(),
                (y + r).toFloat(), mPaint
            )
            canvas.rotate(oriDegrees, oriX.toFloat(), oriY.toFloat())
        }
        canvas.restore()
    }

    /**
     * 返回当前圆周率
     *
     * @param number               一圈有多少个圆
     * @param arrive               当前在第几个圆
     * @param currentCircumference 当前圆周率
     * @return
     */
    private fun getCircumference(
        number: Int,
        arrive: Int,
        currentCircumference: Float
    ): Float {
        return if (currentCircumference > -(360f / number) * (arrive - 1))
            currentCircumference - 1
        else -(360f / number) * (arrive - 1)
    }

    internal class Ori {
        var x = 0
        var y = 0
        var r = 0
    }


}
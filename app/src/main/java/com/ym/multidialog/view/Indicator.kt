package com.ym.multidialog.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.ym.multidialog.R
import java.util.*

/**
 * @author : lxm
 * @package_name ：com.ym.multidialog.view
 * @date : 2021年5月21日16:21:48
 * @description ：
 */
class Indicator : View {

    private var paint: Paint? = null
    private var stepNum = 10
    private var duration = 3000
    private var barNum = 3
    private var barColor = -0x1000000
    private var viewHeight = 0
    private var viewWidth = 0
    private var animList: MutableList<ValueAnimator>? = null
    private var pause = false

    constructor(context: Context?) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(
        context,
        attrs
    ) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Indicator, 0, 0)
        barNum = typedArray.getInt(R.styleable.Indicator_bar_num, 3)
        stepNum = typedArray.getInt(R.styleable.Indicator_step_num, 10)
        duration = typedArray.getInt(R.styleable.Indicator_bar_duration, 3000)
        barColor = typedArray.getColor(
            R.styleable.Indicator_bar_color,
            ContextCompat.getColor(getContext(), R.color.colorPrimary)
        )
        typedArray.recycle()

    }

    constructor(
        context: Context?,
        attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    fun setPause(pause: Boolean) {
        this.pause = pause
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        val floatList =
            getGraduateFloatList(stepNum, viewHeight)
        generateAnim(floatList, barNum)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint = Paint()
        paint?.color = barColor
        paint?.isAntiAlias = true
        drawIndicator(canvas, barNum)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(viewWidth, viewHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun getGraduateFloatList(
        arraySize: Int,
        max: Int
    ): MutableList<Float?> {
        val floatList: MutableList<Float?> =
            ArrayList()
        val dividedMax = max / arraySize.toDouble()
        for (i in 1..arraySize) {
            val a = i * dividedMax
            floatList.add(a.toFloat())
        }
        floatList[floatList.size - 1] = floatList[0]
        return floatList
    }

    private fun generateAnim(
        floatList: MutableList<Float?>,
        barNum: Int
    ) {
        animList = ArrayList()
        for (i in 0 until barNum) {
            floatList.shuffle()
            floatList[floatList.size - 1] = floatList[0]
            val floatArray = FloatArray(floatList.size)
            var j = 0
            for (f in floatList) {
                floatArray[j++] = f ?: Float.NaN
            }
            val anim = ValueAnimator.ofFloat(*floatArray)
            anim.duration = duration.toLong()
            anim.repeatCount = ValueAnimator.INFINITE
            anim.interpolator = LinearInterpolator()
            anim.addUpdateListener {
                if (!pause)
                    invalidate()
            }
            anim.start()
            (animList as ArrayList<ValueAnimator>).add(anim)
        }
    }

    private fun drawIndicator(canvas: Canvas, barNum: Int) {
        val spaceWidth = canvas.width * 0.2 / (barNum - 1)
        val barWidth = canvas.width * 0.5 / barNum
        val sumWidth = spaceWidth + barWidth
        for (i in 0 until barNum - 1) {
            val height = animList!![i].animatedValue as Float
            canvas.drawRoundRect( // left
                (i * sumWidth).toFloat(),  //height, // top
                canvas.height - height,  // right
                (i * sumWidth + barWidth).toFloat(),  // bottom
                canvas.height.toFloat(), 10f, 10f,  // Paint
                paint!!
            )
            if (i == barNum - 2) {
                val heightLast =
                    animList!![i + 1].animatedValue as Float
                canvas.drawRoundRect( // left
                    ((i + 1) * sumWidth).toFloat(),  //height, // top
                    canvas.height - heightLast,  // right
                    ((i + 1) * sumWidth + barWidth).toFloat(),  // bottom
                    canvas.height.toFloat(), 10f, 10f,  // Paint
                    paint!!
                )
            }
        }
    }

    fun setStepNum(stepNum: Int) {
        this.stepNum = stepNum
    }

    fun setDuration(duration: Int) {
        this.duration = duration
    }

    fun setBarNum(barNum: Int) {
        this.barNum = barNum
    }

    fun setBarColor(barColor: Int) {
        this.barColor = barColor
    }
}
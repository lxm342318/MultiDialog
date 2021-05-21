package com.ym.multidialog.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.ym.multidialog.R

/**
 * @author : lxm
 * @package_name ：com.ym.multidialog.view
 * @date : 2021年5月21日16:03:56
 * @description ：
 */
class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    // 绘制画笔
    private val mBackPaint: Paint
    private val mProgPaint: Paint
    // 绘制区域
    private var mRectF: RectF? = null
    // 圆环渐变色
    private var mColorArray: IntArray?
    // 圆环进度(0-100)
    private var mProgress: Float

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val viewWide = measuredWidth - paddingLeft - paddingRight
        val viewHigh = measuredHeight - paddingTop - paddingBottom
        val mRectLength =
            (viewWide.coerceAtMost(viewHigh) - mBackPaint.strokeWidth.coerceAtLeast(mProgPaint.strokeWidth)).toInt()
        val mRectL = paddingLeft + (viewWide - mRectLength) / 2
        val mRectT = paddingTop + (viewHigh - mRectLength) / 2
        mRectF = RectF(
            mRectL.toFloat(),
            mRectT.toFloat(),
            (mRectL + mRectLength).toFloat(),
            (mRectT + mRectLength).toFloat()
        )
        // 设置进度圆环渐变色
        mColorArray?.run {
            if (this.size > 1) {
                mProgPaint.shader = LinearGradient(
                    0F,
                    0F,
                    0F,
                    measuredWidth.toFloat(),
                    this,
                    null,
                    Shader.TileMode.MIRROR
                )
            }
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mRectF?.let {
            canvas.drawArc(it, 0f, 360f, false, mBackPaint)
        }
        mRectF?.let {
            canvas.drawArc(it, 275f, 360 * mProgress / 100, false, mProgPaint)
        }
    }
    // ---------------------------------------------------------------------------------------------
    /**
     * 获取当前进度
     *
     * @return 当前进度（0-100）
     */
    fun getProgress(): Float {
        return mProgress
    }

    /**
     * 设置当前进度
     *
     * @param progress 当前进度（0-100）
     */
    fun setProgress(progress: Float) {
        mProgress = progress
        invalidate()
    }

    /**
     * 设置当前进度，并展示进度动画。如果动画时间小于等于0，则不展示动画
     *
     * @param progress 当前进度（0-100）
     * @param animTime 动画时间（毫秒）
     */
    fun setProgress(progress: Float, animTime: Long) {
        if (animTime <= 0) {
            setProgress(progress)
        } else {
            val animator = ValueAnimator.ofFloat(mProgress, progress)
            animator.addUpdateListener { animation ->
                mProgress = animation.animatedValue as Float
                invalidate()
            }
            animator.interpolator = OvershootInterpolator()
            animator.duration = animTime
            animator.start()
        }
    }

    /**
     * 设置背景圆环宽度
     *
     * @param width 背景圆环宽度
     */
    fun setBackWidth(width: Int) {
        mBackPaint.strokeWidth = width.toFloat()
        invalidate()
    }

    /**
     * 设置背景圆环颜色
     *
     * @param color 背景圆环颜色
     */
    fun setBackColor(@ColorRes color: Int) {
        mBackPaint.color = ContextCompat.getColor(context, color)
        invalidate()
    }

    /**
     * 设置进度圆环宽度
     *
     * @param width 进度圆环宽度
     */
    fun setProgWidth(width: Int) {
        mProgPaint.strokeWidth = width.toFloat()
        invalidate()
    }

    /**
     * 设置进度圆环颜色
     *
     * @param color 景圆环颜色
     */
    fun setProgColor(@ColorRes color: Int) {
        mProgPaint.color = ContextCompat.getColor(context, color)
        mProgPaint.shader = null
        invalidate()
    }

    /**
     * 设置进度圆环颜色(支持渐变色)
     *
     * @param startColor 进度圆环开始颜色
     * @param firstColor 进度圆环结束颜色
     */
    fun setProgColor(@ColorRes startColor: Int, @ColorRes firstColor: Int) {
        mColorArray = intArrayOf(
            ContextCompat.getColor(context, startColor),
            ContextCompat.getColor(context, firstColor)
        )
        mColorArray?.run {
            mProgPaint.shader = LinearGradient(
                0F,
                0F,
                0F,
                measuredWidth.toFloat(),
                this,
                null,
                Shader.TileMode.MIRROR
            )
        }
        invalidate()
    }

    /**
     * 设置进度圆环颜色(支持渐变色)
     *
     * @param colorArray 渐变色集合
     */
    fun setProgColor(@ColorRes colorArray: IntArray?) {
        if (colorArray == null || colorArray.size < 2) {
            return
        }
        mColorArray = IntArray(colorArray.size)
        for (index in colorArray.indices) {
            mColorArray!![index] = ContextCompat.getColor(context, colorArray[index])
        }
        mColorArray?.run {
            mProgPaint.shader = LinearGradient(
                0F,
                0F,
                0F,
                measuredWidth.toFloat(),
                this,
                null,
                Shader.TileMode.MIRROR
            )
        }
        invalidate()
    }

    init {
        @SuppressLint("Recycle") val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CircularProgressView)
        // 初始化背景圆环画笔
        mBackPaint = Paint()
        // 只描边，不填充
        mBackPaint.style = Paint.Style.STROKE
        // 设置圆角
        mBackPaint.strokeCap = Paint.Cap.ROUND
        // 设置抗锯齿
        mBackPaint.isAntiAlias = true
        // 设置抖动
        mBackPaint.isDither = true
        mBackPaint.strokeWidth = typedArray.getDimension(
            R.styleable.CircularProgressView_backWidth,
            5f
        )
        mBackPaint.color = typedArray.getColor(
            R.styleable.CircularProgressView_backColor,
            Color.LTGRAY
        )
        // 初始化进度圆环画笔
        mProgPaint = Paint()
        // 只描边，不填充
        mProgPaint.style = Paint.Style.STROKE
        // 设置圆角
        mProgPaint.strokeCap = Paint.Cap.ROUND
        // 设置抗锯齿
        mProgPaint.isAntiAlias = true
        // 设置抖动
        mProgPaint.isDither = true
        mProgPaint.strokeWidth = typedArray.getDimension(
            R.styleable.CircularProgressView_progressWidth,
            10f
        )
        mProgPaint.color = typedArray.getColor(
            R.styleable.CircularProgressView_progressColor,
            Color.BLUE
        )
        // 初始化进度圆环渐变色
        val startColor =
            typedArray.getColor(R.styleable.CircularProgressView_progressStartColor, -1)
        val firstColor =
            typedArray.getColor(R.styleable.CircularProgressView_progressFirstColor, -1)
        mColorArray = if (startColor != -1 && firstColor != -1)
            intArrayOf(startColor, firstColor)
        else
            null
        // 初始化进度
        mProgress = typedArray.getFloat(R.styleable.CircularProgressView_progress, 0f)
        typedArray.recycle()
    }
}
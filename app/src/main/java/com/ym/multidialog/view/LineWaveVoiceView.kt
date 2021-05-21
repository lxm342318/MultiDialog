package com.ym.multidialog.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.ym.multidialog.R
import com.ym.multidialog.utils.ThreadPoolManager.Companion.getCachedThreadPool
import java.util.*
import kotlin.math.roundToInt

/**
 * @author : lxm
 * @package_name ：com.ym.multidialog.view
 * @date : 2021/5/21 16:28
 * @description ：
 */
class LineWaveVoiceView : View {

    private var paint: Paint? = null
    //矩形波纹颜色
    private var lineColor = 0
    //矩形波纹宽度
    private var lineWidth = 0f
    private var textSize = 0f
    private var text = DEFAULT_TEXT
    private var textColor = 0
    private var isStart = false
    private var mRunnable: Runnable? = null
    //默认矩形波纹的宽度，9像素, 原则上从layout的attr获得
    private val LINE_W = 9
    //最小的矩形线高，是线宽的2倍，线宽从lineWidth获得
    private val MIN_WAVE_H = 1
    //最高波峰，是线宽的4倍
    private val MAX_WAVE_H = 10
    //默认矩形波纹的高度，总共10个矩形，左右各有10个
    private val DEFAULT_WAVE_HEIGHT =
        intArrayOf(2, 3, 4, 3, 2, 2, 2, 2, 2, 2, 3, 4, 3, 2)
    private val mWaveList = LinkedList<Int>()
    //右边波纹矩形的数据，10个矩形复用一个rectF
    private val rectRight: RectF = RectF()
    //左边波纹矩形的数据
    private val rectLeft: RectF = RectF()
    var list: LinkedList<Int> = LinkedList()

    constructor(context: Context?) : super(context)

//    private var audioRecordManager: AudioRecordManager? = null
//    fun setThread(audioRecordManager: AudioRecordManager?) {
//        this.audioRecordManager = audioRecordManager
//    }

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        paint = Paint()
        resetList(list, DEFAULT_WAVE_HEIGHT)
        mRunnable = Runnable {
            while (isStart) {
                refreshElement()
                try {
                    Thread.sleep(UPDATE_INTERVAL_TIME.toLong())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                postInvalidate()
            }
        }
        val mTypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.LineWaveVoiceView)
        lineColor = mTypedArray.getColor(
            R.styleable.LineWaveVoiceView_voiceLineColor,
            ContextCompat.getColor(getContext(), R.color.colorPrimary)
        )
        lineWidth =
            mTypedArray.getDimension(R.styleable.LineWaveVoiceView_voiceLineWidth, LINE_W.toFloat())
        textSize = mTypedArray.getDimension(R.styleable.LineWaveVoiceView_voiceTextSize, 42f)
        textColor = mTypedArray.getColor(
            R.styleable.LineWaveVoiceView_voiceTextColor,
            ContextCompat.getColor(getContext(), R.color.colorPrimary)
        )
        mTypedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val widthCentre = width / 2
        val heightCentre = height / 2
        //更新时间
        paint?.strokeWidth = 0f
        paint?.color = textColor
        paint?.textSize = textSize
        val textWidth = paint?.measureText(text) ?: 1F
        paint?.run {
            canvas.drawText(
                text,
                widthCentre - textWidth / 2,
                heightCentre - (this.ascent() + this.descent()) / 2,
                this
            )
        }
        //更新左右两边的波纹矩形
        paint?.color = lineColor
        paint?.style = Paint.Style.FILL
        paint?.strokeWidth = lineWidth
        paint?.isAntiAlias = true
        for (i in DEFAULT_WAVE_HEIGHT.indices) {
            if (list.size == 0 || list.size - 1 < i) {
                return
            }
            //右边矩形
            rectRight.left = widthCentre + 2 * i * lineWidth + textWidth / 2 + lineWidth
            rectRight.top = heightCentre - list[i] * lineWidth / 2
            rectRight.right = widthCentre + 2 * i * lineWidth + 2 * lineWidth + textWidth / 2
            rectRight.bottom = heightCentre + list[i] * lineWidth / 2
            //左边矩形
            rectLeft.left = widthCentre - (2 * i * lineWidth + textWidth / 2 + 2 * lineWidth)
            rectLeft.top = heightCentre - list[i] * lineWidth / 2
            rectLeft.right = widthCentre - (2 * i * lineWidth + textWidth / 2 + lineWidth)
            rectLeft.bottom = heightCentre + list[i] * lineWidth / 2
            canvas.drawRoundRect(rectRight, 6f, 6f, paint!!)
            canvas.drawRoundRect(rectLeft, 6f, 6f, paint!!)
        }
    }

    @Synchronized
    private fun refreshElement() {
        //if (audioRecordManager != null) {
        var maxAmp = 1F
        maxAmp /= 2
        //wave 在 2 ~ 7 之间
        val waveH = MIN_WAVE_H + (maxAmp * (MAX_WAVE_H - 2)).roundToInt()
        list.add(0, waveH)
        list.removeLast()
        //}
    }

    @Synchronized
    fun setText(text: String) {
        this.text = text
        postInvalidate()
    }

    @Synchronized
    fun startRecord() {
        isStart = true
        getCachedThreadPool().execute(mRunnable)
    }

    @Synchronized
    fun stopRecord() {
        isStart = false
        mWaveList.clear()
        resetList(list, DEFAULT_WAVE_HEIGHT)
        text = DEFAULT_TEXT
        postInvalidate()
    }

    private fun resetList(
        list: MutableList<Int>?,
        array: IntArray
    ) {
        list?.clear()
        for (i in array.indices) {
            list?.add(array[i])
        }
    }

    companion object {
        private val TAG =
            LineWaveVoiceView::class.java.simpleName
        private const val DEFAULT_TEXT = "00:00"
        private const val UPDATE_INTERVAL_TIME = 100 //100ms更新一次
    }
}
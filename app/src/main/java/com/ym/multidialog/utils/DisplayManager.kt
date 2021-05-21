package com.ym.multidialog.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * @author : lxm
 * @package_name ：com.jganzhiyun.controlbox.uitls
 * @date : 2021/3/18 16:13
 * @description ： 屏幕适配
 */

object DisplayManager {


    private var displayMetrics: DisplayMetrics? = null

    private var screenWidth: Int? = null

    private var screenHeight: Int? = null

    private var screenDpi: Int? = null

    fun init(context: Context) {
        displayMetrics = context.resources.displayMetrics
        screenWidth = displayMetrics?.widthPixels
        screenHeight = displayMetrics?.heightPixels
        screenDpi = displayMetrics?.densityDpi
    }


    //UI图的大小
    private const val STANDARD_WIDTH = 1080
    private const val STANDARD_HEIGHT = 1920


    fun getScreenWidth(): Int? {
        return screenWidth
    }

    fun getScreenHeight(): Int? {
        return screenHeight
    }


    /**
     * 传入UI图中问题的高度，单位像素
     * @param size
     * @return
     */
    fun getPaintSize(size: Int): Int? {
        return getRealHeight(size)
    }

    /**
     * 输入UI图的尺寸，输出实际的px
     *
     * @param px ui图中的大小
     * @return
     */
    fun getRealWidth(px: Int): Int? {
        //ui图的宽度
        return getRealWidth(px, STANDARD_WIDTH.toFloat())
    }

    /**
     * 输入UI图的尺寸，输出实际的px,第二个参数是父布局
     *
     * @param px          ui图中的大小
     * @param parentWidth 父view在ui图中的高度
     * @return
     */
    fun getRealWidth(px: Int, parentWidth: Float): Int? {
        getScreenWidth()?.let {
            return (px / parentWidth * it).toInt()
        }
        return -1
    }

    /**
     * 输入UI图的尺寸，输出实际的px
     *
     * @param px ui图中的大小
     * @return
     */
    fun getRealHeight(px: Int): Int? {
        //ui图的宽度
        return getRealHeight(px, STANDARD_HEIGHT.toFloat())
    }

    /**
     * 输入UI图的尺寸，输出实际的px,第二个参数是父布局
     *
     * @param px           ui图中的大小
     * @param parentHeight 父view在ui图中的高度
     * @return
     */
    fun getRealHeight(px: Int, parentHeight: Float): Int? {
        getScreenWidth()?.let {
            return (px / parentHeight * it).toInt()
        }
        return -1
    }

    /**
     * dip转px
     * @param dipValue
     * @return int
     */
    fun dip2px(dipValue: Int): Int {
        displayMetrics?.density?.let {
            return (dipValue * it + 0.5f).toInt()
        }
        return -1
    }

    /**
     * px转dip
     * @param pxValue
     * @return int
     */
    fun px2dip(pxValue: Float): Int? {
        displayMetrics?.density?.let {
            return (pxValue / it + 0.5f).toInt()
        }
        return -1
    }

    /**
     * dp转px
     * @param dpValue
     * @return Float
     */
    fun dp2px(dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
                displayMetrics
        ).toInt()

    }

    /**
     * sp转px
     * @param spValue
     * @return Float
     */
    fun sp2px(spValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spValue,
                displayMetrics
        )
    }

}
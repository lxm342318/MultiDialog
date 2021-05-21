package com.ym.multidialog.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.ym.multidialog.R

/**
 * @author : lxm
 * @package_name ：com.ym.multidialog.view
 * @date : 2021年5月20日19:13:34
 * @description ：
 */
class MaxHeightRecyclerView : RecyclerView {

    private var mMaxHeight = 0

    constructor(context: Context) : super(context)

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        initialize(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initialize(context, attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun initialize(
        context: Context,
        attrs: AttributeSet?
    ) {
        val arr =
            context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerViews)
        mMaxHeight = arr.getLayoutDimension(R.styleable.MaxHeightRecyclerViews_maxHeight, mMaxHeight)
        arr.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpecs = heightMeasureSpec
        if (mMaxHeight > 0)
            heightMeasureSpecs = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, heightMeasureSpecs)
    }
}

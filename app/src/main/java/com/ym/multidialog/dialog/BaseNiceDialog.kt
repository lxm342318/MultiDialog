package com.ym.multidialog.dialog

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ym.multidialog.R
import com.ym.multidialog.utils.DisplayManager

/**
 * @author : lxm
 * @package_name ：com.ym.multidialog.dialog
 * @date : 2021年5月20日19:26:18
 * @description ： 提示框
 */
abstract class BaseNiceDialog : AppCompatDialogFragment() {

    private val MARGIN = "margin"
    private val WIDTH = "width"
    private val HEIGHT = "height"
    private val DIM = "dim_amount"
    private val BOTTOM = "show_bottom"
    private val CANCEL = "out_cancel"
    private val ANIM = "anim_style"
    private val LAYOUT = "layout_id"
    private var content : View ? = null
    private var margin = 0 //左右边距
    private var width = 0 //宽度
    private var height = 0 //高度
    private var dimAmount = 0.5f //灰度深浅
    private var showBottom = false //是否底部显示
    private var outCancel = true //是否点击外部取消

    @StyleRes
    private var animStyle = 0
    @LayoutRes
    protected var layoutId = 0

    abstract fun intLayoutId(): Int

    abstract fun convertView(
        holder: ViewHolder?,
        dialog: BaseNiceDialog?
    )

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.ActionSheetDialogStyle)
        layoutId = intLayoutId()
        //恢复保存的数据
        savedState?.let {
            margin = it.getInt(MARGIN)
            width = it.getInt(WIDTH)
            height = it.getInt(HEIGHT)
            dimAmount = it.getFloat(DIM)
            showBottom = it.getBoolean(BOTTOM)
            outCancel = it.getBoolean(CANCEL)
            animStyle = it.getInt(ANIM)
            layoutId = it.getInt(LAYOUT)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val root  = layoutInflater.inflate(layoutId,null)
        content?.run {
            root.findViewById<LinearLayout>(R.id.content_view).addView(this)
        }
        convertView(ViewHolder.create(root), this)
        return root
    }

    override fun onStart() {
        super.onStart()
        initParams()
    }

    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     *
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MARGIN, margin)
        outState.putInt(WIDTH, width)
        outState.putInt(HEIGHT, height)
        outState.putFloat(DIM, dimAmount)
        outState.putBoolean(BOTTOM, showBottom)
        outState.putBoolean(CANCEL, outCancel)
        outState.putInt(ANIM, animStyle)
        outState.putInt(LAYOUT, layoutId)
    }

    private fun initParams() {

        dialog?.window?.let {
            val lp = it.attributes
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount
            //是否在底部显示
            if (showBottom) {
                lp.gravity = Gravity.BOTTOM
                if (animStyle == 0) {
                    animStyle = R.style.ActionSheetDialogStyle
                }
            }
            //设置dialog宽度
            when (width) {
                0 -> {
                    lp.width =
                        DisplayManager.getScreenWidth()?.let {
                            -2 * DisplayManager.dp2px(margin.toFloat())
                        } ?: 0
                }
                -1 -> {
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                }
                else -> {
                    lp.width = DisplayManager.dp2px(width.toFloat())
                }
            }
            //设置dialog高度
            if (height == 0) {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            } else {
                lp.height = DisplayManager.dp2px(height.toFloat())
            }
            //设置dialog进入、退出的动画
            it.setWindowAnimations(animStyle)
            it.attributes = lp
        }
        isCancelable = outCancel
    }

    fun setMargin(margin: Int): BaseNiceDialog? {
        this.margin = margin
        return this
    }

    fun setWidth(width: Int): BaseNiceDialog? {
        this.width = width
        return this
    }

    fun setHeight(height: Int): BaseNiceDialog? {
        this.height = height
        return this
    }

    fun setDimAmount(dimAmount: Float): BaseNiceDialog? {
        this.dimAmount = dimAmount
        return this
    }

    fun setShowBottom(showBottom: Boolean): BaseNiceDialog? {
        this.showBottom = showBottom
        return this
    }

    fun setOutCancel(outCancel: Boolean): BaseNiceDialog? {
        this.outCancel = outCancel
        return this
    }

    fun setAnimStyle(@StyleRes animStyle: Int): BaseNiceDialog? {
        this.animStyle = animStyle
        return this
    }

    fun setContentView(content : View): BaseNiceDialog {
        this.content = content
        return this
    }

    fun show(manager: FragmentManager): BaseNiceDialog {
        val ft = manager.beginTransaction()
        if (this.isAdded)
            ft.remove(this).commitAllowingStateLoss()
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.add(this, System.currentTimeMillis().toString())
        ft.commitAllowingStateLoss()
        return this
    }

}
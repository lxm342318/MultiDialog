package com.ym.multidialog.dialog

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ym.multidialog.NULL_CHARACTER
import com.ym.multidialog.R
import com.ym.multidialog.model.ChooseInfo
import java.util.*

/**
 * @author : lxm
 * @package_name ：com.ym.multidialog.dialog
 * @date : 2021年5月20日19:25:30
 * @description ： 底部选择对话框
 */
abstract class BaseChooseDialog : AppCompatDialogFragment(){

    var data: MutableList<ChooseInfo> = ArrayList()
    var checked: MutableList<Int> = ArrayList()//选中position集合
    private var title: String = NULL_CHARACTER //title
    private var multiple = false //是否多选
    private var outCancel = true //是否点击外部取消
    private var dimAmount = 0.5f //灰度深浅
    private val DIM = "dim_amount"
    private val CANCEL = "out_cancel"
    private val ANIM = "anim_style"
    private val LAYOUT = "layout_id"
    @LayoutRes
    protected var layoutId = 0
    @StyleRes
    protected var animStyle = R.style.ActionSheetDialogAnimation

    abstract fun intLayoutId(): Int

    abstract fun convertView(
        holder: ViewHolder,
        dialog: BaseChooseDialog
    )


    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.ActionSheetDialogStyle)
        savedState?.let {
            dimAmount = it.getFloat(DIM)
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
        val view = inflater.inflate(layoutId, container, false)
        convertView(ViewHolder.create(view), this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        setDialog()
    }

    /**
     * 设置dialog属性
     */
    private fun setDialog() {
        dialog?.window?.let {
            val height = DisplayMetrics().heightPixels
            val layoutParams = it.attributes
            layoutParams.x = 0
            layoutParams.y = height
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            //调节灰色背景透明度[0-1]，默认0.5f
            layoutParams.dimAmount = dimAmount
            layoutParams.gravity = Gravity.BOTTOM
            // 设置显示位置
            //设置dialog进入、退出的动画
            it.setWindowAnimations(animStyle)
            it.attributes = layoutParams
        }
        isCancelable = outCancel
    }


    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     *
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat(DIM, dimAmount)
        outState.putBoolean(CANCEL, outCancel)
        outState.putInt(ANIM, animStyle)
        outState.putInt(LAYOUT, layoutId)
    }


    fun setData(data: MutableList<ChooseInfo>): BaseChooseDialog {
        this.data = data
        return this
    }

    //fun getData() = data

    fun setChecked(checked: MutableList<Int>): BaseChooseDialog {
        this.checked = checked
        return this
    }

    //fun getChecked() = checked

    fun setTitle(title: String): BaseChooseDialog{
        this.title = title
        return this
    }

    fun getTitle() = title

    fun setMultiple(multiple: Boolean): BaseChooseDialog {
        this.multiple = multiple
        return this
    }

    fun getMultiple() = multiple

    fun setOutCancel(outCancel: Boolean): BaseChooseDialog {
        this.outCancel = outCancel
        return this
    }

    fun setAnimStyle(@StyleRes animStyle: Int): BaseChooseDialog {
        this.animStyle = animStyle
        return this
    }

    fun setDimAmount(dimAmount: Float): BaseChooseDialog {
        this.dimAmount = dimAmount
        return this
    }


    fun show(manager: FragmentManager?): BaseChooseDialog{
        val ft = manager?.beginTransaction()
        if (this.isAdded)
            ft?.remove(this)?.commitAllowingStateLoss()
        ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft?.add(this, System.currentTimeMillis().toString())
        ft?.commitAllowingStateLoss()
        return this
    }




}


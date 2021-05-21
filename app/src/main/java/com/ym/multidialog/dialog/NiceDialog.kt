package com.ym.multidialog.dialog

import android.os.Bundle
import androidx.annotation.LayoutRes

/**
 * @author : lxm
 * @package_name ：com.ym.multidialog.dialog
 * @date : 2021年5月20日19:26:18
 * @description ： 提示框
 */
class NiceDialog : BaseNiceDialog() {

    private var convertListener: ViewConvertListener<NiceDialog>? = null
    override fun intLayoutId(): Int {
        return layoutId
    }

    override fun convertView(holder: ViewHolder?, dialog: BaseNiceDialog?) {

        convertListener?.convertView(holder, dialog as NiceDialog)
    }


    fun setLayoutId(@LayoutRes layoutId: Int): NiceDialog {
        this.layoutId = layoutId
        return this
    }

    fun setConvertListener(convertListener: ViewConvertListener<NiceDialog>?): NiceDialog {
        this.convertListener = convertListener
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            convertListener = savedInstanceState.getParcelable("listener")
        }
    }

    /**
     * 保存接口
     *
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("listener", convertListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        convertListener = null
    }

    companion object {
        fun init(): NiceDialog {
            return NiceDialog()
        }
    }
}

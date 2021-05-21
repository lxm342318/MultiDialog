package com.ym.multidialog.dialog

import android.os.Bundle
import androidx.annotation.LayoutRes

/**
 * @author : lxm
 * @package_name ：com.ym.multidialog.dialog
 * @date : 2021年5月20日19:26:18
 * @description ： 底部选择对话框
 */
class ChooseDialog : BaseChooseDialog(){

    private var convertListener: ViewConvertListener<ChooseDialog>? = null

    companion object{
        fun init():ChooseDialog{
            return ChooseDialog()
        }
    }


    override fun intLayoutId(): Int {
        return layoutId
    }


    fun setLayoutId(@LayoutRes layoutId: Int): ChooseDialog{
        this.layoutId = layoutId
        return this
    }

    fun setConvertListener(convertListener: ViewConvertListener<ChooseDialog>?): ChooseDialog {
        this.convertListener = convertListener
        return this
    }

    override fun convertView(holder: ViewHolder, dialog: BaseChooseDialog) {
        convertListener?.convertView(holder, dialog as ChooseDialog)
    }

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        savedState?.let {
            convertListener = it.getParcelable("listener")
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

}
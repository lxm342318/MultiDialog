package com.ym.multidialog.dialog

import android.util.SparseArray
import android.view.View
import android.widget.TextView

/**
 * @author : lxm
 * @package_name ：com.ym.multidialog.dialog
 * @date : 2021年5月20日19:27:13
 * @description ：
 */
class ViewHolder private constructor(view: View) {

    private var convertView: View = view
    private val views: SparseArray<View?> = SparseArray()

    fun <T : View?> getView(viewId: Int): T? {
        var view = views[viewId]
        if (view == null) {
            view = convertView.findViewById(viewId)
            views.put(viewId, view)
        }
        return view as T?
    }

    fun getConvertView(): View {
        return convertView
    }

    fun setText(viewId: Int, text: String?) {
        val textView = getView<TextView>(viewId)
        textView?.text = text
    }

    fun setText(viewId: Int, textId: Int) {
        val textView = getView<TextView>(viewId)
        textView?.setText(textId)
    }

    fun setTextColor(viewId: Int, colorId: Int) {
        val textView = getView<TextView>(viewId)
        textView?.setTextColor(colorId)
    }

    fun setOnClickListener(
        viewId: Int,
        clickListener: View.OnClickListener?
    ) {
        val view = getView<View>(viewId)
        view?.setOnClickListener(clickListener)
    }

    fun setBackgroundResource(viewId: Int, resId: Int) {
        val view = getView<View>(viewId)
        view?.setBackgroundResource(resId)
    }

    fun setBackgroundColor(viewId: Int, colorId: Int) {
        val view = getView<View>(viewId)
        view?.setBackgroundColor(colorId)
    }

    companion object {
        fun create(view: View): ViewHolder {
            return ViewHolder(view)
        }
    }

}
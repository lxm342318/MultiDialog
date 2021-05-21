package com.ym.multidialog.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ym.multidialog.R
import com.ym.multidialog.model.ChooseInfo

/**
 * @author : lxm
 * @package_name ：com.ym.multidialog.adapter
 * @date : 2021年5月20日19:24:32
 * @description ： dialog底部选择适配器
 */
class ChooseAdapter constructor(data: MutableList<ChooseInfo>?) :
    BaseQuickAdapter<ChooseInfo, BaseViewHolder>(
        R.layout.item_choose, data
    ) {

    override fun convert(holder: BaseViewHolder, item: ChooseInfo) {
        holder.setText(R.id.tv_name, item.name)
        val iv =
            holder.getView<ImageView>(R.id.iv_select_status)
        if (item.check)
            iv.setImageResource(R.mipmap.btn_check_y)
        else
            iv.setImageResource(R.mipmap.btn_check_n)

    }
}
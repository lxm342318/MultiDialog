package com.ym.multidialog.model

/**
 * @author : lxm
 * @package_name ：com.jganzhiyun.controlbox.mvp.model
 * @date : 2021/3/29 14:04
 * @description ：选择框item
 */
data class ChooseInfo(

    var name: String? = null,
    var id: Int? = null,
    var value: String? = null,
    var check: Boolean = false) {

    constructor(name: String?, value: String?)
            : this(name, null, value, false)

    constructor(name: String?, id: Int?) : this(name, id, null, false)

}

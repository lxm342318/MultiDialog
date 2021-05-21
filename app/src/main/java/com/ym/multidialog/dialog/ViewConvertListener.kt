package com.ym.multidialog.dialog

import android.os.Parcel
import android.os.Parcelable
import androidx.appcompat.app.AppCompatDialogFragment

/**
 * @author : lxm
 * @package_name ：com.ym.multidialog.dialog
 * @date : 2021年5月20日19:27:01
 * @description ：
 */
abstract class ViewConvertListener<T : AppCompatDialogFragment>() : Parcelable {

    constructor(parcel: Parcel) : this() {
    }

    abstract fun convertView(holder: ViewHolder?, dialog: T)

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    val CREATOR: Parcelable.Creator<ViewConvertListener<T>?> = object : Parcelable.Creator<ViewConvertListener<T>?> {

            override fun createFromParcel(source: Parcel): ViewConvertListener<T>? {
                return object : ViewConvertListener<T>(source) {

                    override fun convertView(holder: ViewHolder?, dialog: T) {
                    }
                }
            }

            override fun newArray(size: Int): Array<ViewConvertListener<T>?> {
                return arrayOfNulls<ViewConvertListener<T>?>(size)
            }
        }

}
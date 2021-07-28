package com.magic.multi.theme.core.impl

import android.view.View
import com.magic.multi.theme.core.base.BaseAttr
/**
 * 皮肤视图单元集
 * view 当前视图对象
 * attrs 视图对象的属性集合
 * Created by mistletoe
 * on 7/27/21
 **/
internal class SkinView {
    var view: View? = null
    var attrs: MutableList<BaseAttr> = mutableListOf()
    fun apply() {
        view?.let {
            attrs.forEach {
                it.apply(view)
            }
        }
    }

    fun clean() {
        if (attrs.isEmpty()) {
            return
        }
        attrs.clear()
    }
}
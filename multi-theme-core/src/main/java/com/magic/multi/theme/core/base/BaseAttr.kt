package com.magic.multi.theme.core.base

import android.view.View

/**
 * Created by mistletoe
 * on 7/23/21
 **/
abstract class BaseAttr {

    /**
     * xml里某个属性的名称
     */
    var attrName: String? = null

    /**
     * xml里某个属性的值
     */
    var attrValue: Int = 0

    /**
     * xml里某个属性值的名称
     */
    var entryName: String? = null

    /**
     * xml里某个属性值的类型
     */
    var entryType: String? = null

    /**
     * file name
     * assets 里的资源名称
     */
    var attrAssetsValue: String? = null

    /**
     * 是否在解析时立即apply
     */
    open var applyImmediate: Boolean = false

    /**
     * 获取当前View attr其他属性
     */
    var getAttrsBlock: GetAttrsBlock? = null

    /**
     *  刷新该控件的该属性
     */
    abstract fun apply(view: View?)
}

typealias GetAttrsBlock = (name: String) -> Any?
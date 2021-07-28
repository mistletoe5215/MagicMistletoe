package com.magic.multi.theme.core.api

import com.magic.multi.theme.core.base.BaseAttr

/**
 * Created by mistletoe
 * on 7/27/21
 **/
interface IOperationHandler {
    /**
     * 应用主题
     */
    fun applyTheme()
    /**
     * 允许外部配置自定义View的换主题属性映射
     * @param attrMap 自定义属性映射
     */
    fun configCustomAttrs(attrMap: MutableMap<String, BaseAttr>)
    /**
     * 清空当前缓存视图集
     */
    fun clean()
}
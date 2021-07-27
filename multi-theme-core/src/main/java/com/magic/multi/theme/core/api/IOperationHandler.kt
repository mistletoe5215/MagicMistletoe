package com.magic.multi.theme.core.api

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
     * 清空当前缓存视图集
     */
    fun clean()
}
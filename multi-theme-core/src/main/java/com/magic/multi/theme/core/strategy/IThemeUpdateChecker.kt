package com.magic.multi.theme.core.strategy

/**
 * 主题包更新校验接口
 * Created by mistletoe
 * on 2021/9/10
 **/
interface IThemeUpdateChecker {
    fun needUpdate(): Boolean
}
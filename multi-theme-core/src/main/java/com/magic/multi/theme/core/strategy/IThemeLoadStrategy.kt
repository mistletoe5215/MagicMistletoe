package com.magic.multi.theme.core.strategy

import android.content.Context

/**
 * Created by mistletoe
 * on 2021/8/19
 **/
interface IThemeLoadStrategy {
    /**
     * 主题标识
     */
    val themeName: String

    /**
     * 获取/生成主题包
     */
    fun getOrGenerateThemePackage(context: Context): String?
}
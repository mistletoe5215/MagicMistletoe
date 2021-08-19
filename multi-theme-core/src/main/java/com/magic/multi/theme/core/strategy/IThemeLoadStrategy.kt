package com.magic.multi.theme.core.strategy

import android.content.Context

/**
 * Created by mistletoe
 * on 2021/8/19
 **/
interface IThemeLoadStrategy {
    val themeName:String
    fun getOrGenerateThemePackage(context: Context): String?
}
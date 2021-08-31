package com.magic.multi.theme.core.api

import android.graphics.drawable.Drawable

/**
 * Created by mistletoe
 * on 2021/8/31
 **/
interface IResourceHandler {
    fun getDrawable(resId: Int): Drawable
    fun getTextString(resId: Int): String
    fun getColor(resId: Int): Int
    fun getAssetsFilePath(fileName:String):String
}
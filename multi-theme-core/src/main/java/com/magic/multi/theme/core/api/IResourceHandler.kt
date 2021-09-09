package com.magic.multi.theme.core.api

import android.content.Context
import android.graphics.drawable.Drawable

/**
 * Created by mistletoe
 * on 2021/8/31
 **/
interface IResourceHandler {
    fun getDrawable(context: Context,resId: Int): Drawable
    fun getTextString(context: Context,resId: Int): String
    fun getColor(context: Context, resId: Int): Int
    fun getAssetsFilePath(fileName:String):String
}
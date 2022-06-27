package com.magic.multi.theme.core.api

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable

/**
 * Created by mistletoe
 * on 2021/8/31
 **/
interface IResourceHandler {
    fun getDrawable(resId: Int): Drawable
    fun getAppDrawable(resId: Int): Drawable
    fun getSkinDrawable(resId: Int): Drawable
    fun getTextString(resId: Int): String
    fun getAppTextString(resId: Int): String
    fun getSkinTextString(resId: Int): String
    fun getColor(resId: Int): Int
    fun getAppColor(resId: Int): Int
    fun getSkinColor(resId: Int): Int
    fun getDimenString(resId: Int): String
    fun getAppDimenString(resId: Int): String
    fun getSkinDimenString(resId: Int): String
    fun getColorStateList(resId: Int): ColorStateList
    fun getAppColorStateList(resId: Int): ColorStateList
    fun getSkinColorStateList(resId: Int): ColorStateList
    fun getAssetsFilePath(fileName: String): String
}
package com.magic.multi.theme.core.utils

import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.constants.AttrConstants
import com.magic.multi.theme.core.constants.AttrConstants.BACKGROUND
import com.magic.multi.theme.core.constants.AttrConstants.IMAGE_SRC
import com.magic.multi.theme.core.constants.AttrConstants.TEXT_COLOR
import com.magic.multi.theme.core.constants.AttrConstants.TEXT_VALUE
import com.magic.multi.theme.core.impl.BackgroundAttr
import com.magic.multi.theme.core.impl.ImageAttr
import com.magic.multi.theme.core.impl.TextAttr
import com.magic.multi.theme.core.impl.TextColorAttr

/**
 * 属性判别工具
 * Created by mistletoe
 * on 7/27/21
 */
internal object AttrConfig {

    val externalAttrMap = mutableMapOf<String, BaseAttr>()

    /**
     * 根据不同的属性名称创建不同的属性实例对象
     * @param attrName 属性名
     * @param attrValueRefId 属性值
     * @param attrValueRefName 属性值名称
     * @param typeName 属性值类型
     * @return 属性实例对象
     */
    fun get(
        attrName: String,
        attrValueRefId: Int,
        attrValueRefName: String?,
        typeName: String?
    ): BaseAttr? {
        //读取外部设置的属性
        if (externalAttrMap.keys.contains(attrName)) {
            val mExternalSkinAttr = externalAttrMap[attrName]
            mExternalSkinAttr?.attrName = attrName
            mExternalSkinAttr?.attrValue = attrValueRefId
            mExternalSkinAttr?.entryName = attrValueRefName
            mExternalSkinAttr?.entryType = typeName
            return mExternalSkinAttr
        }
        val mSkinAttr: BaseAttr = when (attrName) {
            BACKGROUND -> {
                BackgroundAttr()
            }
            TEXT_COLOR -> {
                TextColorAttr()
            }
            TEXT_VALUE -> {
                TextAttr()
            }
            IMAGE_SRC -> {
                ImageAttr()
            }
            else -> {
                return null
            }
        }
        mSkinAttr.attrName = attrName
        mSkinAttr.attrValue = attrValueRefId
        mSkinAttr.entryName = attrValueRefName
        mSkinAttr.entryType = typeName
        return mSkinAttr
    }

    /**
     * 检查当前属性是否支持替换
     * @param attrName 属性名
     * @return true: 支持 false: 不支持
     */
    fun isSupportedAttr(attrName: String?): Boolean {
        return AttrConstants.attrConstantList.contains(attrName)
    }
}
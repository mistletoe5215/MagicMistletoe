package com.magic.multi.theme.core.impl

import android.view.View
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.constants.AttrConstants.BACKGROUND_COLOR
import com.magic.multi.theme.core.constants.AttrConstants.BACKGROUND_DRAWABLE
import com.magic.multi.theme.core.constants.SkinConfig
import com.magic.multi.theme.core.log.MultiThemeLog

/**
 * Created by mistletoe
 * on 7/23/21
 **/
internal class BackgroundAttr : BaseAttr() {
    override fun apply(view: View?) {
        val modeStr = getAttrsBlock?.invoke(
            SkinConfig.DEFAULT_ATTR_NAME_MODE
        )
        when (entryType) {
            BACKGROUND_COLOR -> {
                view?.setBackgroundColor(
                    when (modeStr) {
                        SkinConfig.DEFAULT_ATTR_VALUE_MODE_LIGHT -> SkinLoadManager.getInstance()
                            .getAppColor(attrValue)
                        SkinConfig.DEFAULT_ATTR_VALUE_MODE_DARK -> SkinLoadManager.getInstance()
                            .getSkinColor(attrValue)
                        else -> SkinLoadManager.getInstance().getColor(attrValue)
                    }
                )
            }
            BACKGROUND_DRAWABLE -> {
                view?.background =
                    when (modeStr) {
                        SkinConfig.DEFAULT_ATTR_VALUE_MODE_LIGHT -> SkinLoadManager.getInstance()
                            .getAppDrawable(attrValue)
                        SkinConfig.DEFAULT_ATTR_VALUE_MODE_DARK -> SkinLoadManager.getInstance()
                            .getSkinDrawable(attrValue)
                        else -> SkinLoadManager.getInstance().getDrawable(attrValue)
                    }
            }
            else -> {
                MultiThemeLog.e("no match entryType")
            }
        }
    }
}
package com.magic.multi.theme.core.impl

import android.view.View
import android.widget.TextView
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.constants.AttrConstants
import com.magic.multi.theme.core.constants.SkinConfig.DEFAULT_ATTR_NAME_MODE
import com.magic.multi.theme.core.constants.SkinConfig.DEFAULT_ATTR_VALUE_MODE_DARK
import com.magic.multi.theme.core.constants.SkinConfig.DEFAULT_ATTR_VALUE_MODE_LIGHT
import com.magic.multi.theme.core.log.MultiThemeLog

/**
 * Created by mistletoe
 * on 7/27/21
 **/
internal class TextColorAttr : BaseAttr() {
    override fun apply(view: View?) {
        when (view) {
            is TextView -> {
                if (AttrConstants.TEXT_COLOR.equals(attrName, true)) {
                    val modeStr = getAttrsBlock?.invoke(DEFAULT_ATTR_NAME_MODE)
                    view.setTextColor(
                        when (modeStr) {
                            DEFAULT_ATTR_VALUE_MODE_LIGHT -> SkinLoadManager.getInstance()
                                .getAppColorStateList(attrValue)
                            DEFAULT_ATTR_VALUE_MODE_DARK -> SkinLoadManager.getInstance()
                                .getSkinColorStateList(attrValue)
                            else -> SkinLoadManager.getInstance().getColorStateList(attrValue)
                        }
                    )
                }
            }
            else -> {
                MultiThemeLog.e("no match text view instance")
            }
        }
    }
}
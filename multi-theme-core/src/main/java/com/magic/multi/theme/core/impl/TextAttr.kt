package com.magic.multi.theme.core.impl

import android.view.View
import android.widget.TextView
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.constants.AttrConstants
import com.magic.multi.theme.core.constants.SkinConfig
import com.magic.multi.theme.core.log.MultiThemeLog

/**
 * Created by mistletoe
 * on 7/27/21
 **/
internal class TextAttr : BaseAttr() {
    override fun apply(view: View?) {
        when (view) {
            is TextView -> {
                if (AttrConstants.TEXT_VALUE.equals(attrName, true)) {
                    val modeStr = getAttrsBlock?.invoke(
                        SkinConfig.DEFAULT_ATTR_NAME_MODE
                    )
                    view.text = when (modeStr) {
                        SkinConfig.DEFAULT_ATTR_VALUE_MODE_LIGHT -> SkinLoadManager.getInstance()
                            .getAppTextString(attrValue)
                        SkinConfig.DEFAULT_ATTR_VALUE_MODE_DARK -> SkinLoadManager.getInstance()
                            .getSkinTextString(attrValue)
                        else -> SkinLoadManager.getInstance().getTextString(attrValue)
                    }


                }
            }
            else -> {
                MultiThemeLog.e("no match text view instance")
            }
        }
    }
}
package com.magic.multi.theme.core.impl

import android.view.View
import android.widget.TextView
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.constants.AttrConstants
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
                    view.text = SkinLoadManager.getInstance().getTextString(attrValue)
                }
            }
            else -> {
                MultiThemeLog.e("no match text view instance")
            }
        }
    }
}
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
internal class TextColorAttr : BaseAttr() {
    override fun apply(view: View?) {
        when (view) {
            is TextView -> {
                if (AttrConstants.TEXT_COLOR.equals(attrName, true)) {
                    view.setTextColor(SkinLoadManager.getInstance().getColorStateList(attrValue))
                }
            }
            else -> {
                MultiThemeLog.e("no match text view instance")
            }
        }
    }
}
package com.magic.multi.theme.core.impl

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.constants.AttrConstants

/**
 * Created by mistletoe
 * on 7/27/21
 **/
internal class TextColorAttr : BaseAttr() {
    override fun apply(view: View?) {
        when (view) {
            is TextView -> {
                if (AttrConstants.TEXT_COLOR.equals(attrName, true)) {
                    view.setTextColor(
                        SkinLoadManager.getInstance().getColor(view.context, attrValue)
                    )
                }
            }
            is AppCompatTextView -> {
                if (AttrConstants.TEXT_COLOR.equals(attrName, true)) {
                    view.setTextColor(
                        SkinLoadManager.getInstance().getColor(view.context, attrValue)
                    )
                }
            }
        }
    }
}
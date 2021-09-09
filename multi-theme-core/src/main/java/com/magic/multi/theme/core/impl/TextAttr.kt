package com.magic.multi.theme.core.impl

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.constants.AttrConstants
import com.magic.multi.theme.core.constants.SkinConfig

/**
 * Created by mistletoe
 * on 7/27/21
 **/
internal class TextAttr : BaseAttr() {
    override fun apply(view: View?) {
        when (view) {
            is TextView -> {
                if (AttrConstants.TEXT_VALUE.equals(attrName, true)) {
                    view.text = SkinLoadManager.getInstance().getTextString(view.context, attrValue)
                }
            }
            is AppCompatTextView -> {
                if (AttrConstants.TEXT_VALUE.equals(attrName, true)) {
                    view.text = SkinLoadManager.getInstance().getTextString(view.context, attrValue)
                }
            }
            else -> {
                Log.e(SkinConfig.MULTI_THEME_TAG, "no match text view instance")
            }
        }
    }
}
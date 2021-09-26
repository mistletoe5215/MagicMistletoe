package com.magic.multi.theme.core.impl

import android.view.View
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.constants.AttrConstants.BACKGROUND_COLOR
import com.magic.multi.theme.core.constants.AttrConstants.BACKGROUND_DRAWABLE
import com.magic.multi.theme.core.log.MultiThemeLog

/**
 * Created by mistletoe
 * on 7/23/21
 **/
internal class BackgroundAttr : BaseAttr() {
    override fun apply(view: View?) {
        when (entryType) {
            BACKGROUND_COLOR -> {
                view?.setBackgroundColor(SkinLoadManager.getInstance().getColor(attrValue))
            }
            BACKGROUND_DRAWABLE -> {
                view?.background = SkinLoadManager.getInstance().getDrawable(attrValue)
            }
            else -> {
                MultiThemeLog.e("no match entryType")
            }
        }
    }
}
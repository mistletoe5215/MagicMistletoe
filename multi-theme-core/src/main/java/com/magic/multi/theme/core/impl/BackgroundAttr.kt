package com.magic.multi.theme.core.impl

import android.util.Log
import android.view.View
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.constants.AttrConstants.BACKGROUND_COLOR
import com.magic.multi.theme.core.constants.AttrConstants.BACKGROUND_DRAWABLE
import com.magic.multi.theme.core.constants.SkinConfig.MULTI_THEME_TAG

/**
 * Created by mistletoe
 * on 7/23/21
 **/
internal class BackgroundAttr : BaseAttr() {
    override fun apply(view: View?) {
        when (entryType) {
            BACKGROUND_COLOR -> {
                view?.let {
                    it.setBackgroundColor(
                        SkinLoadManager.getInstance().getColor(it.context, attrValue)
                    )
                }
            }
            BACKGROUND_DRAWABLE -> {
                view?.let {
                    it.background = SkinLoadManager.getInstance().getDrawable(it.context, attrValue)
                }
            }
            else -> {
                Log.e(MULTI_THEME_TAG, "no match entryType")
            }
        }
    }
}
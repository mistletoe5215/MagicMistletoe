package com.magic.multi.theme.core.impl

import android.view.View
import android.widget.ImageView
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.constants.AttrConstants.IMAGE_SRC
import com.magic.multi.theme.core.log.MultiThemeLog

/**
 * Created by mistletoe
 * on 7/27/21
 **/
internal class ImageAttr : BaseAttr() {
    override fun apply(view: View?) {
        when (view) {
            is ImageView -> {
                if (IMAGE_SRC.equals(attrName, true)) {
                    view.setImageDrawable(SkinLoadManager.getInstance().getDrawable(attrValue))
                }
            }
            else -> {
                MultiThemeLog.e( "no match image view instance")
            }
        }
    }
}
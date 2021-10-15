package com.magic.mistletoe

import android.view.View
import android.widget.ImageView
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.base.BaseAttr

/**
 * Created by mistletoe
 * on 15/10/21
 **/
class ImageViewMultiAlphaAttr : BaseAttr() {
    companion object {
        const val IMAGE_VIEW_MULTI_ALPHA = "multiAlpha"
    }

    override fun apply(view: View?) {
        if (view is ImageView) {
            if (IMAGE_VIEW_MULTI_ALPHA.equals(attrName, true)) {
                view.alpha = SkinLoadManager.getInstance().getInteger(attrValue)/100f
            }
        }
    }
}
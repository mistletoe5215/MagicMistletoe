package com.magic.mistletoe

import android.view.View
import android.widget.ImageView
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.base.BaseAttr
import java.lang.Exception

/**
 * Created by mistletoe
 * on 15/10/21
 **/
class ImageViewAlphaAttr : BaseAttr() {
    companion object {
        const val IMAGE_VIEW_ALPHA = "alpha"
    }

    override fun apply(view: View?) {
        if (view is ImageView) {
            if (IMAGE_VIEW_ALPHA.equals(attrName, true)) {
                val alphaValueString = SkinLoadManager.getInstance().getDimenString(attrValue)
                view.alpha = try {
                    alphaValueString.toFloat()
                } catch (e: Exception) {
                    1f
                }
            }
        }
    }
}
package com.magic.multi.theme.core.impl

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.constants.AttrConstants.IMAGE_SRC
import com.magic.multi.theme.core.constants.SkinConfig

/**
 * Created by mistletoe
 * on 7/27/21
 **/
class ImageAttr : BaseAttr() {
    override fun apply(view: View?) {
        when (view) {
            is ImageView -> {
                if (IMAGE_SRC.equals(attrName, true)) {
                    view.setImageDrawable(SkinLoadManager.getInstance().getDrawable(attrValue))
                }
            }
            is AppCompatImageView -> {
                if (IMAGE_SRC.equals(attrName, true)) {
                    view.setImageDrawable(SkinLoadManager.getInstance().getDrawable(attrValue))
                }
            }
            else -> {
                Log.e(SkinConfig.MULTI_THEME_TAG, "no match image view instance")
            }
        }
    }
}
package com.magic.multi.theme.core.impl

import android.view.View
import android.widget.ImageView
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.constants.AttrConstants.IMAGE_SRC
import com.magic.multi.theme.core.constants.SkinConfig
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
                    val modeStr = getAttrsBlock?.invoke(
                        SkinConfig.DEFAULT_ATTR_NAME_MODE
                    )

                    view.setImageDrawable(
                        when (modeStr) {
                            SkinConfig.DEFAULT_ATTR_VALUE_MODE_LIGHT -> SkinLoadManager.getInstance()
                                .getAppDrawable(attrValue)
                            SkinConfig.DEFAULT_ATTR_VALUE_MODE_DARK -> SkinLoadManager.getInstance()
                                .getSkinDrawable(attrValue)
                            else -> SkinLoadManager.getInstance().getDrawable(attrValue)
                        }
                    )
                }
            }
            else -> {
                MultiThemeLog.e("no match image view instance")
            }
        }
    }
}
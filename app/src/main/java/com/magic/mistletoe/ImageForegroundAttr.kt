package com.magic.mistletoe

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.base.BaseAttr

/**
 * Created by mistletoe
 * on 7/28/21
 **/
class ImageForegroundAttr:BaseAttr() {
    override fun apply(view: View?) {
       when(view){
           is AppCompatImageView ->{
               if ("foreground".equals(attrName, true)) {
                   view.foreground = SkinLoadManager.getInstance().getDrawable(attrValue)
               }
           }
           is ImageView ->{
               if ("foreground".equals(attrName, true)) {
                   view.foreground = SkinLoadManager.getInstance().getDrawable(attrValue)
               }
           }
       }
    }
}
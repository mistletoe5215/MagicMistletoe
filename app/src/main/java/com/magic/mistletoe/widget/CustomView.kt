package com.magic.mistletoe.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import com.magic.multi.theme.core.annotation.UpdateTheme

/**
 * Created by mistletoe
 * on 2021/9/1
 **/
class CustomView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    @UpdateTheme
    fun updateTheme(){
        Log.d("Mistletoe","CustomView  updateTheme executed")
    }
}
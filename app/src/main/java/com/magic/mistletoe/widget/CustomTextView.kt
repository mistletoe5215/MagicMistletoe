package com.magic.mistletoe.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import com.magic.multi.theme.core.annotation.UpdateTheme

/**
 * Created by mistletoe
 * on 2021/10/18
 **/
class CustomTextView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {
    @UpdateTheme
    fun updateTheme(){
        Log.d("Mistletoe","CustomTextView  updateTheme executed")
    }
}
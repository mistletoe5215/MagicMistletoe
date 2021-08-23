package com.magic.mistletoe.utils

import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by mistletoe
 * on 2021/8/23
 **/
val Number.dp2px
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()
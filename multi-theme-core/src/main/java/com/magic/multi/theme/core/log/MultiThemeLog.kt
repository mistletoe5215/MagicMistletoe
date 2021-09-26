package com.magic.multi.theme.core.log

import android.util.Log
import com.magic.multi.theme.core.constants.SkinConfig.MULTI_THEME_TAG

/**
 * Created by mistletoe
 * on 2021/9/26
 **/
object MultiThemeLog {
    private var isEnable = false
    fun v(msg: String) {
        withEnable {
            Log.v(MULTI_THEME_TAG, msg)
        }
    }

    fun d(msg: String) {
        withEnable {
            Log.d(MULTI_THEME_TAG, msg)
        }
    }

    fun i(msg: String) {
        withEnable {
            Log.i(MULTI_THEME_TAG, msg)
        }
    }

    fun w(msg: String) {
        withEnable {
            Log.w(MULTI_THEME_TAG, msg)
        }
    }

    fun e(msg: String) {
        withEnable {
            Log.e(MULTI_THEME_TAG, msg)
        }
    }

    fun enable(isEnable: Boolean) {
        this.isEnable = isEnable
    }

    private inline fun withEnable(block: () -> Unit) {
        if (isEnable) {
            block.invoke()
        }
    }
}
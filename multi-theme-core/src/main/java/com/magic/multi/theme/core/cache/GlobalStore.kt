package com.magic.multi.theme.core.cache

import android.content.res.Resources
import java.util.concurrent.ConcurrentHashMap

/**
 * @brief
 * @author mistletoe
 * @date 2022/6/28
 **/
object GlobalStore {
    val themeCache = ConcurrentHashMap<String,Resources>()
}